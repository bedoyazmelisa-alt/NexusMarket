# NexusMarket — Domain Model

## Overview

The **NexusMarket** platform is a centralized digital marketplace that acts as an intermediary between buyers and sellers.

The system manages the commercial operation from user and seller administration through product catalog management, inventory, shopping carts, orders, billing, logistics, returns, refunds, and administrative reporting.

The domain model represents the business concepts, relationships, behaviors, and business rules required to support these processes.

This model follows **Domain-Driven Design (DDD)** principles and is intended to be implemented according to the project's established coding conventions.

The main objective is to ensure that the business domain is represented coherently in the code, keeping business rules inside the Domain and separating them from technical implementation concerns.

---

# Domain Modeling Principles

The NexusMarket domain model follows these principles:

- Domain-first design.
- Business rules belong to the Domain.
- Entities represent concepts with identity.
- Value Objects represent concepts defined by their values.
- Relationships between entities must reflect real business relationships.
- Business behavior must be modeled explicitly.
- Domain invariants must be enforced by the Domain.
- Domain models must remain independent from infrastructure.
- Persistence concerns must not define the business model.
- Controllers and DTOs must not contain business rules.
- Technical frameworks must not determine domain behavior.

---

# Business Domains

The functional specification defines the following main business domains:

```text
NexusMarket
│
├── User Management
├── Buyer Management
├── Seller Management
├── Warehouse Management
├── Product Catalog
├── Inventory Management
├── Shopping Cart
├── Order Management
├── Billing
├── Logistics
├── Returns
├── Refunds
└── Administrative Reporting
```

These domains correspond to the business processes included in the NexusMarket scope.

---

# Domain Entities

The main entities identified for the domain are:

```text
User
Buyer
Seller
Warehouse
Product
ProductVariant
Inventory
InventoryMovement
Cart
CartItem
Order
OrderItem
Invoice
Shipment
Return
Refund
AuditLog
```

Not every entity necessarily represents an independent aggregate. Aggregate boundaries must be defined according to the behavior and consistency rules of the domain.

---

# User

## Description

`User` represents a person authorized to interact with NexusMarket.

A user has a unique identity and a single role within the system.

## Attributes

```text
Id
Identification
FullName
Email
Role
Status
```

## Business Rules

- The user identifier must be unique.
- The identification must be unique.
- The email must be unique.
- Every user must have exactly one role.
- The user must have a valid operational status.
- A user can only perform operations allowed by their role.
- Every operation must be executed by an authenticated user.

## Behavior

Potential domain behaviors include:

```text
activate()
block()
changeRole()
changeStatus()
```

The entity must protect its own valid state instead of allowing arbitrary modifications from external layers.

---

# Buyer

## Description

`Buyer` represents a customer who purchases products through NexusMarket.

## Attributes

```text
Id
UserId
PrimaryAddress
AdditionalAddresses
CommercialStatus
```

## Relationships

```text
User 1 ───── 1 Buyer
Buyer 1 ───── N Address
Buyer 1 ───── N Cart
Buyer 1 ───── N Order
```

## Business Rules

- A buyer must have a primary address.
- Additional addresses are optional.
- The buyer must have a valid commercial status.
- A buyer cannot administer information belonging to another buyer.
- A buyer cannot administer inventory.

## Behavior

Potential behaviors include:

```text
addAddress()
removeAddress()
setPrimaryAddress()
activate()
suspend()
```

---

# Seller

## Description

`Seller` represents a business participant responsible for commercializing products through the marketplace.

## Attributes

```text
Id
UserId
BusinessInformation
Status
```

## Relationships

```text
User 1 ───── 1 Seller
Seller 1 ───── N Product
Seller 1 ───── N Warehouse
```

## Business Rules

- Sellers cannot self-register.
- Sellers must be incorporated by an Administrator.
- A seller may manage its own products.
- A seller may have warehouses associated with its operation.

## Behavior

