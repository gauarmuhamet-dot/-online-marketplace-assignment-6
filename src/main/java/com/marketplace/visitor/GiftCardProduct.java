
package com.marketplace.visitor;

import java.math.BigDecimal;
import java.util.Objects;

public final class GiftCardProduct implements CatalogElement {

    private final String id;
    private final String name;
    private final BigDecimal price;

    public GiftCardProduct(String id, String name, BigDecimal price) {
        this.id = Objects.requireNonNull(id, "ID is required");
        this.name = Objects.requireNonNull(name, "Name is required");
        this.price = Objects.requireNonNull(price, "Price is required");

        if (id.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException("ID and name cannot be blank");
        }

        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
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

    @Override
    public <R> R accept(CatalogVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
