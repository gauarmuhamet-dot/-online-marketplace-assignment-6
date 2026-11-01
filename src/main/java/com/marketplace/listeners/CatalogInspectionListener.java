
package com.marketplace.listeners;

import com.marketplace.observer.EventPublisher;
import com.marketplace.observer.MarketplaceEvent;
import com.marketplace.observer.Observer;
import com.marketplace.visitor.CatalogStatisticsVisitor;
import com.marketplace.visitor.Category;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CatalogInspectionListener
        extends EventPublisher<MarketplaceEvent>
        implements Observer<MarketplaceEvent> {

    private final Map<String, Category> productCategories;

    private final CopyOnWriteArrayList<String> reports =
            new CopyOnWriteArrayList<>();

    public CatalogInspectionListener(
            Map<String, Category> productCategories) {

        this.productCategories = Map.copyOf(
                Objects.requireNonNull(productCategories)
        );
    }

    @Override
    public void onEvent(MarketplaceEvent event) {

        if (event.getType() != MarketplaceEvent.Type.LOW_STOCK) {
            return;
        }

        Category category =
                productCategories.get(event.getProductId());

        if (category == null) {
            return;
        }

        CatalogStatisticsVisitor visitor =
                new CatalogStatisticsVisitor();

        category.accept(visitor);

        String report = "Low stock inspection: "
                + event.getProductId()
                + ", category: " + category.getName()
                + ", products inspected: "
                + visitor.getTotalProductCount()
                + ", category value: "
                + visitor.getTotalValue();

        reports.add(report);

        MarketplaceEvent resultEvent =
                new MarketplaceEvent(
                        MarketplaceEvent.Type.ANALYTICS_UPDATED,
                        event.getProductId(),
                        visitor.getTotalValue(),
                        visitor.getTotalProductCount()
                );

        notifyObservers(
                "ANALYTICS_UPDATED",
                resultEvent
        );
    }

    public List<String> getReports() {
        return List.copyOf(reports);
    }

    public int getReportCount() {
        return reports.size();
    }
}
