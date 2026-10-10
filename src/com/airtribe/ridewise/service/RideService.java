package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.HashMap;
import java.util.Map;

public class RideService {
    private final DriverService driverService;
    private final RideMatchingStrategy rideMatchingStrategy;
    private final FareStrategy fareStrategy;
    private final Map<String, Ride> rides = new HashMap<>();

    public RideService(DriverService driverService, 
                       RideMatchingStrategy rideMatchingStrategy, 
                       FareStrategy fareStrategy) {
        this.driverService = driverService;
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(Rider rider, double distance) {
        Driver driver = rideMatchingStrategy.findDriver(rider, driverService.getAvailableDrivers());
        if (driver == null) {
            throw new NoDriverAvailableException("No available drivers to accept the ride.");
        }
        
        String rideId = IdGenerator.generateId();
        Ride ride = new Ride(rideId, rider, distance);
        ride.setDriver(driver);
        ride.setStatus(RideStatus.ASSIGNED);
        
        // Mark driver as unavailable
        driverService.updateAvailability(driver.getId(), false);
        
        rides.put(rideId, ride);
        return ride;
    }
    
    public FareReceipt completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null || ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalArgumentException("Invalid ride or ride not in ASSIGNED state.");
        }
        
        ride.setStatus(RideStatus.COMPLETED);
        
        // Free up the driver
        driverService.updateAvailability(ride.getDriver().getId(), true);
        
        double amount = fareStrategy.calculateFare(ride);
        return new FareReceipt(ride.getId(), amount);
    }
    
    public Map<String, Ride> getAllRides() {
        return rides;
    }
}
