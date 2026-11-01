
package com.marketplace.listeners;

import com.marketplace.observer.MarketplaceEvent;
import com.marketplace.observer.Observer;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheListener implements Observer<MarketplaceEvent> {

    private final ConcurrentHashMap<String, BigDecimal> priceCache =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Integer> stockCache =
            new ConcurrentHashMap<>();

    @Override
    public void onEvent(MarketplaceEvent event) {
        switch (event.getType()) {
            case PRICE_CHANGED, PRICE_DROP ->
                    priceCache.put(
                            event.getProductId(),
                            event.getPrice()
                    );

            case STOCK_CHANGED, LOW_STOCK ->
                    stockCache.put(
                            event.getProductId(),
                            event.getStock()
                    );

            case ANALYTICS_UPDATED -> {
                // Analytics events do not change product data.
            }
        }
    }

    public BigDecimal getCachedPrice(String productId) {
        return priceCache.get(productId);
    }

    public int getCachedStock(String productId) {
        return stockCache.getOrDefault(productId, 0);
    }

    public int getCachedProductCount() {
        return priceCache.size();
    }
}
