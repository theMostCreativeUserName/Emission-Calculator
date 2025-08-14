package edu.hm.cs.emission_calculator.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * maps query returns to objects of type AirportCode.
 */
public class AirportRowMapper implements RowMapper {

    /**
     * method mapping data.
     *
     * @param resultSet result of an SQL query.
     * @param i         not needed for this implementation
     * @return mapped airportCode class
     * @throws SQLException if error occurred while reading in data.
     */
    @Override
    public Object mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final AirportCode airportCode = new AirportCode();
        airportCode.setId(resultSet.getInt("id"));
        airportCode.setLatitudedeg(resultSet.getDouble("latitudedeg"));
        airportCode.setLongitudedeg(resultSet.getDouble("longitudedeg"));
        airportCode.setIatacode(resultSet.getString("iatacode"));
        airportCode.setName(resultSet.getString("name"));

        return airportCode;
    }
}
