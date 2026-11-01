package com.marketplace.visitor;

public interface CatalogElement {

    <R> R accept(CatalogVisitor<R> visitor);
}
