package edu.hm.cs.emission_calculator.controller;

import edu.hm.cs.emission_calculator.repo.AirportDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for airports (iatacode & coordinates mapping).
 */
@RestController
@RequestMapping("/airport")
@Api(tags = {"Airports: IATA code and coordinates mapping"})
public class AirportController {

    /**
     * Repository holding all airport data.
     */
    @Autowired
    private AirportDao airportRepository;


    /**
     * Returns all airports, that are registered with an iataCode.
     *
     * @return List of AirportCodes
     */
    @GetMapping("/iata")
    @ApiOperation(notes = "Returns only airports registered with an IATA code.\n"
    	+ "Note: The Try-it-out feature in the Swagger-Ui will take a long time for this request, be careful.", value="")
    public String getAllIata() {
        final JSONObject obj = new JSONObject();
        return obj.put("date",airportRepository.getMembers()).toString();
    }

    /**
     * Returns all airports, with all saved data.
     *
     * @return List of AirportCodes
     */
    @GetMapping("/all")
    @ApiOperation(notes="Returns all airports, with all saved data.\n"
    	+ "Note: The Try-it-out feature in the Swagger-Ui will take a long time for this request, be careful.", value="")
    public String getAllMembers() {
        final JSONObject obj = new JSONObject();
        return obj.put("date",airportRepository.findAll()).toString();
    }

}
