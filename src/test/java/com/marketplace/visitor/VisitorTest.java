
package com.marketplace.visitor;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VisitorTest {

    private Catalog createCatalog() {
        PhysicalProduct laptop = new PhysicalProduct(
                "P001", "Laptop",
                new BigDecimal("350000"), 2.5
        );

        DigitalProduct ebook = new DigitalProduct(
                "D001", "E-Book",
                new BigDecimal("5000"), 15.0
        );

        ServiceProduct repair = new ServiceProduct(
                "S001", "Laptop Repair",
                new BigDecimal("15000"), 60
        );

        SubscriptionProduct premium = new SubscriptionProduct(
                "SUB001", "Premium Membership",
                new BigDecimal("8000"), 12
        );

        Category electronics = new Category(
                "Electronics", List.of(laptop, ebook)
        );

        Category services = new Category(
                "Services", List.of(repair, premium)
        );

        return new Catalog(
                "Online Marketplace",
                List.of(electronics, services)
        );
    }

    private Catalog createCatalogWithGiftCard() {
        GiftCardProduct giftCard = new GiftCardProduct(
                "G001", "Gift Card",
                new BigDecimal("10000")
        );

        Category gifts = new Category(
                "Gifts", List.of(giftCard)
        );

        return new Catalog(
                "Gift Marketplace", List.of(gifts)
        );
    }

    @Test
    void taxCalculationIsCorrect() {
        BigDecimal result = createCatalog().accept(
                new TaxVisitor()
        );

        assertEquals(
                0, new BigDecimal("43890.00").compareTo(result)
        );
    }

    @Test
    void shippingCalculationIsCorrect() {
        BigDecimal result = createCatalog().accept(
                new ShippingVisitor()
        );

        assertEquals(
                0, new BigDecimal("1000.00").compareTo(result)
        );
    }

    @Test
    void filterReturnsAffordableProducts() {
        ProductFilterVisitor visitor =
                new ProductFilterVisitor(
                        new BigDecimal("10000")
                );

        List<CatalogElement> products =
                createCatalog().accept(visitor);

        assertEquals(2, products.size());
    }

    @Test
    void statisticsCountsAllProducts() {
        CatalogStatisticsVisitor visitor =
                new CatalogStatisticsVisitor();

        createCatalog().accept(visitor);

        assertEquals(4, visitor.getTotalProductCount());
        assertEquals(1, visitor.getPhysicalCount());
        assertEquals(1, visitor.getDigitalCount());
        assertEquals(1, visitor.getServiceCount());
        assertEquals(1, visitor.getSubscriptionCount());
    }

    @Test
    void statisticsCalculatesTotalValue() {
        CatalogStatisticsVisitor visitor =
                new CatalogStatisticsVisitor();

        createCatalog().accept(visitor);

        assertEquals(
                0,
                new BigDecimal("378000")
                        .compareTo(visitor.getTotalValue())
        );
    }

    @Test
    void xmlContainsAllProductTypes() {
        String xml = createCatalog().accept(
                new XmlExportVisitor()
        );

        assertTrue(xml.contains("<catalog"));
        assertTrue(xml.contains("<category"));
        assertTrue(xml.contains("<physicalProduct"));
        assertTrue(xml.contains("<digitalProduct"));
        assertTrue(xml.contains("<serviceProduct"));
        assertTrue(xml.contains("<subscriptionProduct"));
        assertTrue(xml.contains("</catalog>"));
    }

    @Test
    void nestedCategoriesAreVisited() {
        PhysicalProduct mouse = new PhysicalProduct(
                "P002", "Mouse",
                new BigDecimal("2000"), 0.2
        );

        Category accessories = new Category(
                "Accessories", List.of(mouse)
        );

        Category electronics = new Category(
                "Electronics", List.of(accessories)
        );

        Catalog catalog = new Catalog(
                "Nested Marketplace",
                List.of(electronics)
        );

        CatalogStatisticsVisitor visitor =
                new CatalogStatisticsVisitor();

        catalog.accept(visitor);

        assertEquals(1, visitor.getTotalProductCount());

        assertEquals(
                0,
                new BigDecimal("2000")
                        .compareTo(visitor.getTotalValue())
        );
    }

    // NEW TEST 1: Gift card statistics

    @Test
    void giftCardIsCountedInStatistics() {
        CatalogStatisticsVisitor visitor =
                new CatalogStatisticsVisitor();

        createCatalogWithGiftCard().accept(visitor);

        assertEquals(1, visitor.getGiftCardCount());
        assertEquals(1, visitor.getTotalProductCount());

        assertEquals(
                0,
                new BigDecimal("10000")
                        .compareTo(visitor.getTotalValue())
        );
    }

    // NEW TEST 2: Gift card XML export

    @Test
    void giftCardIsExportedToXml() {
        String xml = createCatalogWithGiftCard().accept(
                new XmlExportVisitor()
        );

        assertTrue(xml.contains("<giftCardProduct"));
        assertTrue(xml.contains("G001"));
        assertTrue(xml.contains("Gift Card"));
        assertTrue(xml.contains("price=\"10000\""));
    }

    // NEW TEST 3: Gift card filtering

    @Test
    void giftCardCanBeFilteredByPrice() {
        Catalog catalog = createCatalogWithGiftCard();

        ProductFilterVisitor affordable =
                new ProductFilterVisitor(
                        new BigDecimal("10000")
                );

        ProductFilterVisitor tooCheap =
                new ProductFilterVisitor(
                        new BigDecimal("5000")
                );

        assertEquals(1, catalog.accept(affordable).size());
        assertEquals(0, catalog.accept(tooCheap).size());
    }
}