Potential behaviors include:

```text
activate()
suspend()
addProduct()
removeProduct()
associateWarehouse()
```

---

# Warehouse

## Description

`Warehouse` represents a physical location where inventory is stored.

NexusMarket distinguishes between Marketplace warehouses and Seller warehouses.

## Attributes

```text
Id
Name
Address
WarehouseType
Status
```

## Relationships

```text
Warehouse 1 ───── N Inventory
Seller 1 ───── N Warehouse
```

## Business Rules

- A warehouse must have a valid type.
- Inventory must be associated with a specific warehouse.
- Warehouse operations must be performed according to the user's role.

## Behavior

Potential behaviors include:

```text
activate()
deactivate()
receiveInventory()
```

---

# Product

## Description

`Product` represents a good offered through the NexusMarket catalog.

Products can be physical or digital.

## Attributes

```text
Id
SellerId
Name
Description
ProductType
Status
BasePrice
```

## Relationships

```text
Seller 1 ───── N Product
Product 1 ───── N ProductVariant
Product 1 ───── N Inventory
Product 1 ───── N OrderItem
```

## Product Types

```text
Physical
Digital
```

## Product States

```text
Published
Suspended
Discontinued
```

## Business Rules

- Physical products require inventory and shipping.
- Digital products can be delivered immediately after payment.
- A product must have a valid status.
- Product publication must follow the business rules defined by the marketplace.
- Product variants belong to a product.

## Behavior

Potential behaviors include:

```text
publish()
suspend()
discontinue()
addVariant()
removeVariant()
changePrice()
```

---

# ProductVariant

## Description

`ProductVariant` represents a specific variation of a product.

Examples include:

- Color.
- Size.
- Model.

## Attributes

```text
Id
ProductId
Name
Attributes
Price
Status
```

## Relationship

```text
Product 1 ───── N ProductVariant
```

## Business Rules

- A variant must belong to a product.
- Variant information must remain consistent with its parent product.

---

# Inventory

## Description

`Inventory` represents the available stock of a product within a specific warehouse.

Inventory is distributed and must always be associated with both a product and a warehouse.

## Attributes

```text
Id
ProductId
WarehouseId
AvailableQuantity
ReservedQuantity
DamagedQuantity
```

## Relationships

```text
Product 1 ───── N Inventory
Warehouse 1 ───── N Inventory
Inventory 1 ───── N InventoryMovement
```

## Business Rules

- Inventory must belong to a product.
- Inventory must belong to a warehouse.
- Negative inventory is never allowed.
- Inventory cannot be reserved if it does not exist.
- Inventory marked as damaged cannot be reserved.
- Inventory changes must be represented through valid inventory movements.

## Inventory Movements

```text
Entry
Reservation
Sale
Adjustment
Return
```

## Behavior

Potential domain behaviors include:

```text
increase()
reserve()
releaseReservation()
registerSale()
adjust()
registerReturn()
```

Example invariant:

```text
availableQuantity >= 0
```

The invariant must be protected by the Domain.

---

# InventoryMovement

## Description

`InventoryMovement` represents a business event that changes inventory.

## Attributes

```text
Id
InventoryId
MovementType
Quantity
Date
Reason
```

## Business Rules

- Movement quantity must be valid.
- A movement must have a valid movement type.
- Movements must not result in negative inventory.
- Reservations cannot exceed available inventory.

---

# Cart

## Description

`Cart` represents a buyer's provisional selection of products before an order is formally created.

## Attributes

```text
Id
BuyerId
Status
CreatedAt
UpdatedAt
```

## Relationships

```text
Buyer 1 ───── N Cart
Cart 1 ───── N CartItem
CartItem N ───── 1 Product
```

## Business Rules

- A cart belongs to a buyer.
- Cart items must reference valid products.
- The cart represents a provisional selection.
- Checkout transforms the selected information into a formal order.

## Behavior

