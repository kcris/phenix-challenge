package fr.carrefour.phenix.challenge.domain.products.comparators;

import fr.carrefour.phenix.challenge.domain.products.Product;

import java.util.Comparator;

/**
 * compares products by business figures = units sold x unit price (chiffres affaires)
 */
public class ProductsComparatorByValueSold implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {

        float income1 = p1.getUnitsSold() * p1.getUnitPrice();
        float income2 = p2.getUnitsSold() * p2.getUnitPrice();

        if (income1 < income2)
            return -1;
        if (income1 > income2)
            return 1;
        return 0;
    }
}
