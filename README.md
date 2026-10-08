# NexusMarket

NexusMarket is a centralized digital marketplace that connects buyers and sellers, managing
user administration, product catalog, inventory, shopping carts, orders, billing, logistics,
returns, refunds, and administrative reporting.

Built with **Java 17**, **Spring Boot 4** and **Maven**, following **hexagonal architecture
(ports & adapters)** and **Domain-Driven Design**: business rules live in the domain, frameworks
stay in the adapters.

## Tech stack

| Layer | Technology |
|---|---|
| Language / runtime | Java 17, Spring Boot 4.1.1 |
| Transactional data | MySQL 8.4 (Spring Data JPA / Hibernate) |
| Documental data | MongoDB 7 (Spring Data MongoDB) — audit trail |
| API | REST (Spring MVC) + DTO records |
| Security | Spring Security (stateless HTTP Basic for admin endpoints) |
| Packaging | Docker / Docker Compose |

## Architecture

```text
Input Adapters (REST controllers + DTOs)
        │
        ▼
Input Ports (15 use-case interfaces)  ──►  Application services
        │
        ▼
DOMAIN (entities, value objects, enums, business rules, exceptions)
        │
        ▼
Output Ports (repository / service interfaces)
        │
        ├──► JPA adapters      ──► MySQL      (users, sellers, buyers, products, orders, ...)
        ├──► MongoDB adapter   ──► MongoDB    (audit trail)
        ├──► In-memory adapters (development profile)
        └──► FakePaymentService / ConsoleNotificationService
```

Key constraints (see `docs/NexusMarket_Domain_Model.md`):

- The domain has **no** framework, HTTP, JSON or database annotations.
- Persistence and request/response models are **separate classes** mapped at the edges
  (`*Entity` / `*Embeddable` for JPA, `*Document` for Mongo, records for REST).
- Domain entities are rebuilt through `reconstitute(...)` factories that preserve persisted
  state; `create(...)` applies creation-time defaults only.

## Getting started

### Prerequisites

- JDK 17+
- Docker + Docker Compose (recommended), or local MySQL 8.x + MongoDB 7.x

### Option A — Docker (recommended)

```bash
cd nexusmarket
docker compose up --build
```

Starts MySQL, MongoDB and the application (http://localhost:8080). Databases are created
and wired automatically via environment variables.

### Option B — Local databases

```bash
cd nexusmarket
./mvnw spring-boot:run
```

Default settings (see `src/main/resources/application.properties`):

| Variable | Default |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/nexusmarket?createDatabaseIfNotExist=true` |
| `SPRING_DATASOURCE_USERNAME` | `nexusmarket` |
| `SPRING_DATASOURCE_PASSWORD` | `nexusmarket` |
| `MONGODB_URI` | `mongodb://localhost:27017/nexusmarket` |
| `SECURITY_USER` / `SECURITY_PASSWORD` | `admin` / `admin` |

### Option C — No databases at all (in-memory development mode)

```bash
cd nexusmarket
./mvnw spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=in-memory"
```

Activates the in-memory adapters and disables MySQL/MongoDB auto-configuration — the whole
API runs with no infrastructure (data is lost on restart).

## API

All endpoints are under `/api`. Errors: `400` invalid input, `409` business-rule conflict,
both with `{"message": "..."}`.

| Method & path | Use case |
|---|---|
| `POST /api/users` | Create user |
| `POST /api/buyers` | Register buyer |
| `POST /api/sellers` | Register seller (requires acting `actorId`) |
| `POST /api/warehouses` | Create warehouse |
| `POST /api/products` | Create product |
| `PATCH /api/products/{id}/publish` | Publish product |
| `POST /api/inventory/entries` | Register stock entry |
| `POST /api/inventory/reservations` | Reserve stock |
| `POST /api/inventory/reservations/release` | Release reservation |
| `POST /api/inventory/sales` | Register sale |
| `POST /api/inventory/adjustments` | Adjust available quantity |
| `POST /api/inventory/returns` | Register returned stock |
| `POST /api/carts` | Create cart |
| `POST /api/orders` | Create order from cart |
| `POST /api/orders/{id}/payment` | Process payment |
| `PATCH /api/orders/{id}/finalize` | Finalize order |
| `POST /api/shipments` | Create shipment |
| `POST /api/returns` | Create return request |
| `POST /api/refunds` | Process refund |
| `GET /api/reports/{name}?startDate&endDate` | Administrative report (**HTTP Basic admin**) |

Every mutating request (POST/PUT/PATCH/DELETE) is recorded in the audit trail
(MongoDB, `audit_logs` collection). An optional `X-User-Id` header identifies the acting user.

## Testing

```bash
cd nexusmarket
./mvnw test
```

Domain tests cover the business rules from `docs/NexusMarket_Domain_Model.md` (RG-01…RG-15)
and require **no infrastructure** — no MySQL, no MongoDB, no Spring context.

## Project structure

```text
nexusmarket/
├── Dockerfile / docker-compose.yml
├── docs/NexusMarket_Domain_Model.md      # Domain model & business rules
└── src/main/java/application/
    ├── domain/
    │   ├── model/          # Entities (User, Order, Product, ...)
    │   ├── valueobject/    # Money, Email, Address, ...
    │   ├── enum/ exception/
    │   └── port/           # in/ (use cases) · out/ (repositories)
    ├── service/            # Application services (framework-free)
    └── infrastructure/
        ├── config/         # Composition root + security
        └── adapter/
            ├── web/        # REST controllers, DTOs, audit aspect
            ├── jpa/        # MySQL adapters (+ entity/)
            ├── mongo/      # MongoDB audit adapter
            ├── inmemory/   # Development adapters (profile "in-memory")
            ├── payment/ notification/
```
