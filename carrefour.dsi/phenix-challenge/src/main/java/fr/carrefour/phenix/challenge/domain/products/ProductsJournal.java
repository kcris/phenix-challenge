package fr.carrefour.phenix.challenge.domain.products;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Daily products information: all products in all stores, at a given date
 */
public class ProductsJournal {

    private LocalDate date;
    private final Map<UUID, ProductsGroup> storesProducts = new HashMap<>(); //store id -> (productId -> product)

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<UUID, ProductsGroup> getAllProducts() {
        return storesProducts;
    }

    public ProductsGroup getStoreProducts(UUID storeId) {
        return storesProducts.get(storeId);
    }

    void addStoreProducts(UUID storeId, ProductsGroup products) {
        storesProducts.put(storeId, products);
    }

    public Set<UUID> getStores() {
        Set<UUID> stores = getAllProducts().keySet();
        return stores;
    }
}
