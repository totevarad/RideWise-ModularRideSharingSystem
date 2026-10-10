# RideWise Requirements

## Functional Requirements
1. **Register Riders:** Ability to register riders by name and location.
2. **Register Drivers:** Ability to register drivers by name and location. Available by default.
3. **Show Available Drivers:** View all drivers currently not in a ride.
4. **Request Ride:** A rider can request a ride providing distance. A strategy handles matching.
5. **Calculate Fare:** A strategy handles the pricing logic.
6. **Track Ride Status:** Supports statuses: `REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`.

## Non-Functional Requirements
- Modularity & easily extendable pricing and driver matching logics (Open/Closed Principle).
- Low coupling / High cohesion using Service Layers.
- Maintainability by keeping classes single-purpose.
