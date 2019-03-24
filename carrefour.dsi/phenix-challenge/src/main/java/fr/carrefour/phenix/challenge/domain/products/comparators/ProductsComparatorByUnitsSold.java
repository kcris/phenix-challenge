package fr.carrefour.phenix.challenge.domain.products.comparators;

import fr.carrefour.phenix.challenge.domain.products.Product;

import java.util.Comparator;

/**
 * compares products by sales = number of units sold (ventes)
 */
public class ProductsComparatorByUnitsSold implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p1.getUnitsSold() - p2.getUnitsSold();
    }
}
