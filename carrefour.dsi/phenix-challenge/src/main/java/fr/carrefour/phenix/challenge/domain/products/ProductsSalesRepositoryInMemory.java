package fr.carrefour.phenix.challenge.domain.products;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * holds products journals for multiple days in memory
 *
 * for each day, we have all information about all products in all stores
 */
public class ProductsSalesRepositoryInMemory implements ProductsSalesRepository {

    private Map<LocalDate, ProductsJournal> dailyJournals = new ConcurrentHashMap<>(); //all products in all stores for a given date

    @Override
    public void addProducts(UUID storeId, LocalDate date, ProductsGroup products) {
        ProductsJournal journal = new ProductsJournal();
        journal.addStoreProducts(storeId, products);
        this.dailyJournals.put(date, journal);
    }

    @Override
    public ProductsGroup getProducts(UUID storeId, LocalDate date) {
        ProductsJournal journal = dailyJournals.get(date);
        return journal.getStoreProducts(storeId);
    }

    @Override
    public Collection<UUID> getStores(LocalDate date) {
        return dailyJournals.get(date).getStores();
    }

}
