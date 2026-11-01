
package com.marketplace.integration;

import com.marketplace.listeners.CatalogInspectionListener;
import com.marketplace.observer.StockSource;
import com.marketplace.visitor.Category;
import com.marketplace.visitor.PhysicalProduct;
import com.marketplace.visitor.DigitalProduct;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    private Category createCategory() {
        PhysicalProduct laptop = new PhysicalProduct(
                "P001", "Laptop",
                new BigDecimal("350000"), 2.5
        );

        DigitalProduct ebook = new DigitalProduct(
                "D001", "E-Book",
                new BigDecimal("5000"), 15.0
        );

        return new Category(
                "Electronics",
                List.of(laptop, ebook)
        );
    }

    @Test
    void lowStockTriggersVisitor() {
        Category electronics = createCategory();

        CatalogInspectionListener inspection =
                new CatalogInspectionListener(
                        Map.of("P001", electronics)
                );

        StockSource stockSource = new StockSource();

        stockSource.subscribe("LOW_STOCK", inspection);

        stockSource.updateStock("P001", 3);

        assertEquals(1, inspection.getReportCount());
    }

    @Test
    void normalStockDoesNotTriggerVisitor() {
        Category electronics = createCategory();

        CatalogInspectionListener inspection =
                new CatalogInspectionListener(
                        Map.of("P001", electronics)
                );

        StockSource stockSource = new StockSource();

        stockSource.subscribe("LOW_STOCK", inspection);

        stockSource.updateStock("P001", 20);

        assertEquals(0, inspection.getReportCount());
    }

    @Test
    void inspectionUsesCorrectCategory() {
        Category electronics = createCategory();

        CatalogInspectionListener inspection =
                new CatalogInspectionListener(
                        Map.of("P001", electronics)
                );

        StockSource stockSource = new StockSource();

        stockSource.subscribe("LOW_STOCK", inspection);

        stockSource.updateStock("P001", 2);

        String report = inspection.getReports().get(0);

        assertTrue(report.contains("Electronics"));
        assertTrue(report.contains("products inspected: 2"));
        assertTrue(report.contains("category value: 355000"));
    }

    @Test
    void unknownProductDoesNotCreateReport() {
        Category electronics = createCategory();

        CatalogInspectionListener inspection =
                new CatalogInspectionListener(
                        Map.of("P001", electronics)
                );

        StockSource stockSource = new StockSource();

        stockSource.subscribe("LOW_STOCK", inspection);

        stockSource.updateStock("UNKNOWN", 1);

        assertEquals(0, inspection.getReportCount());
    }
}
