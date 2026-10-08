# Object Relationships

- **Rider -> Ride:** `Association`. A ride has a reference to a rider, but a rider and ride can conceptually exist independently.
- **Driver -> Ride:** `Association`. A ride assigns a driver.
- **Ride -> FareReceipt:** `Composition`. A `FareReceipt` is heavily dependent on the existence of a completed `Ride`.
- **RideService -> Strategies:** `Composition/Aggregation`. The `RideService` leverages the strategies to perform core ride calculations and matching.
