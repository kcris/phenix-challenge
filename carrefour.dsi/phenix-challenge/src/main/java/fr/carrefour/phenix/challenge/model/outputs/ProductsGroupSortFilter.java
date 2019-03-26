package fr.carrefour.phenix.challenge.model.outputs;

import fr.carrefour.phenix.challenge.domain.products.Product;

import java.util.Comparator;

public interface ProductsGroupSortFilter {
    Comparator<Product> getSoldFieldComparator();
    String getSoldFieldValue(Product product);
}
