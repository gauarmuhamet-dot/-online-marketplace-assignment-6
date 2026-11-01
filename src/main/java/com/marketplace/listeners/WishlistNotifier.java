
package com.marketplace.listeners;

import com.marketplace.observer.MarketplaceEvent;
import com.marketplace.observer.Observer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public final class WishlistNotifier implements Observer<MarketplaceEvent> {

    private final Set<String> wishlist =
            ConcurrentHashMap.newKeySet();

    private final CopyOnWriteArrayList<String> notifications =
            new CopyOnWriteArrayList<>();

    public void addToWishlist(String productId) {
        wishlist.add(productId);
    }

    public void removeFromWishlist(String productId) {
        wishlist.remove(productId);
    }

    @Override
    public void onEvent(MarketplaceEvent event) {
        if (event.getType() != MarketplaceEvent.Type.PRICE_DROP) {
            return;
        }

        if (wishlist.contains(event.getProductId())) {
            String notification =
                    "Wishlist notification: Product "
                            + event.getProductId()
                            + " price dropped to "
                            + event.getPrice()
                            + " KZT";

            notifications.add(notification);
            System.out.println(notification);
        }
    }

    public List<String> getNotifications() {
        return List.copyOf(notifications);
    }

    public int getNotificationCount() {
        return notifications.size();
    }
}
