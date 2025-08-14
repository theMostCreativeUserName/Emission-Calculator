package edu.hm.cs.emission_calculator.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * maps query returns to objects of type FlightData.
 */
public class FlightRowMapper implements RowMapper {
    @Override
    public Object mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final FlightData flightData = new FlightData();
        flightData.setId(resultSet.getInt("id"));
        flightData.setAuthorityNumber(resultSet.getInt("authoritynumber"));
        flightData.setDistance(resultSet.getDouble("distance"));
        flightData.setEmission(resultSet.getDouble("emission"));
        flightData.setIataDest(resultSet.getString("iatadest"));
        flightData.setIataStart(resultSet.getString("iatastart"));
        flightData.setTravelClass(resultSet.getString("travelclass"));
        flightData.setAuthorityName(resultSet.getString("authorityname"));
        flightData.setStartingDate(resultSet.getDate("startingdate"));
        flightData.setStartName(resultSet.getString("startname"));
        flightData.setDestName(resultSet.getString("destname"));
        return flightData;
    }
}
