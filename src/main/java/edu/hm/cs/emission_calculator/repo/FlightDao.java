package edu.hm.cs.emission_calculator.repo;

import edu.hm.cs.emission_calculator.model.FlightData;

import java.util.List;
import java.util.Map;

/**
 * Flight Dao.
 */
public interface FlightDao {

    /**
     * Makes a FlightRepository class.
     * @return new FlightRepository
     */
    static FlightRepository makeFlightRepo(){
        return new FlightRepository();
    }
    /**
     * Searches for flights by id.
     *
     * @param id id to look for
     * @return found FlightData
     */
    List<FlightData> getById(int id);

    /**
     * Searches for flights by iataCode.
     *
     * @param iatacode iata to look for
     * @return found flight data
     * either start or dest aiport code matches
     */
    List<FlightData> getByIataCode(String iatacode);

    /**
     * Returns all flightData.
     *
     * @return List of FlightData
     */
    List<FlightData> findAll();

    /**
     * Adds another FlightData class.
     *
     * @param data flight to add
     * @return new number of flights saved
     */
    int add(FlightData data);

    /**
     * Deletes a flight by id.
     *
     * @param id id to look for
     * @return true, if removed
     * false, otherwise
     */
    boolean remove(int id);

    /**
     * Clears entire table.
     */
    void clear();

    /**
     * Formats database for dashboard.
     * @return formatted data
     */
    Map<FlightData, Integer> mapView();

}
