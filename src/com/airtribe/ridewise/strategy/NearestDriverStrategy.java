package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import java.util.List;
import java.util.Optional;

public class NearestDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        // Simplification: Try to find a driver in the same location first
        Optional<Driver> driverOpt = drivers.stream()
                .filter(Driver::isAvailable)
                .filter(d -> d.getCurrentLocation().equalsIgnoreCase(rider.getLocation()))
                .findFirst();

        // If none in the exact location, return any available driver
        return driverOpt.orElseGet(() -> drivers.stream()
                .filter(Driver::isAvailable)
                .findFirst()
                .orElse(null));
    }
}
