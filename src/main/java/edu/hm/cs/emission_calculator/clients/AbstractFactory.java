package edu.hm.cs.emission_calculator.clients;

/**
 * Factory for client classes.
 */
public abstract class AbstractFactory {
    /**
     * Creates emission calculator.
     *
     * @param distance    distance to compensate
     * @param travelClass travel class of flight: business, economy, first-class
     * @return calculator
     */
    public static Calculator makeEmissionCalc(final double distance, final String travelClass) {
        return new EmissionCalculator(distance, travelClass);
    }

    /**
     * Creates GCD calculator.
     *
     * @param latStart  latitude of start point.
     * @param longStart longitude of start point.
     * @param latDest   latitude of destination point.
     * @param longDest  longitude of destination point.
     * @return new Emission Calculator
     */
    public static Calculator makeGCDCalc(final double latStart, final double longStart, final double latDest, final double longDest) {
        return new GcdCalculator(longStart, longDest, latStart, latDest);
    }
}
