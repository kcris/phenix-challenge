package fr.carrefour.phenix.challenge.domain.stats.daily.per_store;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournal;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournalsRepository;
import fr.carrefour.phenix.challenge.domain.stats.ProductsStat;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public abstract class AbstractTopDailyProductsStatPerStore implements ProductsStat {

    private UUID storeId;
    private LocalDate date;
    private int limit;
    private ProductsJournalsRepository repository;

    public AbstractTopDailyProductsStatPerStore(UUID storeId, LocalDate date, int limit, ProductsJournalsRepository repository) {
        this.storeId = storeId;
        this.date = date;
        this.limit = limit;
        this.repository = repository;
    }

    @Override
    public ProductsGroup getStatistics() {

        //get store products
        ProductsJournal journal = repository.getJournal(date);
        ProductsGroup storeProducts = journal.getStoreProducts(storeId);

        //compute top
        ProductsGroup topProducts = storeProducts.top(limit, getComparator());
        return topProducts;
    }

    abstract Comparator<Product> getComparator();
}
