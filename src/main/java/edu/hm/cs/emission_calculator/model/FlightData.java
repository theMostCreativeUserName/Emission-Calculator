package edu.hm.cs.emission_calculator.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity class for flight data.
 */
@Data
@Entity
@Table(name = "flight_data")
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class FlightData implements Serializable {
    /**
     * Id of this entry.
     */
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * Date this flight was taken at.
     */
    @Column(name = "startingdate")
    private Date startingDate;
    /**
     * Authority number of travelling division.
     */
    @Column(name = "authoritynumber")
    private int authorityNumber;
    /**
     * Authority name of travelling division.
     */
    @Column(name = "authorityname")
    private String authorityName;
    /**
     * Iata code of start airport.
     */
    @Column(name = "iatastart")
    private String iataStart;
    /**
     * Name of start airport.
     */
    @Column(name = "startname")
    private String startName;
    /**
     * Iata code of destination airport.
     */
    @Column(name = "iatadest")
    private String iataDest;
    /**
     * Name of destination airport.
     */
    @Column(name = "destname")
    private String destName;
    /**
     * Travel class for this flight (business, economy, first-class).
     */
    @Column(name = "travelclass")
    private String travelClass;
    /**
     * Great-Circle_distance of both airports including corrective factor in km.
     */
    @Column(name = "distance")
    private double distance;
    /**
     * Calculated emission in tonnes per person.
     */
    @Column(name = "emission")
    private double emission;
    /**
     * Number of travelling people.
     */
    private int number;


    /**
     * Standard C-Tor.
     * @param startingDate date the flight was taken
     * @param authorityNumber identifikation number of the authority
     * @param authorityName name of the authority
     * @param iataStart IATA-code of start airport
     * @param startName name of start airport
     * @param iataDest IATA-code of destination airport
     * @param destName name of destination airport
     * @param travelClass travelClass for flight (business, economy, first-class)
     * @param distance Great-Circle-Distance of both airports including correction-km
     * @param emission Calculated emission
     */
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public FlightData(final Date startingDate, final int authorityNumber, final String authorityName
          , final String iataStart, final String startName, final String iataDest, final String destName
          , final String travelClass, final double distance, final double emission) {

        this.startingDate = startingDate;
        this.authorityNumber = authorityNumber;
        this.authorityName = authorityName;
        this.iataStart = iataStart;
        this.startName = startName;
        this.iataDest = iataDest;
        this.destName = destName;
        this.travelClass = travelClass;
        this.distance = distance;
        this.emission = emission;
    }

    /**
     * Empty C-Tor to create an empty entry.
     */
    public FlightData() {
    }

}
