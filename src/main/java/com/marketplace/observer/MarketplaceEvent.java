
package com.marketplace.observer;

import java.math.BigDecimal;
import java.util.Objects;

public final class MarketplaceEvent {

    public enum Type {
        PRICE_CHANGED,
        STOCK_CHANGED,
        PRICE_DROP,
        LOW_STOCK,
        ANALYTICS_UPDATED
    }

    private final Type type;
    private final String productId;
    private final BigDecimal price;
    private final int stock;

    public MarketplaceEvent(Type type, String productId,
                            BigDecimal price, int stock) {
        this.type = Objects.requireNonNull(type);
        this.productId = Objects.requireNonNull(productId);
        this.price = Objects.requireNonNull(price);
        this.stock = stock;
    }

    public Type getType() {
        return type;
    }

    public String getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "MarketplaceEvent{" +
                "type=" + type +
                ", productId='" + productId + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
