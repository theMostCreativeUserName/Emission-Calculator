package edu.hm.cs.emission_calculator.clients;

/**
 * Calculator to calculate GCD-distance of two places.
 */
public class GcdCalculator implements Calculator {
    /**
     * Distance correction for short flights.
     */
    private static final int SHORT_CORRECTION = 50;
    /**
     * Distance correction for mid-distance flights.
     */
    private static final int MID_CORRECTION = 100;
    /**
     * Distance correction for long flights.
     */
    private static final int LONG_CORRECTION = 125;
    /**
     * Radius of spherical earth in km.
     */
    private static final int EARTH_RADIUS = 6371;
    /**
     * Gcd mid-distance correction threshold.
     */
    private static final int MID_DISTANCE_THRESHOLD = 550;
    /**
     * Gcd long-distance correction threshold.
     */
    private static final int HIGH_DISTANCE_THRESHOLD = 5500;
    /**
     * longitude coordinates of start airport.
     */
    private final double longStart;
    /**
     * longitude coordinates of destination airport.
     */
    private final double longDest;
    /**
     * latitude coordinates of start airport.
     */
    private final double latStart;
    /**
     * latitude coordinates of destination airport.
     */
    private final double latDest;

    /**
     * Standard C-Tor.
     * @param longStart longitude of start airport
     * @param longDest longitude of destination airport
     * @param latStart latitude of start airport
     * @param latDest latitude of destination airport
     */
    public GcdCalculator(final double longStart, final double longDest, final double latStart, final double latDest) {
        this.longStart = longStart;
        this.longDest = longDest;
        this.latStart = latStart;
        this.latDest = latDest;
    }

    @Override
    public double calculate() {
        return gcdCorrection(calculateGCD());
    }

    @Override
    public String getName() {
        return "gcd";
    }

    /**
     * Calculate GCD-value.
     *
     * @return gcd
     */
    private double calculateGCD() {
        final Double latDelta = toRad(latDest - latStart);
        final Double longDelta = toRad(longDest - longStart);
        final Double aDouble = Math.sin(latDelta / 2) * Math.sin(latDelta / 2) +
              Math.cos(toRad(latStart)) * Math.cos(toRad(latDest)) *
                    Math.sin(longDelta / 2) * Math.sin(longDelta / 2);
        final Double calFactor = 2 * Math.atan2(Math.sqrt(aDouble), Math.sqrt(1 - aDouble));
        return EARTH_RADIUS * calFactor;
    }

    /**
     * ICAO foresees a general correction factor to equal stacking, traffic and weather-driven happenings.
     * https://www.icao.int/environmental-protection/CarbonOffset/Documents/Methodology%20ICAO%20Carbon%20Calculator_v11-2018.pdf
     * page 8
     *
     * @param distance gcd distance form airport to airport.
     * @return corrected distance
     */
    private double gcdCorrection(final double distance) {
        double correctedDistance = distance;
        if (distance < MID_DISTANCE_THRESHOLD) {
            correctedDistance += SHORT_CORRECTION;
        } else if (distance < HIGH_DISTANCE_THRESHOLD) {
            correctedDistance += MID_CORRECTION;
        } else {
            correctedDistance += LONG_CORRECTION;
        }
        return correctedDistance;
    }

    /**
     * Transform value from degree to radians.
     *
     * @param value value to transform
     * @return radiant value
     */
    private static Double toRad(final Double value) {
        final int conversion = 180;
        return value * Math.PI / conversion;
    }

    @Override
    public String toString() {
        return "GcdCalculator{" +
              "longStart=" + longStart +
              ", longDest=" + longDest +
              ", latStart=" + latStart +
              ", latDest=" + latDest +
              '}';
    }
}
