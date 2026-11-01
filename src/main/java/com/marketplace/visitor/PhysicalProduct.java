
package com.marketplace.visitor;

import java.math.BigDecimal;
import java.util.Objects;

public final class PhysicalProduct implements CatalogElement {

    private final String id;
    private final String name;
    private final BigDecimal price;
    private final double weight;

    public PhysicalProduct(String id, String name,
                           BigDecimal price, double weight) {
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

        if (!Double.isFinite(weight) || weight < 0) {
            throw new IllegalArgumentException(
                    "Weight must be a non-negative finite number"
            );
        }

        this.weight = weight;
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

    public double getWeight() {
        return weight;
    }

    @Override
    public <R> R accept(CatalogVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
