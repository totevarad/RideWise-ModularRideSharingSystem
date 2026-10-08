package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class PeakHourFareStrategy implements FareStrategy {
    private static final double PEAK_RATE_PER_KM = 15.0;
    
    @Override
    public double calculateFare(Ride ride) {
        return ride.getDistance() * PEAK_RATE_PER_KM;
    }
}
