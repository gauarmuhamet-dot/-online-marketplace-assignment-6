
package com.marketplace.visitor;

import java.math.BigDecimal;

public final class CatalogStatisticsVisitor
        implements CatalogVisitor<Void> {

    private int physicalCount;
    private int digitalCount;
    private int serviceCount;
    private int subscriptionCount;

    private BigDecimal totalValue = BigDecimal.ZERO;

    @Override
    public Void visit(Catalog catalog) {
        for (Category category : catalog.getCategories()) {
            category.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Category category) {
        for (CatalogElement element : category.getElements()) {
            element.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(PhysicalProduct product) {
        physicalCount++;
        totalValue = totalValue.add(product.getPrice());
        return null;
    }

    @Override
    public Void visit(DigitalProduct product) {
        digitalCount++;
        totalValue = totalValue.add(product.getPrice());
        return null;
    }

    @Override
    public Void visit(ServiceProduct product) {
        serviceCount++;
        totalValue = totalValue.add(product.getPrice());
        return null;
    }

    @Override
    public Void visit(SubscriptionProduct product) {
        subscriptionCount++;
        totalValue = totalValue.add(product.getPrice());
        return null;
    }

    public int getPhysicalCount() {
        return physicalCount;
    }

    public int getDigitalCount() {
        return digitalCount;
    }

    public int getServiceCount() {
        return serviceCount;
    }

    public int getSubscriptionCount() {
        return subscriptionCount;
    }

    public int getTotalProductCount() {
        return physicalCount + digitalCount
                + serviceCount + subscriptionCount;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void reset() {
        physicalCount = 0;
        digitalCount = 0;
        serviceCount = 0;
        subscriptionCount = 0;
        totalValue = BigDecimal.ZERO;
    }
}