```text
addItem()
removeItem()
changeQuantity()
clear()
checkout()
```

---

# CartItem

## Description

`CartItem` represents a product selected in a shopping cart.

## Attributes

```text
Id
CartId
ProductId
Quantity
UnitPrice
```

## Business Rules

- Quantity must be greater than zero.
- The referenced product must exist.
- Product availability must be validated before creating the order.

---

# Order

## Description

`Order` represents the formal commercial commitment created by a buyer.

The order is one of the central business concepts in NexusMarket.

## Attributes

```text
Id
BuyerId
Status
TotalAmount
CreatedAt
UpdatedAt
```

## Relationships

```text
Buyer 1 ───── N Order
Order 1 ───── N OrderItem
Order 1 ───── 1 Invoice
Order 1 ───── N Shipment
Order 1 ───── N Return
```

## Order Lifecycle

```text
Cart
   │
   ▼
Pending Payment
   │
   ▼
Paid
   │
   ▼
Dispatched
   │
   ▼
Delivered / Finalized
```

## Business Rules

- Payment must be confirmed before fulfillment.
- Inventory must be available before reservation.
- The order must follow valid state transitions.
- A finalized order cannot be modified under any circumstance.
- Order information must remain consistent with its items and totals.

## Behavior

Potential domain behaviors include:

```text
create()
addItem()
removeItem()
calculateTotal()
markAsPendingPayment()
markAsPaid()
markAsDispatched()
markAsDelivered()
finalize()
```

The `finalize()` operation must establish the final state and prevent subsequent modifications.

---

# OrderItem

## Description

`OrderItem` represents a product included in an order.

## Attributes

```text
Id
OrderId
ProductId
Quantity
UnitPrice
Subtotal
```

## Business Rules

- Quantity must be greater than zero.
- Unit price must be valid.
- Subtotal must be consistent with quantity and unit price.
- Order items belonging to a finalized order cannot be modified.

---

# Invoice

## Description

`Invoice` represents the commercial billing information associated with a purchase.

## Attributes

```text
Id
OrderId
InvoiceNumber
IssueDate
Subtotal
Taxes
Total
Status
```

## Relationship

```text
Order 1 ───── 1 Invoice
```

The exact invoicing rules must be defined by the detailed business requirements before implementing additional domain behavior.

---

# Shipment

## Description

`Shipment` represents the logistics process associated with the physical fulfillment of an order.

## Attributes

```text
Id
OrderId
WarehouseId
TrackingNumber
Status
ShipmentDate
DeliveryDate
```

## Relationships

```text
Order 1 ───── N Shipment
Warehouse 1 ───── N Shipment
```

## Business Rules

- Physical products require logistics processing.
- Shipment status must follow valid transitions.
- Dispatch occurs after the required order conditions are satisfied.

## Behavior

Potential behaviors include:

```text
prepare()
dispatch()
markInTransit()
markDelivered()
```

---

# Return

## Description

`Return` represents a product return requested or processed after a purchase.

## Attributes

```text
Id
OrderId
Status
Reason
CreatedAt
ProcessedAt
```

## Relationships

```text
Order 1 ───── N Return
Return 1 ───── N ReturnItem
```

The detailed eligibility and approval rules for returns are not specified in the functional document and must be defined before adding more specific business behavior.

---

# Refund

## Description

`Refund` represents the reimbursement associated with an approved return or other applicable business operation.

## Attributes

```text
Id
ReturnId
Amount
Status
CreatedAt
ProcessedAt
```

## Relationship

```text
Return 1 ───── 1 Refund
```

The detailed refund rules are not sufficiently specified in the functional document and therefore should not be invented at the domain-modeling stage.

---

# AuditLog

## Description

`AuditLog` represents information required to maintain operational traceability.

## Attributes

```text
Id
UserId
Action
Entity
EntityId
Timestamp
Details
```

The exact audit requirements should be refined according to the technical and operational requirements of the project.

---

