package edu.hm.cs.emission_calculator.controller;

import edu.hm.cs.emission_calculator.model.FlightData;
import edu.hm.cs.emission_calculator.repo.FlightDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for file-functions.
 */
@RestController
@RequestMapping(path = "api/file")
@Api(tags = {"File-functions like \"download as CSV\""})
public class FileController {

    /**
     * Repository holding all airport data.
     */
    @Autowired
    private FlightDao flightRepository;

    /**
     * Parses flight-data list to csv file.
     * @param searchParams
     * @return downloadable csv file
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(path = "/download", method = RequestMethod.GET, produces = "application/hal+json;charset=utf8")
    @ApiOperation(notes = "Exports an optionally filtered copy of the current dataset as a CSV file.", value="")
    public ResponseEntity<byte[]> getParsedFile(
	    @ApiParam(name="searchParams", value ="The flights contained in the file to be generated can optionally be filtered by an "
	    	+ "authority number, startdate or enddate. \nFor example, when filtering by start date, the input "
	    	+ "parameter contains the tuple ((\"start\")(\"2021-06-11\")).", type="Map<String, String>", required=false) 
	    @RequestParam final Map<String, String> searchParams) throws IOException, ParseException {
	final Optional<String> authorityNum = Optional.ofNullable(searchParams.get("authority"));
        final Optional<String> start = Optional.ofNullable(searchParams.get("start"));
        final Optional<String> end = Optional.ofNullable(searchParams.get("end"));
        List<FlightData> resultData = flightRepository.findAll();

        if (authorityNum.isPresent()) {
            resultData = AbstractFileFunction.authorityFilter(Integer.parseInt(authorityNum.get()), resultData);
        }
        if (start.isPresent()) {
            if (end.isPresent()) {
                resultData = AbstractFileFunction.dateFilter(start.get(), end.get(), resultData);
            } else {
                resultData = AbstractFileFunction.dateFilter(start.get(), resultData);
            }
        }

        final File file = AbstractFileFunction.parseFlightData(resultData);
        return ResponseEntity.ok()
              .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
              .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + file.getName())
              .body(Files.readAllBytes(Path.of(file.getPath())));
    }


}
