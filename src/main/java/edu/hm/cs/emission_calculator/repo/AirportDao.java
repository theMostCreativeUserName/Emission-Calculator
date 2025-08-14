package edu.hm.cs.emission_calculator.repo;

import edu.hm.cs.emission_calculator.model.AirportCode;

import java.util.List;

/**
 * Airport DAO.
 */
public interface AirportDao {

    /**
     * Makes a new AirportRepository.
     * @return AirportRepository
     */
    static AirportRepository make (){
        return new AirportRepository();
    }
    /**
     * Search database for airport via id.
     *
     * @param id id of searched for airport.
     * @return airport or null, if airport couldn't be found.
     */
    AirportCode getById(int id);

    /**
     * Search database for airport via iata-code.
     *
     * @param iataCode iata-code of searched for airport.
     * @return airport or null, if airport couldn't be found.
     */
    AirportCode getByIatacode(String iataCode);

    /**
     * Returns all Entries of the database.
     *
     * @return List of all airports
     */
    List<AirportCode> findAll();

    /**
     * Get Latitude of an airport via iata-code.
     *
     * @param iataCode iata-code of airport
     * @return latitude of searched airport
     */
    double getLat(String iataCode);

    /**
     * Get Longitude of an airport via iata-code.
     *
     * @param iataCode iata-code of airport
     * @return latitude of searched airport
     */
    double getLong(String iataCode);

    /**
     * Returns all airports with valid id, latitude, longitude and iata-code.
     *
     * @return List of airports
     */
    List<AirportCode> getMembers();
}
