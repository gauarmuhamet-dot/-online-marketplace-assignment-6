
package com.marketplace.observer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class PriceSource extends EventPublisher<MarketplaceEvent> {

    private final ConcurrentHashMap<String, BigDecimal> prices =
            new ConcurrentHashMap<>();

    public void updatePrice(String productId, BigDecimal newPrice) {
        Objects.requireNonNull(productId, "Product ID is required");
        Objects.requireNonNull(newPrice, "Price is required");

        if (productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be blank");
        }

        if (newPrice.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        prices.compute(productId, (id, oldPrice) -> {
            if (newPrice.compareTo(oldPrice == null
                    ? newPrice : oldPrice) == 0 && oldPrice != null) {
                return oldPrice;
            }

            MarketplaceEvent event = new MarketplaceEvent(
                    MarketplaceEvent.Type.PRICE_CHANGED,
                    id,
                    newPrice,
                    0
            );

            notifyObservers(event.getType().name(), event);

            if (oldPrice != null && newPrice.compareTo(oldPrice) < 0) {
                MarketplaceEvent dropEvent = new MarketplaceEvent(
                        MarketplaceEvent.Type.PRICE_DROP,
                        id,
                        newPrice,
                        0
                );

                notifyObservers(dropEvent.getType().name(), dropEvent);
            }

            return newPrice;
        });
    }

    public BigDecimal getPrice(String productId) {
        return prices.get(productId);
    }
}
