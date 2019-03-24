package fr.carrefour.phenix.challenge.domain.products.aggregators;

import fr.carrefour.phenix.challenge.domain.products.Product;

/**
 * merge two products by creating a new product whose sales volume is the sum of the sales volumes of our inputs
 */
public class ProductAggregatorByUnitsSold implements Aggregator<Product> {

    @Override
    public Product apply(Product v1, Product v2) {

        assert v1.getProductId() == v2.getProductId(); //make sure both instances are for the same product

        return new Product(v1.getProductId(), v1.getUnitsSold() + v2.getUnitsSold(), v1.getUnitPrice());
    }
}
