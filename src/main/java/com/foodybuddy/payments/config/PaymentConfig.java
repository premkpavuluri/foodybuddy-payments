package com.foodybuddy.payments.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for payment processing settings
 * Maps properties from application.yml to Java objects
 */
@Configuration
@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {
    
    private Processing processing = new Processing();
    private Simulation simulation = new Simulation();
    
    public Processing getProcessing() {
        return processing;
    }
    
    public void setProcessing(Processing processing) {
        this.processing = processing;
    }
    
    public Simulation getSimulation() {
        return simulation;
    }
    
    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }
    
    public static class Processing {
        private long timeout = 30000;
        private int retryAttempts = 3;
        private double successRate = 0.9;
        
        public long getTimeout() {
            return timeout;
        }
        
        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }
        
        public int getRetryAttempts() {
            return retryAttempts;
        }
        
        public void setRetryAttempts(int retryAttempts) {
            this.retryAttempts = retryAttempts;
        }
        
        public double getSuccessRate() {
            return successRate;
        }
        
        public void setSuccessRate(double successRate) {
            this.successRate = successRate;
        }
    }
    
    public static class Simulation {
        private boolean enabled = true;
        private long processingDelay = 2000;
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public long getProcessingDelay() {
            return processingDelay;
        }
        
        public void setProcessingDelay(long processingDelay) {
            this.processingDelay = processingDelay;
        }
    }
}
