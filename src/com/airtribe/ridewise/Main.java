package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        
        // For simplicity, injecting Default strategies in the main application
        RideService rideService = new RideService(
                driverService, 
                new NearestDriverStrategy(), 
                new DefaultFareStrategy()
        );

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("Welcome to RideWise — Modular Ride-Sharing System");

        while (!exit) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add Rider");
            System.out.println("2. Add Driver");
            System.out.println("3. View Available Drivers");
            System.out.println("4. Request Ride");
            System.out.println("5. Complete Ride");
            System.out.println("6. View Rides");
            System.out.println("7. Exit");
            System.out.print("Select an option: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Rider Name: ");
                        String riderName = scanner.nextLine();
                        System.out.print("Enter Location: ");
                        String riderLocation = scanner.nextLine();
                        Rider rider = riderService.registerRider(riderName, riderLocation);
                        System.out.println("Rider added successfully with ID: " + rider.getId());
                        break;
                    case 2:
                        System.out.print("Enter Driver Name: ");
                        String driverName = scanner.nextLine();
                        System.out.print("Enter Current Location: ");
                        String driverLocation = scanner.nextLine();
                        Driver driver = driverService.registerDriver(driverName, driverLocation);
                        System.out.println("Driver added successfully with ID: " + driver.getId());
                        break;
                    case 3:
                        List<Driver> availableDrivers = driverService.getAvailableDrivers();
                        System.out.println("Available Drivers:");
                        if (availableDrivers.isEmpty()) {
                            System.out.println("No drivers currently available.");
                        } else {
                            for (Driver d : availableDrivers) {
                                System.out.println("- ID: " + d.getId() + " | Name: " + d.getName() + " | Location: " + d.getCurrentLocation());
                            }
                        }
                        break;
                    case 4:
                        System.out.print("Enter Rider ID: ");
                        String rId = scanner.nextLine();
                        Rider r = riderService.getRider(rId);
                        if (r == null) {
                            System.out.println("Rider not found.");
                            break;
                        }
                        System.out.print("Enter Distance for Ride (km): ");
                        double distance = Double.parseDouble(scanner.nextLine());
                        Ride ride = rideService.requestRide(r, distance);
                        System.out.println("Ride assigned successfully!");
                        System.out.println("Ride ID: " + ride.getId());
                        System.out.println("Assigned Driver: " + ride.getDriver().getName());
                        break;
                    case 5:
                        System.out.print("Enter Ride ID to complete: ");
                        String rideId = scanner.nextLine();
                        FareReceipt receipt = rideService.completeRide(rideId);
                        System.out.println("Ride completed successfully.");
                        System.out.println("--- Fare Receipt ---");
                        System.out.println("Ride ID: " + receipt.getRideId());
                        System.out.println("Amount: $" + receipt.getAmount());
                        System.out.println("Time: " + receipt.getGeneratedAt());
                        break;
                    case 6:
                        Map<String, Ride> allRides = rideService.getAllRides();
                        System.out.println("All Rides:");
                        if (allRides.isEmpty()) {
                            System.out.println("No rides found.");
                        } else {
                            for (Ride currRide : allRides.values()) {
                                System.out.println("- Ride ID: " + currRide.getId() + " | Rider: " + currRide.getRider().getName() + " | Driver: " + currRide.getDriver().getName() + " | Status: " + currRide.getStatus());
                            }
                        }
                        break;
                    case 7:
                        exit = true;
                        System.out.println("Exiting RideWise. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NoDriverAvailableException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
