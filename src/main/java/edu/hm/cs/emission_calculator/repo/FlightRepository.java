package edu.hm.cs.emission_calculator.repo;

import edu.hm.cs.emission_calculator.model.FlightData;
import edu.hm.cs.emission_calculator.model.FlightRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository for handling flight entries.
 */
class FlightRepository implements FlightDao {
    /**
     * Base SQL-query.
     */
    private static final String START_QUERY = "select * " +
          "from public.\"flight_data\"";
    /**
     * Insert SQL-query.
     */
    private static final String INSERT_QUERY = "INSERT INTO public.flight_data(" +
          "startingdate, iatadest, destname, iatastart, startname, travelclass, authoritynumber, authorityname, distance, emission, id)" +
          "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %s, '%s', %s, %s, %s);";

    /**
     * Jdbc-Template; Database of this project.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<FlightData> getById(final int id) {
        final String sql = START_QUERY + " where id=" + id + ";";
        try {
            return jdbcTemplate.query(sql, new FlightRowMapper());
        } catch (EmptyResultDataAccessException | IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public List<FlightData> getByIataCode(final String iatacode) {
        final String sql = START_QUERY + " where iatastart='" + iatacode + "' or iatadest='" + iatacode + "'";
        try {
            return jdbcTemplate.query(sql, new FlightRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<FlightData> findAll() {
        return jdbcTemplate.query(START_QUERY, new FlightRowMapper());
    }

    @Override
    public int add(final FlightData data) {
        data.setId(getValidId());
        final String sql = String.format(INSERT_QUERY
              , data.getStartingDate(), data.getIataDest(), data.getDestName(), data.getIataStart(), data.getStartName()
              , data.getTravelClass(), data.getAuthorityNumber()
              , data.getAuthorityName(), data.getDistance(), data.getEmission(), getValidId());
        jdbcTemplate.update(sql);
        return findAll().size();
    }


    @Override
    public boolean remove(final int id) {
        final String sql = "DELETE FROM public.\"flight_data\" WHERE id=" + id + ";";
        final int before = findAll().size();
        jdbcTemplate.update(sql);
        return before > findAll().size();
    }

    @Override
    public void clear() {
        final String sql = "TRUNCATE TABLE public.\"flight_data\";";
        jdbcTemplate.update(sql);
    }

    @Override
    public Map<FlightData, Integer> mapView() {
        final List<FlightData> data = findAll();
        data.forEach(flight -> flight.setId(0));
        final Set<FlightData> dataSet = new HashSet(data);
        final Map<FlightData, Integer> result = new HashMap<>();
        final List<FlightData> resultList = List.copyOf(dataSet);

        resultList.forEach(flight -> result.put(flight, getMultiples(flight)));
        result.forEach((flight, integer) -> {
            flight.setId(chooseID(flight));
            final double singleEmission = flight.getEmission();
            flight.setEmission(singleEmission * integer);
        });
        return result;
    }


    /**
     * Returns calculated Id for new entry.
     *
     * @return int of id
     */
    private int getValidId() {
        if (findAll().isEmpty()) {
            return 0;
        }
        final String sql = "select * from public.\"flight_data\" where id = (select max(id) from public.\"flight_data\");";
        final FlightData data = (FlightData) jdbcTemplate.query(sql, new FlightRowMapper()).get(0);
        return data.getId() + 1;
    }

    /**
     * Returns doubles of given flight data.
     *
     * @param data flight data to look for
     * @return number of all multiples
     */
    private int getMultiples(final FlightData data) {
        final String sql = START_QUERY + "where startingdate='" + data.getStartingDate() + "' and iatadest='" + data.getIataDest()
              + "' and iatastart='" + data.getIataStart() + "' and travelclass='" + data.getTravelClass()
              + "' and authoritynumber='" + data.getAuthorityNumber() + "';";
        System.out.println(sql);
        try {
            return jdbcTemplate.query(sql, new FlightRowMapper()).size();
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }
    /**
     * Returns a present id for a dataset.
     *
     * @param data flight data to look for
     * @return valid id
     */
    private int chooseID(final FlightData data) {
        final String sql = START_QUERY + "where startingdate='" + data.getStartingDate() + "' and iatadest='" + data.getIataDest()
              + "' and iatastart='" + data.getIataStart() + "' and travelclass='" + data.getTravelClass()
              + "' and authoritynumber='" + data.getAuthorityNumber() + "';";
        System.out.println(sql);
        try {
             final List<FlightData> result = jdbcTemplate.query(sql, new FlightRowMapper());
             return result.get(0).getId();
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }

}
