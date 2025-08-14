package edu.hm.cs.emission_calculator.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * represents one row of the airports.csv list as Class.
 */
@Data
@Entity
@Table(name = "airport_codes")
@SuppressWarnings({"PMD.MissingSerialVersionUID", "PMD.TooManyFields"})
public class AirportCode implements Serializable {
    /**
     * Id of entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Indent of entry.
     */
    @Column(name = "indent")
    private String indent;
    /**
     * Airport type.
     */
    @Column(name = "type")
    private String type;
    /**
     * Airport name.
     */
    @Column(name = "name")
    private String name;
    /**
     * Latitude coordinates of airport.
     */
    @Column(name = "latitudedeg")
    private double latitudedeg;
    /**
     * Longitude coordinates of airport.
     */
    @Column(name = "longitudedeg")
    private double longitudedeg;
    /**
     * Elevation of airport in feet.
     */
    @Column(name = "elevationft")
    private int elevationft;
    /**
     * Continent.
     */
    @Column(name = "continent")
    private String continent;
    /**
     * Iso code of airports country.
     */
    @Column(name = "isocountry")
    private String isocountry;
    /**
     * Iso code of airports region.
     */
    @Column(name = "isoregion")
    private String isoregion;
    /**
     * Name of administrative division.
     */
    @Column(name = "municipality")
    private String municipality;
    /**
     * Answer to: if flights can be schedueled.
     */
    @Column(name = "scheduledservice")
    private String scheduledservice;
    /**
     * Gps code of airport.
     */
    @Column(name = "gpscode")
    private String gpscode;
    /**
     * Iata code of airport.
     */
    @Column(name = "iatacode")
    private String iatacode;
    /**
     * Local codee for airport.
     */
    @Column(name = "localcode")
    private String localcode;
    /**
     * Link to homepage of this airport.
     */
    @Column(name = "homelink")
    private String homelink;
    /**
     * Link to the wikipedia article about this airport.
     */
    @Column(name = "wikilink")
    private String wikilink;
    /**
     * Keywords for airport.
     */
    @Column(name = "keywords")
    private String keywords;

    @Override
    public String toString() {
        return "AirportEntity{" +
              "name= '" + name + '\'' +
              ", latitude_deg= " + latitudedeg +
              ", longitude_deg= " + longitudedeg +
              ", iata_code= " + iatacode +
              '}';
    }
}
