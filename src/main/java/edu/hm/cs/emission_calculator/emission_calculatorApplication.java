package edu.hm.cs.emission_calculator;

import edu.hm.cs.emission_calculator.repo.AirportDao;
import edu.hm.cs.emission_calculator.repo.FlightDao;
import edu.hm.cs.emission_calculator.repo.InputDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Application.
 */
@SpringBootApplication
public class emission_calculatorApplication {

    /**
     * Function starting the application.
     * @param args args
     */
    public static void main(final String[] args) {
        SpringApplication.run(emission_calculatorApplication.class, args);
    }

    /**
     * Airport repository; maps Iata-codes to all necessary airport information.
     *
     * @return AirportRepository
     */
    @Bean
    public AirportDao getAirportRepo() {
        return AirportDao.make();
    }

    /**
     * Flight repository.
     *
     * @return FlightRepository
     */
    @Bean
    public FlightDao getFlightRepository() {
        return FlightDao.makeFlightRepo();
    }

    /**
     * Input repository.
     * @return InputRepository
     */
    @Bean
    public InputDao getInputRepository(){return InputDao.makeInputRepo();}
}
