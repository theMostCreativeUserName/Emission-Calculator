package edu.hm.cs.emission_calculator.controller;

import com.opencsv.CSVWriter;
import edu.hm.cs.emission_calculator.model.FlightData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * exports Data.
 */
abstract class AbstractFileFunction {
    /**
     * Name of the export file.
     */
    private static final String NAME = "emission_calculator-export.csv";
    /**
     * Header for csv-Data.
     */
    private static final String[] HEADER = {"Datum", "IATA Startflughafen", "Name Startflughafen"
          , "IATA Ankunftsflughafen", "Name Ankunftsflughafen", "Flugklasse", "Dienststellennummer"
          , "geflogene Distanz (in km)", "Emissionen (in Tonnen)"};


    /**
     * Parses all currently saved data to an csv File.
     *
     * @param allData Data to be formatted to the csv file
     * @return File export.csv
     */
    static File parseFlightData(final List<FlightData> allData) {

        final File parsedFile = new File(NAME);
        try (CSVWriter writer = new CSVWriter(new FileWriter(parsedFile, StandardCharsets.ISO_8859_1))) {
            final List<String[]> parsedData = new ArrayList<>();
            parsedData.add(HEADER);
            allData.stream().map(flight -> new String[]{
                  flight.getStartingDate().toString()
                  , flight.getIataStart()
                  , flight.getStartName()
                  , flight.getIataDest()
                  , flight.getDestName()
                  , flight.getTravelClass()
                  , String.valueOf(flight.getAuthorityNumber())
                  , String.valueOf(flight.getDistance())
                  , String.valueOf(flight.getEmission())})
                  .forEach(parsedData::add);
            writer.writeAll(parsedData);
        } catch (IOException exception) {
            System.out.println("Export couldn't be created");
        }
        return parsedFile;
    }

    /**
     * Parses all currently saved data to an csv File, filtered for authority.
     *
     * @param allData         Data to be formatted to the csv file
     * @param authorityNumber number of authority to search for
     * @return File export.csv
     */
    static List<FlightData> authorityFilter(final int authorityNumber, final List<FlightData> allData) {
        return allData.stream().filter(entry -> entry.getAuthorityNumber() == authorityNumber).collect(Collectors.toList());
    }

    /**
     * Parses all currently saved data to an csv File, filtered for start and end date.
     *
     * @param allData Data to be formatted to the csv file
     * @param start   start date to search for
     * @param end     end date
     * @return File export.csv
     */
    static List<FlightData> dateFilter(final String start, final String end, final List<FlightData> allData) throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final Date startDate = formatter.parse(start);
        final Date endDate = formatter.parse(end);
        return allData.stream()
              .filter(entry -> entry.getStartingDate().after(startDate))
              .filter(entry -> entry.getStartingDate().before(endDate))
              .collect(Collectors.toList());
    }

    /**
     * Parses all currently saved data to an csv File.
     *
     * @param allData Data to be formatted to the csv file
     * @param start   start date to search for
     * @return File export.csv
     */
    static List<FlightData> dateFilter(final String start, final List<FlightData> allData) throws ParseException {
        // today's date
        final Date today = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return dateFilter(start, formatter.format(today), allData);
    }


}
