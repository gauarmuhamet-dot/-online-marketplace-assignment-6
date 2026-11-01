
package com.marketplace.visitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ShippingVisitor implements CatalogVisitor<BigDecimal> {

    private static final BigDecimal BASE_SHIPPING =
            new BigDecimal("500");

    private static final BigDecimal PRICE_PER_KG =
            new BigDecimal("200");

    @Override
    public BigDecimal visit(Catalog catalog) {
        BigDecimal total = BigDecimal.ZERO;

        for (Category category : catalog.getCategories()) {
            total = total.add(category.accept(this));
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal visit(Category category) {
        BigDecimal total = BigDecimal.ZERO;

        for (CatalogElement element : category.getElements()) {
            total = total.add(element.accept(this));
        }

        return total;
    }

    @Override
    public BigDecimal visit(PhysicalProduct product) {
        BigDecimal weightCost = BigDecimal.valueOf(product.getWeight())
                .multiply(PRICE_PER_KG);

        return BASE_SHIPPING.add(weightCost)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal visit(DigitalProduct product) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal visit(ServiceProduct product) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal visit(SubscriptionProduct product) {
        return BigDecimal.ZERO;
    }
}
