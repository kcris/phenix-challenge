package fr.carrefour.phenix.challenge.domain.products;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * holds products journals for multiple days
 *
 * for each day, we have all information about all products in all stores
 */
public interface ProductsSalesRepository {

    void addProducts(UUID storeId, LocalDate date, ProductsGroup products);
    ProductsGroup getProducts(UUID storeId, LocalDate date);
    Collection<UUID> getStores(LocalDate date);
    Collection<ProductsGroup> getProducts(LocalDate date);
}
