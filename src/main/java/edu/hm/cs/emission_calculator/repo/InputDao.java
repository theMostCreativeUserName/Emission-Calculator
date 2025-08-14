package edu.hm.cs.emission_calculator.repo;

import edu.hm.cs.emission_calculator.model.FlightData;

import java.util.List;
import java.util.Map;

/**
 * Flight Dao.
 */
public interface InputDao {

    /**
     * Makes a new Input repository.
     * @return Input Repository
     */
    static InputRepository makeInputRepo(){
        return new InputRepository();
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

    /**
     * Creates temporary input repository.
     */
    void create();
    /**
     * removes all saved entries of given flight.
     *
     * @param data flight to delete
     * @return true if deleted, else false
     */
    boolean removeAll(FlightData data);


}
