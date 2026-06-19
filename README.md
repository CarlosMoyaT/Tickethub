# Event Ticket Booking Platform
[![Live Demo](https://img.shields.io/badge/Live-Demo-success)](https://carlos-moya-t-spring-boot-microservicios-a086461cr.vercel.app/home)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)]()
[![Microservices](https://img.shields.io/badge/Architecture-Microservices-blue)]()

A personal project built to practice microservices architecture with Java 21 and Spring Boot 3.
The platform allows users to browse events, check availability, and book tickets through
a distributed system with asynchronous communication.


## Architecture

![Architecture Diagram](inventoryservice/docs/architecture-design/Diagram.png)

The platform is built around four core microservices that communicate both synchronously
(REST via API Gateway) and asynchronously (Apache Kafka):

- The **Frontend** (Angular 20) sends requests through the **API Gateway**
- The **API Gateway** handles authentication via Keycloak JWT validation and routes
  requests to the appropriate service
- The **Booking Service** validates the customer, checks inventory availability,
  calculates the total price, and produces a Kafka event
- The **Order Service** consumes the Kafka event and persists the order,
  then updates inventory capacity
- The **Inventory Service** manages event and venue data

### Key architectural decisions

**Why Kafka instead of direct REST calls between Booking and Order?**
Decoupling the booking confirmation from the order creation means the booking service
doesn't need to wait for the order to be persisted. If the order service is temporarily
down, the Kafka message is retained and processed when the service recovers.

**Why a dedicated API Gateway?**
Centralizing authentication and routing in one place means the internal services
don't need to handle JWT validation individually. It also provides a single entry
point for circuit breaking with Resilience4j.

**Why Keycloak?**
Rather than implementing custom authentication, Keycloak provides a production-grade
identity provider with JWT tokens, which is what most enterprise environments use.

---

## Tech Stack

### Frontend
| Technology | Purpose |
|---|---|
| Angular 20 | SPA framework with standalone components and signals |
| Angular Material | UI component library |
| TypeScript | Type-safe frontend development |

### Backend
| Technology | Purpose |
|---|---|
| Java 21 | Modern language features (records, pattern matching) |
| Spring Boot 3.5 | Microservices framework |
| Spring Cloud Gateway | API Gateway and request routing |
| Resilience4j | Circuit breaker pattern |
| Lombok | Boilerplate reduction |
| Maven | Build and dependency management |

### Data
| Technology | Purpose |
|---|---|
| PostgreSQL | Relational database for each service |
| Flyway | Database schema versioning and migrations |

### Messaging & Security
| Technology | Purpose |
|---|---|
| Apache Kafka | Async event streaming between services |
| Keycloak | JWT-based authentication and authorization |

### Observability
| Technology | Purpose |
|---|---|
| Prometheus | Metrics collection via /actuator/prometheus |
| Grafana | Metrics visualization and dashboards |
| Spring Boot Actuator | Exposes health and metrics endpoints |

---

## Services

### Inventory Service — `localhost:8080`
Manages the catalogue of events and venues. Exposes REST endpoints consumed
by the Booking Service to check availability and ticket pricing.

**Endpoints:**
- `GET /api/v1/inventory/events` — list all events with availability
- `GET /api/v1/inventory/event/{eventId}` — event detail with capacity and price
- `GET /api/v1/inventory/venue/{venueId}` — venue information
- `PUT /api/v1/inventory/event/{eventId}/capacity/{capacity}` — update remaining capacity

**Docs:** http://localhost:8080/swagger-ui.html

![Inventory Service Swagger](inventoryservice/docs/documentationimg/InventoryService%20swagger.JPG)

---

### Booking Service — `localhost:8081`
Handles the ticket purchase flow. Validates that the customer exists,
checks availability in the Inventory Service, calculates the total price,
and produces a `BookingEvent` to the `booking` Kafka topic.

**Flow:**
1. Receive `POST /api/v1/booking` with customer and event data
2. Validate customer exists in the database
3. Call Inventory Service to get event capacity and ticket price
4. Verify sufficient capacity
5. Calculate total price
6. Publish `BookingEvent` to Kafka
7. Return booking confirmation

---

### Order Service — `localhost:8082`
Consumes `BookingEvent` messages from Kafka and persists the order to the database.
After saving, it calls the Inventory Service to decrement the available capacity.

**Flow:**
1. Consume `BookingEvent` from Kafka topic `booking`
2. Persist order to PostgreSQL via Flyway-managed schema
3. Call Inventory Service to update remaining capacity

---

### API Gateway — `localhost:8090`
Single entry point for all client requests. Validates JWT tokens issued by Keycloak
before forwarding requests to internal services. Implements circuit breaker pattern
with Resilience4j — if a service is unavailable, returns a controlled fallback response
instead of propagating the failure.

---

## Getting Started

### Prerequisites
- Docker and Docker Compose
- Java 21 (for local development without Docker)
- Maven (for local development without Docker)

### Run with Docker Compose
```bash
git clone https://github.com/CarlosMoyaT/CarlosMoyaT-Spring-boot-microservicios.git
cd CarlosMoyaT-Spring-boot-microservicios
docker compose up -d
```

Start the monitoring stack:
```bash
cd monitoring
docker compose up -d
```

### Service URLs after startup

| Service | URL |
|---|---|
| Frontend | http://localhost:4200 |
| API Gateway | http://localhost:8090 |
| Inventory Service | http://localhost:8080 |
| Booking Service | http://localhost:8081 |
| Order Service | http://localhost:8082 |
| Keycloak | http://localhost:8091 |
| Grafana | http://localhost:3000 |
| Prometheus | http://localhost:9090 |

---

## Observability

Each service exposes metrics at `/actuator/prometheus`. Prometheus scrapes these
endpoints every 15 seconds and Grafana provides dashboards for latency,
request counts, circuit breaker state, and JVM memory usage.

![Prometheus Metrics](inventoryservice/docs/documentationimg/Metric%20prometheus.JPG)

---

## Testing

The project includes integration tests using **Testcontainers**, which spin up
real Docker containers during the test phase — no mocking of the database or
message broker.
```bash
# Run tests for order service (requires Docker)
cd orderservice
./mvnw test
```

The `OrderRepositoryTest` verifies persistence behaviour against a real PostgreSQL
container, and `InventoryServiceClientTest` tests the HTTP client against a
containerized stub.










