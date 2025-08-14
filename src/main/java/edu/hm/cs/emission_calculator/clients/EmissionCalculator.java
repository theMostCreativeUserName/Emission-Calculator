package edu.hm.cs.emission_calculator.clients;


/**
 * This class calculates Emissions of a flight.
 * ----------------------
 * To include non-CO2 Emission these emissions are multiplied by 3.
 * https://www.atmosfair.de/wp-content/uploads/flug-emissionsrechner-dokumentation-berechnungsmethode-1.pdf
 * page 18
 * ----------------------
 * MyClimate-source:
 * https://www.myclimate.org/fileadmin/user_upload/myclimate_-_home/01_Information/01_About_myclimate/09_Calculation_principles/Documents/myclimate-Flugrechner-Grundlagen_DE.pdf
 */
public class EmissionCalculator implements Calculator {

    /**
     * Threshold for long-range flights in emission calculation.
     */
    private static final int CALCULATING_THRESHOLD = 1500;

    /**
     * Emission factor of calculation.
     * 3.16: Co2-emission
     * 3   : Non-Co2-emission
     * 0.54: emission factor for readying planes
     */
    private static final double EMISSION_FACTOR = 3.16 * 3 + 0.54;

    /**
     * Default factor considering passenger weight on flight.
     */
    private static final double PAYLOAD_FACTOR = 0.82;
    /**
     * Default factor considering standard emission of airplane itself.
     */
    private static final double AIRPLANE_FACTOR = 0.000_38;

    /**
     * Default factor considering upholding of airport infrastructure.
     */
    private static final double INFRASTRUCTURE_FACTOR = 11.68;

    /**
     * Distance to compensate.
     */
    private final double distance;
    /**
     * travel class of flight: economy, business, first-class.
     */
    private final String travelClass;

    /**
     * Standard C-Tor.
     *
     * @param distance    Great-Circle-Distance including corrective factor.
     * @param travelClass travelclass (business, economy, first-class)
     */
    public EmissionCalculator(final double distance, final String travelClass) {
        this.distance = distance;
        this.travelClass = travelClass;
    }

    @Override
    public double calculate() {
        return distance > CALCULATING_THRESHOLD ? longTravelCalc() : shortTravelCalc();
    }

    @Override
    public String getName() {
        return "emission";
    }

    private double getDistance() {
        return distance;
    }

    /**
     * MyClimate uses different default-values.
     * page: 6
     *
     * @return CW corrected for short travel distances
     */
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    //this is allowed, but annotation is created due to limitations of xpath.
    private double travelClassCorrectionShort() {
        final double economyFactor = 0.96;
        final double businessFactor = 1.26;
        final double firstClassFactor = 2.40;

        final double result;
        switch (travelClass) {
            case "economy": {
                result = economyFactor;
                break;
            }
            case "business": {
                result = businessFactor;
                break;
            }
            case "first-class": {
                result = firstClassFactor;
                break;
            }
            default:
                result = businessFactor;
        }
        return result;
    }

    /**
     * MyClimate uses different default-values.
     * page: 6
     *
     * @return CW corrected for long travel distances
     */
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    //this is allowed, but annotation is created due to limitations of xpath.
    private double travelClassCorrectionLong() {
        final double economyFactor = 0.80;
        final double businessFactor = 1.54;
        final double firstClassFactor = 2.40;

        final double result;
        switch (travelClass) {
            case "economy": {
                result = economyFactor;
                break;
            }
            case "business": {
                result = businessFactor;
                break;
            }
            case "first-class": {
                result = firstClassFactor;
                break;
            }
            default:
                result = -1;
        }
        return result;
    }

    /**
     * MyClimate uses different default-values.
     * page: 6
     *
     * @return emissions for short travel distances
     */
    private double shortTravelCalc() {
        final double standardSeatNumber = 153.51;
        final double standardCargoFactor = 0.93;
        final double distance = getDistance();

        // emission formula
        final double result = (2.714 * distance + 1166.52) / (standardSeatNumber * PAYLOAD_FACTOR)
              * (1 - standardCargoFactor) * travelClassCorrectionShort()
              * EMISSION_FACTOR + AIRPLANE_FACTOR * distance + INFRASTRUCTURE_FACTOR;
        return result / 100;
    }

    /**
     * MyClimate uses different default-values.
     * page: 6
     *
     * @return emissions for long travel distances
     */
    private double longTravelCalc() {
        final double standardSeatNumber = 280.21;
        final double standardCargoFactor = 0.74;
        final double distance = getDistance();

        // emission formula
        final double result = (0.000_01 * distance * distance + 7.104 * distance + 5_044.93) / (standardSeatNumber * PAYLOAD_FACTOR)
              * (1 - standardCargoFactor) * travelClassCorrectionLong()
              * EMISSION_FACTOR + AIRPLANE_FACTOR * distance + INFRASTRUCTURE_FACTOR;
        return result / 100;
    }

}
