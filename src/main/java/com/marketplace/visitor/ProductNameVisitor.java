
package com.marketplace.visitor;

import java.util.ArrayList;
import java.util.List;

public final class ProductNameVisitor
        implements CatalogVisitor<List<String>> {

    @Override
    public List<String> visit(Catalog catalog) {
        List<String> names = new ArrayList<>();

        for (Category category : catalog.getCategories()) {
            names.addAll(category.accept(this));
        }

        return names;
    }

    @Override
    public List<String> visit(Category category) {
        List<String> names = new ArrayList<>();

        for (CatalogElement element : category.getElements()) {
            names.addAll(element.accept(this));
        }

        return names;
    }

    @Override
    public List<String> visit(PhysicalProduct product) {
        return List.of(product.getName());
    }

    @Override
    public List<String> visit(DigitalProduct product) {
        return List.of(product.getName());
    }

    @Override
    public List<String> visit(ServiceProduct product) {
        return List.of(product.getName());
    }

    @Override
    public List<String> visit(SubscriptionProduct product) {
        return List.of(product.getName());
    }
}
