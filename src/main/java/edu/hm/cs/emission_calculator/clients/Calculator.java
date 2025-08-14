package edu.hm.cs.emission_calculator.clients;

/**
 * Base for calculators.
 */
public interface Calculator {

    /**
     * Calculate specific value.
     *
     * @return solution
     */
    double calculate();

    /**
     * Returns name/type of the calculator.
     *
     * @return name
     */
    String getName();
}