# Value Objects

Value Objects represent concepts whose identity is determined by their values.

Potential NexusMarket Value Objects include:

```text
Email
Address
Money
ProductCode
InvoiceNumber
TrackingNumber
```

---

# Email

`Email` encapsulates email validation and normalization.

```text
Email
├── value
└── validation rules
```

The Domain should receive a valid `Email` rather than a raw string when the concept requires domain validation.

---

# Address

`Address` represents a delivery location.

Potential attributes include:

```text
Street
City
State
PostalCode
Country
```

An address is compared by its values rather than by an independent business identity when modeled as a Value Object.

---

# Money

`Money` represents a monetary amount and its currency.

```text
Money
├── amount
└── currency
```

Monetary calculations should be performed through domain-safe operations rather than unrestricted primitive arithmetic.

---

# Enums

The domain may define enumerations for controlled business states and classifications.

Examples:

```text
UserRole
UserStatus
BuyerStatus
SellerStatus
WarehouseType
ProductType
ProductStatus
InventoryMovementType
OrderStatus
ShipmentStatus
ReturnStatus
RefundStatus
```

Enums should represent business concepts rather than database-specific implementation details.

---

# Domain Services

A Domain Service is used when a business operation involves multiple domain objects and does not naturally belong to a single entity.

Potential services include:

```text
SellerRegistrationService
InventoryReservationService
OrderCreationService
OrderFinalizationService
OrderValidationService
RefundProcessingService
```

---

## SellerRegistrationService

Responsible for the business operation of incorporating a seller.

Main rule:

```text
Seller cannot self-register.
Seller must be incorporated by an Administrator.
```

---

## InventoryReservationService

Responsible for coordinating inventory reservations.

Main rules:

```text
Inventory must exist.
Inventory must not be damaged.
Requested quantity must be available.
Inventory cannot become negative.
```

---

## OrderCreationService

Coordinates the creation of a formal order from a cart.

Potential responsibilities:

```text
Validate buyer
Validate cart
Validate product availability
Reserve inventory
Create order
Calculate totals
Persist order through an Output Port
```

---

## OrderFinalizationService

Coordinates the final stage of an order.

Main rule:

```text
A finalized order cannot be modified.
```

---

# Aggregates

The domain should define aggregate boundaries around concepts that require transactional consistency.

A proposed initial model is:

```text
Buyer Aggregate
└── Buyer
    └── Addresses

Seller Aggregate
└── Seller

Warehouse Aggregate
└── Warehouse

Product Aggregate
└── Product
    └── ProductVariants

Inventory Aggregate
└── Inventory
    └── InventoryMovements

Cart Aggregate
└── Cart
    └── CartItems

Order Aggregate
└── Order
    └── OrderItems
```

The aggregate root is responsible for protecting invariants within its boundary.

---

# Domain Ports

Ports define communication contracts between the Domain and external systems.

The Domain owns these interfaces.

```text
domain/
└── ports/
    ├── in/
    └── out/
```

---

# Input Ports

Input Ports represent the business operations that the application exposes.

Examples:

```text
CreateUserUseCase
RegisterBuyerUseCase
RegisterSellerUseCase
CreateWarehouseUseCase
CreateProductUseCase
PublishProductUseCase
ManageInventoryUseCase
CreateCartUseCase
CreateOrderUseCase
ProcessPaymentUseCase
CreateShipmentUseCase
FinalizeOrderUseCase
CreateReturnUseCase
ProcessRefundUseCase
GenerateReportUseCase
```

Input Ports describe what the system can do without exposing how it is technically implemented.

---

# Output Ports

Output Ports represent dependencies required by the Domain.

Examples:

```text
UserRepository
BuyerRepository
SellerRepository
WarehouseRepository
ProductRepository
InventoryRepository
CartRepository
OrderRepository
InvoiceRepository
ShipmentRepository
ReturnRepository
RefundRepository
AuditRepository
NotificationService
PaymentService
```

