
package com.marketplace.visitor;

import java.math.BigDecimal;
import java.util.Objects;

public final class DigitalProduct implements CatalogElement {

    private final String id;
    private final String name;
    private final BigDecimal price;
    private final double fileSizeMB;

    public DigitalProduct(String id, String name,
                          BigDecimal price, double fileSizeMB) {
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

        if (!Double.isFinite(fileSizeMB) || fileSizeMB < 0) {
            throw new IllegalArgumentException(
                    "File size must be a non-negative finite number"
            );
        }

        this.fileSizeMB = fileSizeMB;
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

    public double getFileSizeMB() {
        return fileSizeMB;
    }

    @Override
    public <R> R accept(CatalogVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
