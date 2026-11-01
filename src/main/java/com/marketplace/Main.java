
package com.marketplace;

import com.marketplace.concurrent.ConcurrentEventSimulator;
import com.marketplace.listeners.*;
import com.marketplace.observer.*;
import com.marketplace.visitor.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {

        // PART 1: CREATE CATALOG

        PhysicalProduct laptop = new PhysicalProduct(
                "P001",
                "Laptop",
                new BigDecimal("350000"),
                2.5
        );

        DigitalProduct ebook = new DigitalProduct(
                "D001",
                "E-Book",
                new BigDecimal("5000"),
                15.0
        );

        ServiceProduct repair = new ServiceProduct(
                "S001",
                "Laptop Repair",
                new BigDecimal("15000"),
                60
        );

        SubscriptionProduct premium = new SubscriptionProduct(
                "SUB001",
                "Premium Membership",
                new BigDecimal("8000"),
                12
        );

        // NEW ELEMENT: GiftCardProduct
        GiftCardProduct giftCard = new GiftCardProduct(
                "G001",
                "Gift Card",
                new BigDecimal("10000")
        );

        Category electronics = new Category(
                "Electronics",
                List.of(laptop, ebook)
        );

        Category services = new Category(
                "Services",
                List.of(repair, premium)
        );

        Category gifts = new Category(
                "Gifts",
                List.of(giftCard)
        );

        Catalog catalog = new Catalog(
                "Online Marketplace",
                List.of(electronics, services, gifts)
        );

        // PART 2: OBSERVER PATTERN

        PriceSource priceSource = new PriceSource();
        StockSource stockSource = new StockSource();

        WishlistNotifier wishlist = new WishlistNotifier();

        PriceDropAlert priceAlert =
                new PriceDropAlert(new BigDecimal("8000"));

        AnalyticsListener analytics = new AnalyticsListener();
        CacheListener cache = new CacheListener();

        StockAlertListener stockAlert =
                new StockAlertListener();

        CatalogInspectionListener inspection =
                new CatalogInspectionListener(
                        Map.of(
                                "P001", electronics,
                                "D001", electronics,
                                "S001", services,
                                "SUB001", services,
                                "G001", gifts
                        )
                );

        wishlist.addToWishlist("P001");

        priceSource.subscribe("PRICE_DROP", wishlist);
        priceSource.subscribe("PRICE_DROP", priceAlert);
        priceSource.subscribe("PRICE_CHANGED", analytics);
        priceSource.subscribe("PRICE_DROP", analytics);
        priceSource.subscribe("PRICE_CHANGED", cache);

        stockSource.subscribe("STOCK_CHANGED", analytics);
        stockSource.subscribe("LOW_STOCK", analytics);
        stockSource.subscribe("STOCK_CHANGED", cache);
        stockSource.subscribe("LOW_STOCK", stockAlert);

        // Observer + Visitor integration
        stockSource.subscribe("LOW_STOCK", inspection);

        inspection.subscribe("ANALYTICS_UPDATED", event -> {
            System.out.println("=== VISITOR EVENT RECEIVED ===");
            System.out.println("Product ID: " + event.getProductId());
            System.out.println("Category value: " + event.getPrice());
            System.out.println("Products inspected: " + event.getStock());
        });

        priceSource.updatePrice(
                "P001", new BigDecimal("10000")
        );

        priceSource.updatePrice(
                "P001", new BigDecimal("7500")
        );

        stockSource.updateStock("P001", 10);
        stockSource.updateStock("P001", 3);

        System.out.println("=== OBSERVER RESULTS ===");

        System.out.println("Wishlist notifications: "
                + wishlist.getNotificationCount());

        System.out.println("Price alerts: "
                + priceAlert.getAlertCount());

        System.out.println("Stock alerts: "
                + stockAlert.getAlertCount());

        System.out.println("Total analytics events: "
                + analytics.getTotalEvents());

        System.out.println("Cached price: "
                + cache.getCachedPrice("P001"));

        System.out.println("Cached stock: "
                + cache.getCachedStock("P001"));

        // PART 3: VISITOR PATTERN

        TaxVisitor taxVisitor = new TaxVisitor();

        ShippingVisitor shippingVisitor =
                new ShippingVisitor();

        ProductFilterVisitor filterVisitor =
                new ProductFilterVisitor(
                        new BigDecimal("10000")
                );

        XmlExportVisitor xmlVisitor =
                new XmlExportVisitor();

        CatalogStatisticsVisitor statisticsVisitor =
                new CatalogStatisticsVisitor();

        System.out.println("\n=== VISITOR RESULTS ===");

        System.out.println("Total tax: "
                + catalog.accept(taxVisitor));

        System.out.println("Total shipping: "
                + catalog.accept(shippingVisitor));

        System.out.println("Products under 10000: "
                + catalog.accept(filterVisitor).size());

        catalog.accept(statisticsVisitor);

        System.out.println("Total products: "
                + statisticsVisitor.getTotalProductCount());

        System.out.println("Gift cards: "
                + statisticsVisitor.getGiftCardCount());

        System.out.println("Total catalog value: "
                + statisticsVisitor.getTotalValue());

        System.out.println("\n=== XML EXPORT ===");

        System.out.println(
                catalog.accept(xmlVisitor)
        );

        // PART 4: OBSERVER + VISITOR INTEGRATION

        System.out.println("=== INTEGRATION RESULTS ===");

        System.out.println("Inspection reports: "
                + inspection.getReportCount());

        for (String report : inspection.getReports()) {
            System.out.println(report);
        }

        // PART 5: CONCURRENCY

        System.out.println("\n=== CONCURRENCY RESULTS ===");

        EventPublisher<MarketplaceEvent> concurrentPublisher =
                new EventPublisher<>();

        AtomicInteger receivedEvents =
                new AtomicInteger(0);

        concurrentPublisher.subscribe(
                "STOCK_CHANGED",
                event -> receivedEvents.incrementAndGet()
        );

        int threadCount = 4;
        int eventsPerThread = 1000;

        ConcurrentEventSimulator simulator =
                new ConcurrentEventSimulator(
                        threadCount,
                        eventsPerThread
                );

        try {
            simulator.run(concurrentPublisher);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Concurrency simulation failed", e
            );
        }

        int expectedEvents =
                threadCount * eventsPerThread;

        int actualEvents =
                receivedEvents.get();

        System.out.println(
                "Producer threads: " + threadCount
        );

        System.out.println(
                "Events per thread: " + eventsPerThread
        );

        System.out.println(
                "Expected events: " + expectedEvents
        );

        System.out.println(
                "Received events: " + actualEvents
        );

        System.out.println(
                "No events lost: "
                        + (expectedEvents == actualEvents)
        );
    }
}