The Domain depends on these abstractions rather than concrete infrastructure implementations.

---

# Domain Exceptions

Business exceptions belong to the Domain.

Examples:

```text
InvalidUserStatusException
SellerRegistrationNotAllowedException
InvalidProductStateException
InsufficientInventoryException
DamagedInventoryException
InvalidInventoryMovementException
InvalidOrderStateException
FinalizedOrderModificationException
InvalidReturnException
InvalidRefundException
```

Exceptions should represent violations of business rules.

---

# Main Domain Relationships

The main relationships can be summarized as follows:

```text
User
├── Buyer
│   ├── Cart
│   │   └── CartItem ─── Product
│   │
│   └── Order
│       ├── OrderItem ─── Product
│       ├── Invoice
│       ├── Shipment ─── Warehouse
│       └── Return
│           └── Refund
│
└── Seller
    ├── Product
    │   └── ProductVariant
    │
    └── Warehouse
            │
            └── Inventory
                    └── InventoryMovement
```

---

# Core Business Flow

The complete business flow can be represented as:

```text
Administrator
      │
      ▼
Register Seller
      │
      ▼
Register Warehouse
      │
      ▼
Seller Registers Products
      │
      ▼
Register Initial Inventory
      │
      ▼
Publish Product
      │
      ▼
Buyer Selects Product
      │
      ▼
Shopping Cart
      │
      ▼
Create Order
      │
      ▼
Pending Payment
      │
      ▼
Payment Confirmation
      │
      ▼
Inventory Reservation
      │
      ▼
Paid
      │
      ▼
Preparation
      │
      ▼
Dispatch
      │
      ▼
Transportation
      │
      ▼
Delivery
      │
      ▼
Finalized
```

---

# Critical Business Rules

## User Rules

```text
RG-01
Every operation must be executed by an authenticated user.

RG-02
Each user has exactly one role.

RG-03
Users cannot administer information outside their assigned role.
```

## Seller Rules

```text
RG-04
Sellers cannot self-register.

RG-05
Seller registration must be performed by an Administrator.
```

## Inventory Rules

```text
RG-06
Inventory must be associated with a product and warehouse.

RG-07
Inventory cannot become negative.

RG-08
Inventory cannot be reserved when it does not exist.

RG-09
Damaged inventory cannot be reserved.
```

## Order Rules

```text
RG-10
Orders must follow a valid lifecycle.

RG-11
Payment confirmation is required before fulfillment.

RG-12
A finalized order cannot be modified.
```

## Data Integrity Rules

```text
RG-13
User identification must be unique.

RG-14
User email must be unique.

RG-15
Business state transitions must be controlled by the Domain.
```

---

# Domain Independence

The Domain must not depend on external technologies.

The following dependencies are prohibited inside the Domain:

```text
Spring
Spring Boot
JPA
Hibernate
MySQL
MongoDB
SQL
REST
HTTP
JSON
Controllers
Request DTOs
Response DTOs
Persistence Entities
```

For example, this is incorrect:

```text
Domain Service
      │
      ▼
JpaRepository
      │
      ▼
MySQL
```

The correct dependency direction is:

```text
Domain Service
      │
      ▼
Output Port
      ▲
      │ implements
      │
Persistence Adapter
      │
      ▼
JpaRepository
      │
      ▼
MySQL
```

---

# Coding Conventions

The domain implementation must follow the project's coding conventions.

The following conventions are recommended for Domain entities:

## Private Constructors

Entities should prevent uncontrolled construction when the business model requires invariants.

```java
private Product() {
}
```

## Factory Methods

Creation should occur through explicit factory methods when business rules must be validated.

```java
public static Product create(...) {
    // validate business rules
    // create valid entity
}
```

## Encapsulation

Domain properties should not be freely mutable from external code.

Prefer:

```java
private String name;
```

over exposing unrestricted setters.

Business state changes should be expressed through domain methods.

