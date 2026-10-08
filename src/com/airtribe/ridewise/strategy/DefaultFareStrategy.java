package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {
    private static final double RATE_PER_KM = 10.0;
    
    @Override
    public double calculateFare(Ride ride) {
        return ride.getDistance() * RATE_PER_KM;
    }
}
