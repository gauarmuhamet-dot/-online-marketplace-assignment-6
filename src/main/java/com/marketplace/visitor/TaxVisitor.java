
package com.marketplace.visitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class TaxVisitor implements CatalogVisitor<BigDecimal> {

    private static final BigDecimal PHYSICAL_TAX =
            new BigDecimal("0.12");

    private static final BigDecimal DIGITAL_TAX =
            new BigDecimal("0.10");

    private static final BigDecimal SERVICE_TAX =
            new BigDecimal("0.05");

    private static final BigDecimal SUBSCRIPTION_TAX =
            new BigDecimal("0.08");

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
        return calculateTax(product.getPrice(), PHYSICAL_TAX);
    }

    @Override
    public BigDecimal visit(DigitalProduct product) {
        return calculateTax(product.getPrice(), DIGITAL_TAX);
    }

    @Override
    public BigDecimal visit(ServiceProduct product) {
        return calculateTax(product.getPrice(), SERVICE_TAX);
    }

    @Override
    public BigDecimal visit(SubscriptionProduct product) {
        return calculateTax(product.getPrice(), SUBSCRIPTION_TAX);
    }

    private BigDecimal calculateTax(BigDecimal price, BigDecimal rate) {
        return price.multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
