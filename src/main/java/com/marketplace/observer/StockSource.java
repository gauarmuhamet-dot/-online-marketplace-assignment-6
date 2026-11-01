
package com.marketplace.observer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class StockSource extends EventPublisher<MarketplaceEvent> {

    private static final int LOW_STOCK_LIMIT = 5;

    private final ConcurrentHashMap<String, Integer> stocks =
            new ConcurrentHashMap<>();

    public void updateStock(String productId, int newStock) {
        Objects.requireNonNull(productId, "Product ID is required");

        if (productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be blank");
        }

        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        stocks.compute(productId, (id, oldStock) -> {
            if (oldStock != null && oldStock == newStock) {
                return oldStock;
            }

            MarketplaceEvent event = new MarketplaceEvent(
                    MarketplaceEvent.Type.STOCK_CHANGED,
                    id,
                    BigDecimal.ZERO,
                    newStock
            );

            notifyObservers(event.getType().name(), event);

            if (newStock <= LOW_STOCK_LIMIT) {
                MarketplaceEvent lowStockEvent = new MarketplaceEvent(
                        MarketplaceEvent.Type.LOW_STOCK,
                        id,
                        BigDecimal.ZERO,
                        newStock
                );

                notifyObservers(
                        lowStockEvent.getType().name(),
                        lowStockEvent
                );
            }

            return newStock;
        });
    }

    public int getStock(String productId) {
        return stocks.getOrDefault(productId, 0);
    }
}
