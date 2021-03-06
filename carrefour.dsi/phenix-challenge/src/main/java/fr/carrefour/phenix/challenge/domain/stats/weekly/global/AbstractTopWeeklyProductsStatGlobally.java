package fr.carrefour.phenix.challenge.domain.stats.weekly.global;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournal;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournalsRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.stats.ProductsStat;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public abstract class AbstractTopWeeklyProductsStatGlobally implements ProductsStat {

    private LocalDate date;
    private int limit;
    private ProductsJournalsRepository repository;

    public AbstractTopWeeklyProductsStatGlobally(LocalDate date, int limit, ProductsJournalsRepository repository) {
        this.date = date;
        this.limit = limit;
        this.repository = repository;
    }

    @Override
    public ProductsGroup getStatistics() {

        //get already computed daily products for the past 7 days and merge them
        ProductsGroup mergedProducts = null;

        for (int i = 0; i <= 6; ++i) {
            LocalDate dt = date.minusDays(i);

            ProductsJournal journal = repository.getJournal(dt);

            for(UUID storeId : journal.getStores())
            {
                ProductsGroup products = journal.getStoreProducts(storeId);
                if (mergedProducts == null)
                    mergedProducts = products;
                else
                    mergedProducts = mergedProducts.merge(products, getAggregator());
            }
        }

        //compute top
        ProductsGroup topProducts = mergedProducts.top(limit, getComparator());
        return topProducts;
    }

    abstract Comparator<Product> getComparator();

    abstract Aggregator<Product> getAggregator();
}
