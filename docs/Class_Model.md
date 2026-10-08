# RideWise Class Model

## Entities (`com.airtribe.ridewise.model`)
- **Rider:** Attributes `id`, `name`, `location`.
- **Driver:** Attributes `id`, `name`, `currentLocation`, `available`.
- **Ride:** Attributes `id`, `rider`, `driver`, `distance`, `status`.
- **FareReceipt:** Attributes `rideId`, `amount`, `generatedAt`.
- **Enums:** `RideStatus` (`REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`), `VehicleType` (`BIKE`, `AUTO`, `CAR`).

## Strategies (`com.airtribe.ridewise.strategy`)
- **RideMatchingStrategy (interface):** `findDriver(Rider rider, List<Driver> drivers)`
  - *NearestDriverStrategy*
  - *LeastActiveDriverStrategy*
- **FareStrategy (interface):** `calculateFare(Ride ride)`
  - *DefaultFareStrategy*
  - *PeakHourFareStrategy*

## Services (`com.airtribe.ridewise.service`)
- **RiderService:** Manages rider registration and lookup.
- **DriverService:** Manages driver registration and availability.
- **RideService:** Manages requesting, assigning, and completing rides. Uses strategies.
