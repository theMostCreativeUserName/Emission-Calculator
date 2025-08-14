package edu.hm.cs.emission_calculator.controller;

import edu.hm.cs.emission_calculator.clients.Calculator;
import edu.hm.cs.emission_calculator.clients.AbstractFactory;
import edu.hm.cs.emission_calculator.repo.AirportDao;
import edu.hm.cs.emission_calculator.model.FlightData;
import edu.hm.cs.emission_calculator.repo.FlightDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for all airport flight data.
 */
@RestController
@RequestMapping(path = "/api")
@Api(tags = {"Flights: process all flight data and also calculate the emissions"})
public class FlightController {

    /**
     * Repository for flight data.
     */
    @Autowired
    private FlightDao flightRepository;
    /**
     * Repository for all airport data.
     */
    @Autowired
    private AirportDao airportRepository;


    /**
     * Returns all saved flights.
     *
     * @return List of FlightData
     */
    @GetMapping("/all")
    @ApiOperation(notes = "Returns all flights saved in the database.", value="")
    public String getAllMembers() {
        final JSONObject resultObj = new JSONObject();
        final JSONArray flights = new JSONArray();
        flightRepository.findAll().stream()
              .forEach( single ->{
                  final JSONObject singleFlight = new JSONObject();
                  final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                  singleFlight.put("id", single.getId());
                  singleFlight.put("destName", single.getDestName());
                  singleFlight.put("emission", single.getEmission());
                  singleFlight.put("authorityName", single.getAuthorityName());
                  singleFlight.put("distance", single.getDistance());
                  singleFlight.put("iataDest", single.getIataDest());
                  singleFlight.put("iataStart", single.getIataStart());
                  singleFlight.put("startingDate", formatter.format(single.getStartingDate()));
                  singleFlight.put("authorityNumber", single.getAuthorityNumber());
                  singleFlight.put("startName", single.getStartName());
                  singleFlight.put("travelClass", single.getTravelClass());
                  flights.put(singleFlight);
                    }
              );
        resultObj.put("data", flights);
        return resultObj.toString();
    }

    /**
     * Calculates a flight and saves it into the database.
     *
     * @param input all necessary parameters for calculation
     *              date = date of the flight in format yyyy/MM/dd
     *              iataStart = IATA of Start Airport
     *              iataDest = IATA of destination airport
     *              class = travelClass for this flight (economy, business, first-class)
     *              authorityNumber = authority number
     *              travelling = number of people taking this flight
     *              other given data is dummy data, but can be replayed by parameters later.
     * @return String of data added
     */
    @PostMapping(path = "/data/add")
    @ApiOperation(notes = "Calculates a flight and saves it into the database.", value="")
    public String add(
	    @ApiParam(name="input", value ="The as JSON formatted input String contains all relevant data about the flight: \n"
	    	+ "date of the flight, IATA codes of the start and destination airport airport, travel class for the flight,"
	    	+ " autority number and the amount of travellers.", type="String", required=true)
	    @RequestBody final String input) throws ParseException {
        final JSONObject inputObj = new JSONObject(input);
        final JSONArray flights = inputObj.getJSONArray("flight");

        flights.forEach(singleFLight -> {
            final JSONObject flightData = (JSONObject) singleFLight;
            final String date = flightData.getString("date");
            final String iataStart = flightData.getString("iataStart");
            final String iataDest = flightData.getString("iataDest");
            final String travelClass = flightData.getString("class");
            final int authNumber = flightData.getInt("authorityNumber");
            final int number = flightData.getInt("travelling");

            // format and calculate data
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final Calculator gcd = AbstractFactory.makeGCDCalc(airportRepository.getLat(iataStart), airportRepository.getLong(iataStart),
                  airportRepository.getLat(iataDest), airportRepository.getLong(iataDest));
            final double distance = gcd.calculate();
            final Calculator emission = AbstractFactory.makeEmissionCalc(distance, travelClass);

            for (int numberAdded = 0; numberAdded < number; numberAdded++) {
                final FlightData toAdd;
                try {
                    toAdd = new FlightData(formatter.parse(date), authNumber
                          , "Authority", iataStart, airportRepository.getByIatacode(iataStart).getName()
                          , iataDest, airportRepository.getByIatacode(iataDest).getName()
                          , travelClass, distance, emission.calculate());
                    flightRepository.add(toAdd);
                } catch (ParseException exception) {
                    System.out.println("date couldn't be parsed!");
                }
            }
        });
        final JSONObject obj = new JSONObject();
        obj.put("status", "added");
        return obj.toString();
    }

    /**
     * Clears entire database.
     *
     * @return JSON confirming database was cleared.
     */
    @DeleteMapping("/clear")
    @ApiOperation(notes = "Clears the entire database.", value="")
    public String clear() {
        flightRepository.clear();
        final JSONObject obj = new JSONObject();
        return obj.put("status", "cleared").toString();
    }

