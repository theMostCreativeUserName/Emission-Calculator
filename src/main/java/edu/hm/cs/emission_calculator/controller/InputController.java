package edu.hm.cs.emission_calculator.controller;

import edu.hm.cs.emission_calculator.clients.AbstractFactory;
import edu.hm.cs.emission_calculator.clients.Calculator;
import edu.hm.cs.emission_calculator.model.FlightData;
import edu.hm.cs.emission_calculator.repo.AirportDao;
import edu.hm.cs.emission_calculator.repo.FlightDao;
import edu.hm.cs.emission_calculator.repo.InputDao;
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
import java.util.List;
import java.util.Map;

/**
 * Controller for temporary input data.
 */
@RestController
@RequestMapping(path = "/api/input")
@Api(tags = {"Temporal data: process data in a non-commital way"})
public class InputController {
    /**
     * Repository for flight data.
     */
    @Autowired
    private InputDao inputRepository;
    /**
     * Repository for all airport data.
     */
    @Autowired
    private AirportDao airportRepository;
    /**
     * Repository for flight data.
     */
    @Autowired
    private FlightDao flightRepository;

    /**
     * Creates a new temporary database.
     *
     * @return JSON String confirming database was created.
     */
    @PostMapping("/create")
    @ApiOperation(notes = "Creates a new temporary database.", value="")
    public String createRepo() {
        inputRepository.create();
        final JSONObject obj = new JSONObject();
        obj.put("status", "created");
        return obj.toString();
    }

    /**
     * Merges all data from the temporary database into the flight repository.
     *
     * @return JSON String confirming data was merged.
     */
    @DeleteMapping("/merge")
    @ApiOperation(notes = "Merges all data from the temporary database into the flight repository. "
    	+ "The temporary database will be cleared after this process.", value="")
    public String mergeToFlight() {
        final List<FlightData> savedTempInput = inputRepository.findAll();
        final JSONObject obj = new JSONObject();
        if (savedTempInput.isEmpty()) {
            obj.put("status", "error: no data to merge");
        } else {
            savedTempInput.forEach(data -> flightRepository.add(data));
            inputRepository.clear();
            obj.put("status", "merged into flight repository");
        }
        return obj.toString();
    }

    /**
     * Creates view for data.
     * id is number of travelling people
     *
     * @return JSON String of formatted data
     */
    @GetMapping("/view")
    @ApiOperation(notes = "Generates an overview of all flights temporary stored.", value="")
    public String getView() {
        final Map<FlightData, Integer> map = inputRepository.mapView();
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
     * Calculates a flight and saves it into the database.
     *
     * @param input all necessary parameters for calculation
     *              date = date of the flight in format yyyy/dd/MM
     *              iataStart = iata of Start Airport
     *              iataDest = iata of destination airport
     *              class = travelClass for this flight (economy, business, first-class)
     *              authorityNumber = authority number
     *              travelling = number of people taking this flight
     *              other given data is dummy data, but can be replayed by parameters later.
     * @return String of data added
     */
    @PostMapping(path = "/data/add")
    @ApiOperation(notes = "Calculates a flight and saves it into the temporary database.", value="")
    public String add(
	    @ApiParam(name="input", value ="The as JSON formatted input String contains all relevant data about the flight: \n"
		    	+ "date of the flight, IATA codes of the start and destination airport airport, travel class for the flight,"
		    	+ " autority number and the amount of travellers.", type="String", required=true)
	    @RequestBody final String input) throws ParseException {
        final JSONObject inputObj = new JSONObject(input);
        final JSONArray flights = inputObj.getJSONArray("flight");

        flights.forEach(singleFLight -> {
            final JSONObject flightData = (JSONObject) singleFLight;
            final int travelling = flightData.getInt("travelling");

            for (int numberAdded = 0; numberAdded < travelling; numberAdded++) {
                final FlightData toAdd = formatFromJSONObject(flightData);
                inputRepository.add(toAdd);
            }
        });
        final JSONObject obj = new JSONObject();
        obj.put("status", "added");
        return obj.toString();
    }

    /**
     * Removes all entries with specified id.
     *
     * @param id id of data to remove
     * @return JSON confirming database was cleared.
     */
    @DeleteMapping("/data/{id}")
    @ApiOperation(notes = "Removes all stored entries signed with the specific id in the temporary database.", value="")
    public String remove(
	    @ApiParam(name="id", value ="The exact id of the flight to be removed", type="String", required=true)
	    @PathVariable final String id) {
        final JSONObject obj = new JSONObject();
        try {
            inputRepository.remove(Integer.parseInt(id));
            obj.put("status", "removed");
        } catch (NumberFormatException ex) {
            obj.put("status", "error: data didn't exist");
        }
        return obj.toString();
    }

    /**
     * Updates at least one entry.
     *
     * @param input JSONObject with changed entry in view-version
     * @return JSONObject confirming change
     * @throws ParseException
     */
    @PostMapping("/data")
    @ApiOperation(notes = "Updates at least one entry in the temporary database.", value="")
    public String update(
	    @ApiParam(name="input", value ="The as JSON formatted input String contains all entries to change.", type="String", required=false)
	    @RequestBody final String input) throws ParseException {
        final JSONObject inputObj = new JSONObject(input);
        final JSONArray flights = inputObj.getJSONArray("flight");
        flights.forEach(entry -> {
            final JSONObject object = (JSONObject) entry;
            final JSONArray update = object.getJSONArray("update");
            final JSONObject oldData = update.getJSONObject(0);
            final JSONObject newData = update.getJSONObject(1);
            final int travelling = newData.getInt("travelling");

            final FlightData oldFlight = formatFromJSONObject(oldData);
            final FlightData newFlight = formatFromJSONObject(newData);
            inputRepository.removeAll(oldFlight);
            for (int times = 0; times < travelling; times++) {
                inputRepository.add(newFlight);
            }

        });


        final JSONObject obj = new JSONObject();
        obj.put("status", "updated");
        return obj.toString();
    }

    private FlightData formatFromJSONObject(final JSONObject input) {
        final String date = input.getString("date");
        final String iataStart = input.getString("iataStart");
        final String iataDest = input.getString("iataDest");
        final String travelClass = input.getString("class");
        final int authNumber = input.getInt("authorityNumber");

        // format and calculate data
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final Calculator gcd = AbstractFactory.makeGCDCalc(airportRepository.getLat(iataStart),
              airportRepository.getLong(iataStart),
              airportRepository.getLat(iataDest), airportRepository.getLong(iataDest));
        final double distance = gcd.calculate();
        final Calculator emission = AbstractFactory.makeEmissionCalc(distance, travelClass);
        FlightData result = null;
        try {
            result = new FlightData(formatter.parse(date), authNumber
                  , "Authority", iataStart, airportRepository.getByIatacode(iataStart).getName()
                  , iataDest, airportRepository.getByIatacode(iataDest).getName()
                  , travelClass, distance, emission.calculate());
        } catch (ParseException exception) {
            System.out.println("date couldn't be parsed!");
        }
        return result;
    }
}
