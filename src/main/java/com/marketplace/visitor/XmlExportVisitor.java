
package com.marketplace.visitor;

public final class XmlExportVisitor implements CatalogVisitor<String> {

    @Override
    public String visit(Catalog catalog) {
        StringBuilder xml = new StringBuilder();

        xml.append("<catalog name=\"")
                .append(escape(catalog.getName()))
                .append("\">\n");

        for (Category category : catalog.getCategories()) {
            xml.append(indent(category.accept(this), 1));
        }

        xml.append("</catalog>\n");
        return xml.toString();
    }

    @Override
    public String visit(Category category) {
        StringBuilder xml = new StringBuilder();

        xml.append("<category name=\"")
                .append(escape(category.getName()))
                .append("\">\n");

        for (CatalogElement element : category.getElements()) {
            xml.append(indent(element.accept(this), 1));
        }

        xml.append("</category>\n");
        return xml.toString();
    }

    @Override
    public String visit(PhysicalProduct product) {
        return "<physicalProduct id=\"" + escape(product.getId())
                + "\" name=\"" + escape(product.getName())
                + "\" price=\"" + product.getPrice()
                + "\" weight=\"" + product.getWeight()
                + "\"/>\n";
    }

    @Override
    public String visit(DigitalProduct product) {
        return "<digitalProduct id=\"" + escape(product.getId())
                + "\" name=\"" + escape(product.getName())
                + "\" price=\"" + product.getPrice()
                + "\" fileSizeMB=\"" + product.getFileSizeMB()
                + "\"/>\n";
    }

    @Override
    public String visit(ServiceProduct product) {
        return "<serviceProduct id=\"" + escape(product.getId())
                + "\" name=\"" + escape(product.getName())
                + "\" price=\"" + product.getPrice()
                + "\" durationMinutes=\"" + product.getDurationMinutes()
                + "\"/>\n";
    }

    @Override
    public String visit(SubscriptionProduct product) {
        return "<subscriptionProduct id=\"" + escape(product.getId())
                + "\" name=\"" + escape(product.getName())
                + "\" price=\"" + product.getPrice()
                + "\" durationMonths=\"" + product.getDurationMonths()
                + "\"/>\n";
    }

    @Override
    public String visit(GiftCardProduct product) {
        return "<giftCardProduct id=\"" + escape(product.getId())
                + "\" name=\"" + escape(product.getName())
                + "\" price=\"" + product.getPrice()
                + "\"/>\n";
    }

    private String escape(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String indent(String text, int level) {
        String spaces = "    ".repeat(level);

        return text.lines()
                .map(line -> spaces + line + "\n")
                .collect(java.util.stream.Collectors.joining());
    }
}
