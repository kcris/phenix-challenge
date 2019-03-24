package fr.carrefour.phenix.challenge.model.outputs;

import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;

public interface ProductsGroupSaver {
    void saveProducts(ProductsGroup products, String title);
}
