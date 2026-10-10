package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.PeakHourFareStrategy;

import java.util.List;
import java.util.Arrays;

public class RideWiseTests {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("Running Unit and Integration Tests...\n");

        try {
            testRiderService();
            passed++;
        } catch (Throwable t) {
            System.err.println("testRiderService FAILED: " + t.getMessage());
            failed++;
        }

        try {
            testDriverService();
            passed++;
        } catch (Throwable t) {
            System.err.println("testDriverService FAILED: " + t.getMessage());
            failed++;
        }

        try {
            testRideMatchingStrategies();
            passed++;
        } catch (Throwable t) {
            System.err.println("testRideMatchingStrategies FAILED: " + t.getMessage());
            failed++;
        }

        try {
            testFareStrategies();
            passed++;
        } catch (Throwable t) {
            System.err.println("testFareStrategies FAILED: " + t.getMessage());
            failed++;
        }

        try {
            testRideServiceIntegration();
            passed++;
        } catch (Throwable t) {
            System.err.println("testRideServiceIntegration FAILED: " + t.getMessage());
            failed++;
        }

        System.out.println("\nTests Completed.");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void assertEqual(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void testRiderService() {
        RiderService riderService = new RiderService();
        Rider rider = riderService.registerRider("Alice", "LocationA");
        assertEqual("Alice", rider.getName(), "Rider name should match");
        assertEqual("LocationA", rider.getLocation(), "Rider location should match");
        assertTrue(rider.getId() != null, "Rider ID should not be null");

        Rider fetchedRider = riderService.getRider(rider.getId());
        assertEqual(rider, fetchedRider, "Fetched rider should match registered rider");
    }

    private static void testDriverService() {
        DriverService driverService = new DriverService();
        Driver driver1 = driverService.registerDriver("Bob", "LocationA");
        Driver driver2 = driverService.registerDriver("Charlie", "LocationB");
        
        assertEqual("Bob", driver1.getName(), "Driver 1 name match");
        assertTrue(driver1.isAvailable(), "Driver should be available by default");

        List<Driver> available = driverService.getAvailableDrivers();
        assertEqual(2, available.size(), "Should be 2 available drivers");

        driverService.updateAvailability(driver1.getId(), false);
        available = driverService.getAvailableDrivers();
        assertEqual(1, available.size(), "Should be 1 available driver after updating one to false");
        assertEqual("Charlie", available.get(0).getName(), "Available driver should be Charlie");
    }

    private static void testRideMatchingStrategies() {
        Driver driver1 = new Driver("1", "Bob", "LocationA");
        Driver driver2 = new Driver("2", "Charlie", "LocationB");
        Driver driver3 = new Driver("3", "Dave", "LocationC");
        
        Rider rider = new Rider("100", "Alice", "LocationB");
        
        List<Driver> availableDrivers = Arrays.asList(driver1, driver2, driver3);
        
        NearestDriverStrategy nearestDriverStrategy = new NearestDriverStrategy();
        Driver nearest = nearestDriverStrategy.findDriver(rider, availableDrivers);
        assertEqual("Charlie", nearest.getName(), "Nearest driver should be Charlie (LocationB)");
        
        LeastActiveDriverStrategy leastActiveDriverStrategy = new LeastActiveDriverStrategy();
        Driver leastActive = leastActiveDriverStrategy.findDriver(rider, availableDrivers);
        assertEqual("Dave", leastActive.getName(), "Least active driver should be Dave (last in list)");
    }

    private static void testFareStrategies() {
        Ride ride = new Ride("r1", new Rider("100", "Alice", "LocationA"), 10.5);
        
        DefaultFareStrategy defaultFare = new DefaultFareStrategy();
        assertEqual(105.0, defaultFare.calculateFare(ride), "Default fare should be 10.5 * 10");

        PeakHourFareStrategy peakFare = new PeakHourFareStrategy();
        assertEqual(157.5, peakFare.calculateFare(ride), "Peak fare should be 10.5 * 15");
    }

    private static void testRideServiceIntegration() {
        DriverService driverService = new DriverService();
        Driver driver1 = driverService.registerDriver("Bob", "LocationB"); // Not rider location
        Driver driver2 = driverService.registerDriver("Charlie", "LocationA"); // Matches rider location

        RiderService riderService = new RiderService();
        Rider rider = riderService.registerRider("Alice", "LocationA");

        RideService rideService = new RideService(driverService, new NearestDriverStrategy(), new DefaultFareStrategy());
        
        // Request ride
        Ride ride = rideService.requestRide(rider, 5.0); // 5km
        assertEqual(RideStatus.ASSIGNED, ride.getStatus(), "Ride should be assigned");
        assertEqual("Charlie", ride.getDriver().getName(), "Charlie should be assigned because of NearestDriverStrategy");
        
        // Verify driver is now unavailable
        assertTrue(!driver2.isAvailable(), "Charlie should not be available after ride assigned");
        
        // Complete ride
        FareReceipt receipt = rideService.completeRide(ride.getId());
        assertEqual(50.0, receipt.getAmount(), "Fare should be 50.0 for 5km default fare");
        assertEqual(RideStatus.COMPLETED, ride.getStatus(), "Ride should be COMPLETED");
        
        // Verify driver is available again
        assertTrue(driver2.isAvailable(), "Charlie should be available again after ride completion");
        
        // Try requesting ride when no drivers available
        driverService.updateAvailability(driver1.getId(), false);
        driverService.updateAvailability(driver2.getId(), false);
        
        boolean exceptionThrown = false;
        try {
            rideService.requestRide(rider, 10.0);
        } catch (NoDriverAvailableException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown, "NoDriverAvailableException should be thrown when no drivers are available");
    }
}
