
# Online Marketplace

## Assignment 6: Observer and Visitor Patterns

**Variant 4: Online Marketplace**

This project demonstrates the Observer and Visitor design patterns using Java 17.

## Observer Pattern

The marketplace has two event sources:

- PriceSource
- StockSource

The system includes the following listeners:

- WishlistNotifier
- PriceDropAlert
- AnalyticsListener
- CacheListener
- StockAlertListener
- CatalogInspectionListener

The system supports subscriptions, unsubscriptions, and event filtering by type.

Events are represented by immutable MarketplaceEvent objects.

## Visitor Pattern

The marketplace catalog contains categories and five product types:

- PhysicalProduct
- DigitalProduct
- ServiceProduct
- SubscriptionProduct
- GiftCardProduct

Visitors perform operations without placing business logic inside product classes:

- TaxVisitor
- ShippingVisitor
- CatalogStatisticsVisitor
- ProductFilterVisitor
- XmlExportVisitor
- ProductNameVisitor

The catalog supports nested categories and uses double dispatch through accept() and visit() methods.

## Observer and Visitor Integration

When a product has low stock, CatalogInspectionListener receives a LOW_STOCK event.

It runs CatalogStatisticsVisitor on the related category and publishes an ANALYTICS_UPDATED event.

This demonstrates how Observer can trigger Visitor operations.

## Concurrency

ConcurrentEventSimulator uses ExecutorService to simulate multiple event producers.

The application demonstrates four producer threads generating 1000 events each.

Expected events: 4000

Received events: 4000

No events lost: true

## Testing

JUnit 5 tests cover:

- Observer subscriptions and notifications
- Visitor calculations and traversal
- Nested categories
- XML export
- Product filtering
- GiftCardProduct support
- Observer and Visitor integration
- Concurrent event delivery

The test suite includes more than 15 tests.

## Technologies

- Java 17
- Maven
- JUnit 5
- IntelliJ IDEA
- Git and GitHub

## How to Run

1. Open the project in IntelliJ IDEA.
2. Use Java 17.
3. Run Main.java to see the marketplace demonstration.
4. Run the JUnit test classes to verify the implementation.

## Design Decisions

### Observer: Push vs Pull

The project uses the push model. Event sources send immutable
MarketplaceEvent objects directly to subscribed listeners.

This allows listeners to react without repeatedly asking the
source for its current state.

Listeners subscribe to specific event types. They can also
unsubscribe at runtime.

### Visitor: Traversal Ownership

Traversal is controlled by visitors, not by catalog elements.

Catalog and Category store data and provide accept() methods.
Visitors decide how to traverse the tree and process products.

This keeps business operations separate from the catalog model.

### Observer and Visitor Integration

CatalogInspectionListener connects the two patterns.

When LOW_STOCK is received, it identifies the relevant category
and runs CatalogStatisticsVisitor only on that subtree.

The calculated result is published as ANALYTICS_UPDATED.

### Thread Safety

EventPublisher uses ConcurrentHashMap and CopyOnWriteArraySet
to manage subscribers.

ConcurrentEventSimulator uses ExecutorService with a configurable
number of producer threads.

The concurrency tests verify that 4000 events are delivered
without loss.

## Visitor Pattern Trade-offs

### Trade-off 1: Adding a New Visitor

ProductNameVisitor was added to collect product names.

It implements the existing CatalogVisitor interface.

No changes were required in existing catalog element classes.

This demonstrates that the Visitor pattern makes it easy
to add new operations.

### Trade-off 2: Adding a New Element

GiftCardProduct was added as a new product type.

The following files were updated:

- CatalogVisitor.java
- TaxVisitor.java
- ShippingVisitor.java
- CatalogStatisticsVisitor.java
- ProductFilterVisitor.java
- XmlExportVisitor.java
- ProductNameVisitor.java

GiftCardProduct.java was created and integrated into Main.java.

New JUnit tests verify gift card statistics, XML export,
and price filtering.

This demonstrates that adding a new element type requires
modifying the Visitor interface and its implementations.

### Conclusion

The Visitor pattern is useful when the object structure
is relatively stable but new operations are frequently added.

Adding a new visitor is easy because existing elements
do not need to change.

Adding a new element is more difficult because all existing
visitors must support the new element type.
