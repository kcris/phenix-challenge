package fr.carrefour.phenix.challenge.domain.products;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * holds products journals for multiple days
 *
 * for each day, we have all information about all products in all stores
 */
public class ProductsJournalsRepository {

    private Map<LocalDate, ProductsJournal> dailyJournals = new ConcurrentHashMap<>(); //all products in all stores for a given date

    public void addJournal(LocalDate date, ProductsJournal products) {
        this.dailyJournals.put(date, products);
    }

    public ProductsJournal getJournal(LocalDate date) {
        return dailyJournals.get(date);
    }
}
