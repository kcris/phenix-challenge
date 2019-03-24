package fr.carrefour.phenix.challenge.domain.products.aggregators;

import fr.carrefour.phenix.challenge.domain.products.Product;

/**
 * merge two products by creating a new product whose sales volume is the sum of the sales volumes of our inputs
 *
 * this can be used to generate statistics based on merged journals as long as the two product instances
 * belong to the same product
 *
 * the merged product will sum both
 *  - the units sold and
 *  - the value sold
 */
public class ProductAggregatorSumSales implements Aggregator<Product> {

    @Override
    public Product apply(Product v1, Product v2) {

        assert v1.getProductId() == v2.getProductId(); //make sure both instances are for the same product

        int totalUnitsSold = v1.getUnitsSold() + v2.getUnitsSold();
        float totalValueSold = v1.getValueSold() + v2.getValueSold();

        return new Product(v1.getProductId(), totalUnitsSold, totalValueSold);
    }
}
