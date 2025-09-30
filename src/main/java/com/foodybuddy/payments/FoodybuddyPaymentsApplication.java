package com.foodybuddy.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FoodybuddyPaymentsApplication {

    private static final Logger logger = LoggerFactory.getLogger(FoodybuddyPaymentsApplication.class);

    public static void main(String[] args) {
        logger.info("Starting FoodyBuddy Payments Application...");
        
        try {
            ConfigurableApplicationContext context = SpringApplication.run(FoodybuddyPaymentsApplication.class, args);
            logger.info("FoodyBuddy Payments Application started successfully on port: {}", 
                context.getEnvironment().getProperty("server.port", "8082"));
        } catch (Exception e) {
            logger.error("Failed to start FoodyBuddy Payments Application", e);
            System.exit(1);
        }
    }

}
