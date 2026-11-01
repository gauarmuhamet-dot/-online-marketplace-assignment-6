
package com.marketplace.listeners;

import com.marketplace.observer.MarketplaceEvent;
import com.marketplace.observer.Observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class StockAlertListener implements Observer<MarketplaceEvent> {

    private final CopyOnWriteArrayList<String> alerts =
            new CopyOnWriteArrayList<>();

    @Override
    public void onEvent(MarketplaceEvent event) {
        if (event.getType() != MarketplaceEvent.Type.LOW_STOCK) {
            return;
        }

        String alert = "Low stock alert: Product "
                + event.getProductId()
                + " has only "
                + event.getStock()
                + " items remaining.";

        alerts.add(alert);
        System.out.println(alert);
    }

    public List<String> getAlerts() {
        return List.copyOf(alerts);
    }

    public int getAlertCount() {
        return alerts.size();
    }
}
