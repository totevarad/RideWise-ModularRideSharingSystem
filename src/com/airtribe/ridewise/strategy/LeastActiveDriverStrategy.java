package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        // Simplification: Pick the last available driver in the list to simulate least active.
        // In a real application, we would check the driver's ride count or hours online.
        return drivers.stream()
                .filter(Driver::isAvailable)
                .reduce((first, second) -> second)
                .orElse(null);
    }
}
