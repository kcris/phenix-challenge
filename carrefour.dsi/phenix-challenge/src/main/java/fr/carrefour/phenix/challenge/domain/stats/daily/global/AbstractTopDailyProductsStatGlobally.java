package fr.carrefour.phenix.challenge.domain.stats.daily.global;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournal;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournalsRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.stats.ProductsStat;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractTopDailyProductsStatGlobally implements ProductsStat {

    private LocalDate date;
    private int limit;
    private ProductsJournalsRepository repository;

    public AbstractTopDailyProductsStatGlobally(LocalDate date, int limit, ProductsJournalsRepository repository) {
        this.date = date;
        this.limit = limit;
        this.repository = repository;
    }

    @Override
    public ProductsGroup getStatistics() {

        //get all stores' products
        ProductsJournal journal = repository.getJournal(date);
        Map<UUID, ProductsGroup> allStoresProducts = journal.getAllProducts();

        //merge all stores' products
        ProductsGroup mergedProducts = null;
        for(ProductsGroup products : allStoresProducts.values()) {
            if (mergedProducts == null)
                mergedProducts = products;
            else
                mergedProducts = mergedProducts.merge(products, getAggregator());
        }

        //compute top
        ProductsGroup topProducts = mergedProducts.top(limit, getComparator());
        return topProducts;
    }

    abstract Comparator<Product> getComparator();

    abstract Aggregator<Product> getAggregator();
}
