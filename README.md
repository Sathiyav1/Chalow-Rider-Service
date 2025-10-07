# RiderService

A standalone Spring Boot service extracted from the monolith to provide RiderController endpoints.

Requirements
- Java 17
- Maven 3.8+

Build
- From the project root (/Users/Hershey/Documents/Sathiyan/App_Workspace/RiderService):

```bash
mvn -DskipTests clean package
```

Run
- Start the service:

```bash
java -jar target/RiderService.jar
```

Configuration
- `src/main/resources/application.properties` contains defaults:
  - `server.servlet.context-path=/rider`
  - `server.port=8082`
  - Actuator endpoints are available under `/actuator` (liveness/readiness/health)

Notes
- This standalone service uses in-memory implementations for user and ride operations to provide fully functional endpoints for development and testing.
- For production, replace the in-memory services with real persistence and security wiring.
