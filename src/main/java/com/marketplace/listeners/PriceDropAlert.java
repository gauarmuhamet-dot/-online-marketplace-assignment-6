
package com.marketplace.listeners;

import com.marketplace.observer.MarketplaceEvent;
import com.marketplace.observer.Observer;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PriceDropAlert implements Observer<MarketplaceEvent> {

    private final BigDecimal targetPrice;
    private final CopyOnWriteArrayList<String> alerts =
            new CopyOnWriteArrayList<>();

    public PriceDropAlert(BigDecimal targetPrice) {
        if (targetPrice == null || targetPrice.signum() < 0) {
            throw new IllegalArgumentException(
                    "Target price must be non-negative"
            );
        }

        this.targetPrice = targetPrice;
    }

    @Override
    public void onEvent(MarketplaceEvent event) {
        if (event.getType() != MarketplaceEvent.Type.PRICE_DROP) {
            return;
        }

        if (event.getPrice().compareTo(targetPrice) <= 0) {
            String alert = "Price alert: Product "
                    + event.getProductId()
                    + " now costs "
                    + event.getPrice()
                    + " KZT (target: "
                    + targetPrice
                    + " KZT)";

            alerts.add(alert);
            System.out.println(alert);
        }
    }

    public List<String> getAlerts() {
        return List.copyOf(alerts);
    }

    public int getAlertCount() {
        return alerts.size();
    }
}
