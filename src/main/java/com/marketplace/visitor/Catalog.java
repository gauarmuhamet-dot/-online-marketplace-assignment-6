
package com.marketplace.visitor;

import java.util.List;
import java.util.Objects;

public final class Catalog implements CatalogElement {

    private final String name;
    private final List<Category> categories;

    public Catalog(String name, List<Category> categories) {
        this.name = Objects.requireNonNull(name, "Name is required");
        this.categories = List.copyOf(
                Objects.requireNonNull(categories, "Categories are required")
        );
    }

    public String getName() {
        return name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public <R> R accept(CatalogVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
