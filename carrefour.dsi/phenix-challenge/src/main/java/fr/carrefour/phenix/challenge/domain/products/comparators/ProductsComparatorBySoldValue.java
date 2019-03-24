package fr.carrefour.phenix.challenge.domain.products.comparators;

import fr.carrefour.phenix.challenge.domain.products.Product;

import java.util.Comparator;

/**
 * compares products by sold value (chiffres affaires)
 */
public class ProductsComparatorBySoldValue implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {

        float income1 = p1.getValueSold();
        float income2 = p2.getValueSold();

        if (income1 < income2)
            return -1;
        if (income1 > income2)
            return 1;
        return 0;
    }
}
