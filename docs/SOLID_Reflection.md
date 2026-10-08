# SOLID Principles Applied in RideWise

## S - Single Responsibility Principle
Each class handles only one aspect. E.g., `RiderService` only manages riders, `DriverService` only manages drivers, and strategies only handle one algorithm.

## O - Open/Closed Principle
`RideService` is open for extension but closed for modification. We can add a new pricing algorithm (e.g., `SurgeFareStrategy`) without touching `RideService` code by passing it through the interface.

## L - Liskov Substitution Principle
`NearestDriverStrategy` and `LeastActiveDriverStrategy` can interchangeably be substituted in `RideService` via the `RideMatchingStrategy` interface. The system will continue to behave correctly.

## I - Interface Segregation Principle
We have separate small interfaces: `RideMatchingStrategy` and `FareStrategy`. This prevents classes from being forced to depend on methods they don't use.

## D - Dependency Inversion Principle
`RideService` depends on `RideMatchingStrategy` and `FareStrategy` (abstractions), rather than concrete implementations like `DefaultFareStrategy`.
