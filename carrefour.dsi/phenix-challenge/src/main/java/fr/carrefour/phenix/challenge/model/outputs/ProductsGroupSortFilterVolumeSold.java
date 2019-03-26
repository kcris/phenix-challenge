package fr.carrefour.phenix.challenge.model.outputs;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorBySoldVolume;

import java.util.Comparator;

public class ProductsGroupSortFilterVolumeSold implements ProductsGroupSortFilter {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorBySoldVolume();

    @Override
    public Comparator<Product> getSoldFieldComparator() {
        return COMPARATOR;
    }

    @Override
    public String getSoldFieldValue(Product product) {
        return String.valueOf(product.getUnitsSold());
    }
}