For example:

```java
product.publish();
product.suspend();
order.markAsPaid();
order.finalize();
inventory.reserve(quantity);
```

instead of:

```java
product.setStatus(...);
order.setStatus(...);
inventory.setQuantity(...);
```

## Validation

Business validations must be executed inside the Domain.

Examples:

```text
Inventory cannot become negative.
Finalized orders cannot be modified.
Damaged inventory cannot be reserved.
Sellers cannot self-register.
```

---

# Domain Testing

The Domain must be testable without infrastructure.

Examples of unit tests include:

```text
shouldNotAllowNegativeInventory()
shouldNotReserveDamagedInventory()
shouldNotAllowSellerSelfRegistration()
shouldNotModifyFinalizedOrder()
shouldRequireValidOrderStateTransition()
shouldRequireUniqueUserEmail()
shouldRequireValidProductStatus()
```

These tests should not require:

```text
MySQL
MongoDB
REST Server
Spring Context
HTTP
External APIs
```

---

# Architecture Boundary

The Domain Model is implemented inside the Domain layer of the Hexagonal Architecture.

```text
                    ┌───────────────────────────┐
                    │      Input Adapters       │
                    │ REST / Controllers / DTOs │
                    └────────────┬──────────────┘
                                 │
                                 ▼
                    ┌───────────────────────────┐
                    │        Input Ports        │
                    │         Use Cases          │
                    └────────────┬──────────────┘
                                 │
                                 ▼
              ┌────────────────────────────────────────┐
              │                 DOMAIN                 │
              │                                        │
              │ Entities                               │
              │ Value Objects                          │
              │ Enums                                  │
              │ Domain Services                        │
              │ Business Rules                         │
              │ Exceptions                             │
              │ Input Ports                            │
              │ Output Ports                           │
              │                                        │
              └────────────────┬───────────────────────┘
                               │
                               ▼
                    ┌───────────────────────────┐
                    │       Output Ports        │
                    └────────────┬──────────────┘
                                 │
                    ┌────────────┴────────────┐
                    ▼                         ▼
           ┌─────────────────┐       ┌─────────────────┐
           │ MySQL Adapter   │       │ MongoDB Adapter │
           └────────┬────────┘       └────────┬────────┘
                    │                         │
                    ▼                         ▼
                 MySQL                     MongoDB
```

---

# Architectural Constraints

The following rules must always be respected:

1. Business logic belongs exclusively to the Domain.
2. Domain entities must represent business concepts rather than database tables.
3. Controllers must not contain business rules.
4. DTOs must never enter the Domain.
5. Persistence entities must never be exposed through the API.
6. Communication with external systems must occur through Ports.
7. Adapters implement Ports but do not define business rules.
8. Infrastructure must never be a dependency of the Domain.
9. Domain entities must remain framework-independent.
10. Business invariants must be enforced inside the Domain.
11. State transitions must be controlled by domain behavior.
12. Domain tests must not require infrastructure.
13. Database-specific annotations must not be placed on Domain entities.
14. REST-specific annotations must not be placed on Domain entities.
15. The Domain must not depend on SQL, JSON, HTTP, JPA, MongoDB, or Spring.

---

# Final Domain Model

The NexusMarket domain is centered around the commercial relationship between buyers, sellers, products, inventory, and orders.

The principal business chain is:

```text
Seller
   │
   ├── Product
   │      │
   │      └── Inventory
   │              │
   │              └── Warehouse
   │
   ▼
Product Catalog
   │
   ▼
Buyer
   │
   ▼
Cart
   │
   ▼
Order
   │
   ├── Invoice
   ├── Shipment
   └── Return
          │
          └── Refund
```

The Domain is responsible for maintaining the consistency of this model and enforcing the business rules defined by NexusMarket.

The technical implementation, database technology, REST framework, and infrastructure remain outside the Domain and communicate with it through clearly defined Ports and Adapters.
