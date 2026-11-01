
package com.marketplace.listeners;

import com.marketplace.observer.EventPublisher;
import com.marketplace.observer.MarketplaceEvent;
import com.marketplace.observer.Observer;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public final class AnalyticsListener
        extends EventPublisher<MarketplaceEvent>
        implements Observer<MarketplaceEvent> {

    private final AtomicInteger priceChanges = new AtomicInteger();
    private final AtomicInteger stockChanges = new AtomicInteger();
    private final AtomicInteger priceDrops = new AtomicInteger();
    private final AtomicInteger lowStockEvents = new AtomicInteger();

    @Override
    public void onEvent(MarketplaceEvent event) {
        switch (event.getType()) {
            case PRICE_CHANGED -> priceChanges.incrementAndGet();
            case STOCK_CHANGED -> stockChanges.incrementAndGet();
            case PRICE_DROP -> priceDrops.incrementAndGet();
            case LOW_STOCK -> lowStockEvents.incrementAndGet();
            case ANALYTICS_UPDATED -> {
                return;
            }
        }

        int totalEvents = getTotalEvents();

        MarketplaceEvent analyticsEvent = new MarketplaceEvent(
                MarketplaceEvent.Type.ANALYTICS_UPDATED,
                event.getProductId(),
                BigDecimal.valueOf(totalEvents),
                totalEvents
        );

        notifyObservers(
                analyticsEvent.getType().name(),
                analyticsEvent
        );
    }

    public int getPriceChanges() {
        return priceChanges.get();
    }

    public int getStockChanges() {
        return stockChanges.get();
    }

    public int getPriceDrops() {
        return priceDrops.get();
    }

    public int getLowStockEvents() {
        return lowStockEvents.get();
    }

    public int getTotalEvents() {
        return priceChanges.get()
                + stockChanges.get()
                + priceDrops.get()
                + lowStockEvents.get();
    }
}
