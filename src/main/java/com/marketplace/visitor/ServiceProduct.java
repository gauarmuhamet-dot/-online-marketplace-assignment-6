
package com.marketplace.visitor;

import java.math.BigDecimal;
import java.util.Objects;

public final class ServiceProduct implements CatalogElement {

    private final String id;
    private final String name;
    private final BigDecimal price;
    private final int durationMinutes;

    public ServiceProduct(String id, String name,
                          BigDecimal price, int durationMinutes) {
        this.id = Objects.requireNonNull(id, "ID is required");
        this.name = Objects.requireNonNull(name, "Name is required");
        this.price = Objects.requireNonNull(price, "Price is required");

        if (id.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException(
                    "ID and name cannot be blank"
            );
        }

        if (price.signum() < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative"
            );
        }

        if (durationMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Duration must be positive"
            );
        }

        this.durationMinutes = durationMinutes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public <R> R accept(CatalogVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
