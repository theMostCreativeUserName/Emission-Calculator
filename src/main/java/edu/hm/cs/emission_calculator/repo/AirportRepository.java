package edu.hm.cs.emission_calculator.repo;

import edu.hm.cs.emission_calculator.model.AirportCode;
import edu.hm.cs.emission_calculator.model.AirportRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Represents Airport repository, calls from database.
 */
class AirportRepository implements AirportDao {

    /**
     * Base SQL-query.
     */
    private static final String START_QUERY = "select id, latitudedeg, longitudedeg, iatacode, name " +
          "from public.\"airport_codes\" where iatacode is not null";

    /**
     * Jdbc-Template; Database of this project.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Returns all airports with valid id, latitude, longitude and iata-code.
     *
     * @return List of airports
     */
    @Override
    public List<AirportCode> getMembers() {
        return jdbcTemplate.query(START_QUERY, new AirportRowMapper());
    }

    @Override
    public AirportCode getById(final int id) {
        final String sql = START_QUERY + " and id='" + id + "'";
        try {
            return (AirportCode) this.jdbcTemplate.query(sql, new AirportRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public AirportCode getByIatacode(final String iataCode) {
        final String sql = START_QUERY+" and iatacode='" + iataCode + "' and scheduledservice='yes'";
        try {
            return (AirportCode) this.jdbcTemplate.query(sql, new AirportRowMapper()).get(0);

        } catch (EmptyResultDataAccessException ex) {
            System.out.println("error while accessing data");
            return null;
        }
    }

    @Override
    public List<AirportCode> findAll() {
        return jdbcTemplate.query("select * from public.\"airport_codes\"", new AirportRowMapper());
    }

    @Override
    public double getLat(final String iataCode) {
        return getByIatacode(iataCode).getLatitudedeg();
    }

    @Override
    public double getLong(final String iataCode) {
        return getByIatacode(iataCode).getLongitudedeg();
    }
}