    /**
     * Removes one entry via id.
     *
     * @param id id of data to remove
     * @return JSON confirming database was cleared.
     */
    @DeleteMapping("/data/{id}")
    @ApiOperation(notes = "Removes all stored entries signed with the specific id.", value="")
    public String remove(
	    @ApiParam(name="id", value ="The exact id of the flight to be removed", type="String", required=true)
	    @PathVariable final String id) {
        final JSONObject obj = new JSONObject();
        try {
            flightRepository.remove(Integer.parseInt(id));
            obj.put("status", "removed");
        } catch (NumberFormatException ex) {
            obj.put("status", "error: data didn't exist");
        }
        return obj.toString();
    }

    /**
     * Searches for a data entry by IATA code or id.
     *
     * @param searchParams parameters either
     *                     id = id of entry
     *                     code = IATA code of entry (here eiter start of dest airport)
     *                     if both parameters are set, search focuses on id
     * @return Json of found data.
     */
    @GetMapping("/search")
    @ApiOperation(notes = "Searches for a data entry by IATA code or id and returns the JSON of the found data.", value="")
    public String getBy(
	    @ApiParam(name="searchParams", value ="Must contain either the exact id of a flight or the IATA code"
	    	+ " of start or destination airport.", type="Map<String, String>", required=true)
	    @RequestParam final Map<String, String> searchParams) {
        final Optional<String> id = Optional.ofNullable(searchParams.get("id"));
        final Optional<String> code = Optional.ofNullable(searchParams.get("code"));
        final JSONObject obj = new JSONObject();
        if (id.isPresent()) {
            obj.put("data", flightRepository.getById(Integer.parseInt(id.get())));
            return obj.toString();
        } else if (code.isPresent()) {
            obj.put("data", flightRepository.getByIataCode(code.get()));
            return obj.toString();
        } else {
            return obj.toString();
        }
    }

    /**
     * Creates dashboard data.
     * id is number of travelling people
     *
     * @return JSON String of formatted data
     */
    @GetMapping("/dashboard")
    @ApiOperation(notes = "Generates data about all stored flights for the dashboard. Returns a String formatted as JSON.", value="")
    public String getDash() {
        final Map<FlightData, Integer> map = flightRepository.mapView();
        map.forEach((data, integer) -> data.setNumber(integer));
        final Collection<FlightData> list = map.keySet();

        final JSONObject resultObj = new JSONObject();
        final JSONArray flights = new JSONArray();
        list.stream()
              .forEach( single ->{
                        final JSONObject singleFlight = new JSONObject();
                        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        singleFlight.put("id", single.getId());
                        singleFlight.put("number", single.getNumber());
                        singleFlight.put("destName", single.getDestName());
                        singleFlight.put("emission", single.getEmission());
                        singleFlight.put("distance", single.getDistance());
                        singleFlight.put("iataDest", single.getIataDest());
                        singleFlight.put("iataStart", single.getIataStart());
                        singleFlight.put("startingDate", formatter.format(single.getStartingDate()));
                        singleFlight.put("authorityNumber", single.getAuthorityNumber());
                        singleFlight.put("startName", single.getStartName());
                        singleFlight.put("travelClass", single.getTravelClass());
                        flights.put(singleFlight);
                    }
              );
        resultObj.put("data", flights);

        return resultObj.toString();
    }

    /**
     * Testing method for calculation.
     * Tests a flight from MUC to DUS in business class
     *
     *
     * @return String of added entry
     */
    @PostMapping(path = "/calctest")
    @ApiOperation(notes = "Testing method for correct calculation. Creates also an flight example entry in the database.", value="calctest")
    public String calc() {
        final String iataStart = "MUC";
        final String iataDest = "DUS";
        final String travelClass = "business";

        final Calculator gcd = AbstractFactory.makeGCDCalc(airportRepository.getLat(iataStart), airportRepository.getLong(iataStart),
              airportRepository.getLat(iataDest), airportRepository.getLong(iataDest));
        final double distance = gcd.calculate();
        final Calculator emission = AbstractFactory.makeEmissionCalc(distance, travelClass);

        final int dummyNumber = 1234;
        final FlightData data = new FlightData(new Date(System.currentTimeMillis()), dummyNumber
              , "Dummy-Abteilung", iataStart, airportRepository.getByIatacode(iataStart).getName()
              , iataDest, airportRepository.getByIatacode(iataDest).getName()
              , travelClass, distance, emission.calculate());
        flightRepository.add(data);
        final JSONObject obj = new JSONObject();
        return obj.put("status", "added").toString();
    }
}
