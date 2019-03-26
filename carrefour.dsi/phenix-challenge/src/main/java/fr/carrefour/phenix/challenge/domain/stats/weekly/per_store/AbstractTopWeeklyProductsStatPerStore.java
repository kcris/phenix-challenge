package fr.carrefour.phenix.challenge.domain.stats.weekly.per_store;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsSalesRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.stats.ProductsStat;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public abstract class AbstractTopWeeklyProductsStatPerStore implements ProductsStat {

    private UUID storeId;
    private LocalDate date;
    private int limit;
    private ProductsSalesRepository repository;

    public AbstractTopWeeklyProductsStatPerStore(UUID storeId, LocalDate date, int limit, ProductsSalesRepository repository) {
        this.storeId = storeId;
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

            ProductsGroup products = repository.getProducts(storeId, dt);
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
