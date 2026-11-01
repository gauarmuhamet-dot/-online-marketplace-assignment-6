
package com.marketplace.visitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ProductFilterVisitor
        implements CatalogVisitor<List<CatalogElement>> {

    private final BigDecimal maxPrice;

    public ProductFilterVisitor(BigDecimal maxPrice) {
        this.maxPrice = Objects.requireNonNull(
                maxPrice, "Maximum price is required"
        );

        if (maxPrice.signum() < 0) {
            throw new IllegalArgumentException(
                    "Maximum price cannot be negative"
            );
        }
    }

    @Override
    public List<CatalogElement> visit(Catalog catalog) {
        List<CatalogElement> results = new ArrayList<>();

        for (Category category : catalog.getCategories()) {
            results.addAll(category.accept(this));
        }

        return List.copyOf(results);
    }

    @Override
    public List<CatalogElement> visit(Category category) {
        List<CatalogElement> results = new ArrayList<>();

        for (CatalogElement element : category.getElements()) {
            results.addAll(element.accept(this));
        }

        return List.copyOf(results);
    }

    @Override
    public List<CatalogElement> visit(PhysicalProduct product) {
        return filter(product, product.getPrice());
    }

    @Override
    public List<CatalogElement> visit(DigitalProduct product) {
        return filter(product, product.getPrice());
    }

    @Override
    public List<CatalogElement> visit(ServiceProduct product) {
        return filter(product, product.getPrice());
    }

    @Override
    public List<CatalogElement> visit(SubscriptionProduct product) {
        return filter(product, product.getPrice());
    }

    private List<CatalogElement> filter(
            CatalogElement product,
            BigDecimal price
    ) {
        if (price.compareTo(maxPrice) <= 0) {
            return List.of(product);
        }

        return List.of();
    }
}
