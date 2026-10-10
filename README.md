# RideWise — Modular Ride-Sharing System

RideWise is a robust, modular ride-sharing system designed as a console application. It focuses on implementing **Low-Level Design (LLD)** concepts, **Design Principles**, and **Object-Oriented Programming (OOP)** paradigms, with a special emphasis on **SOLID** principles. 

This project aims to demonstrate how to build clean, extensible, and maintainable systems using Java, by completely decoupling the application layers and relying on interfaces and design patterns (like Strategy and Dependency Injection).

---

## 🎯 Learning Objectives & Design Principles

### OOP & SOLID Principles
- **Single Responsibility Principle (SRP):** Distinct services such as `RideService`, `DriverService`, and `RiderService` each have single, focused responsibilities.
- **Open/Closed Principle (OCP):** New ride matching algorithms (e.g., `LeastActiveDriverStrategy`) and pricing strategies (e.g., `PeakHourFareStrategy`) can be added seamlessly without altering the core system logic.
- **Liskov Substitution Principle (LSP):** Different strategy implementations remain completely interchangeable and act as drop-in replacements.
- **Interface Segregation Principle (ISP):** Small, focused interfaces like `RideMatchingStrategy` and `FareStrategy`.
- **Dependency Inversion Principle (DIP):** Core services rely solely on abstractions/interfaces instead of concrete implementations.

### Other Core Design Principles
- **DRY (Don't Repeat Yourself):** Reusable allocation and validation logic.
- **KISS (Keep It Simple, Stupid):** Straightforward and simple entity relationships.
- **YAGNI (You Aren't Gonna Need It):** Focused strictly on the Minimum Viable Product (MVP) feature set.
- **Law of Demeter:** Minimal dependency chains; objects only talk to their immediate collaborators.

---

## ✨ Features

- **Rider Management:** Register and manage riders.
- **Driver Management:** Register drivers, update availability, and view currently active drivers.
- **Dynamic Ride Matching:** Allocate drivers to riders based on interchangeable strategies (e.g., Nearest Driver, Least Active Driver).
- **Flexible Pricing Engine:** Calculate trip fares dynamically using strategies (e.g., Default Pricing, Peak-Hour Pricing).
- **Ride Lifecycle Tracking:** Seamlessly move a ride from `REQUESTED` to `ASSIGNED`, and finally to `COMPLETED` or `CANCELLED`.

---

## 🏗 System Architecture & Domain Entities

### Core Packages
- `model/`: Contains all domain entities and Enums.
- `service/`: Contains core business logic.
- `strategy/`: Contains matching and pricing interfaces and their concrete implementations.

### Domain Models
- **`Rider`**: Id, Name, Location
- **`Driver`**: Id, Name, Current Location, Availability Status
- **`Ride`**: Id, Rider, Driver, Distance, Status
- **`FareReceipt`**: RideId, Amount, Timestamp
- **Enums**: `RideStatus` (`REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`), `VehicleType` (`BIKE`, `AUTO`, `CAR`)

### Strategy Design

#### 1. Ride Matching Strategy
```java
public interface RideMatchingStrategy {
    Driver findDriver(Rider rider, List<Driver> drivers);
}
```
*Implemented by:* `NearestDriverStrategy`, `LeastActiveDriverStrategy`

#### 2. Fare Calculation Strategy
```java
public interface FareStrategy {
    double calculateFare(Ride ride);
}
```
*Implemented by:* `DefaultFareStrategy`, `PeakHourFareStrategy`

---

## 🚀 Getting Started

### Prerequisites
- **Java 11 or higher**
- A terminal or IDE (e.g., IntelliJ IDEA, Eclipse) to run the application.

### Installation & Execution
1. **Clone the repository:**
   ```bash
   git clone https://github.com/totevarad/Ridewise-ModularRideSharingSystem.git
   cd Ridewise-ModularRideSharingSystem
   ```
2. **Compile the project:**
   Compile the Java classes within the `src/` directory.
   ```bash
   javac -d out src/**/*.java
   ```
3. **Run the Application:**
   Execute the `Main` class to interact with the Console Menu.
   ```bash
   java -cp out Main
   ```

---

## 🖥 Console Menu Interface

Upon running the application, you'll be greeted with an interactive console menu:

1. **Add Rider**
2. **Add Driver**
3. **View Available Drivers**
4. **Request Ride**
5. **Complete Ride**
6. **View Rides**
7. **Exit**

*Follow the on-screen prompts to navigate the system.*

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For questions or issues, please open an issue on GitHub.
