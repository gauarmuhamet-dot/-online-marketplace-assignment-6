
package com.marketplace.visitor;

import java.util.List;
import java.util.Objects;

public final class Category implements CatalogElement {

    private final String name;
    private final List<CatalogElement> elements;

    public Category(String name, List<CatalogElement> elements) {
        this.name = Objects.requireNonNull(name, "Name is required");
        this.elements = List.copyOf(
                Objects.requireNonNull(elements, "Elements are required")
        );
    }

    public String getName() {
        return name;
    }

    public List<CatalogElement> getElements() {
        return elements;
    }

    @Override
    public <R> R accept(CatalogVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
