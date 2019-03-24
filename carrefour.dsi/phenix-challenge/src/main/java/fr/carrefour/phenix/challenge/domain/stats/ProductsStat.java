package fr.carrefour.phenix.challenge.domain.stats;

import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;

/**
 * strategy/algorithm for computing a specific statistic about existing products
 */
public interface ProductsStat {
    ProductsGroup getStatistics();
}
