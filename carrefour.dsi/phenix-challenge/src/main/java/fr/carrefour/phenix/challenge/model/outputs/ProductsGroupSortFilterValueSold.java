package fr.carrefour.phenix.challenge.model.outputs;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorBySoldValue;

import java.util.Comparator;

public class ProductsGroupSortFilterValueSold implements ProductsGroupSortFilter {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorBySoldValue();

    @Override
    public Comparator<Product> getSoldFieldComparator() {
        return COMPARATOR;
    }

    @Override
    public String getSoldFieldValue(Product product) {
        return String.valueOf(product.getValueSold());
    }
}
