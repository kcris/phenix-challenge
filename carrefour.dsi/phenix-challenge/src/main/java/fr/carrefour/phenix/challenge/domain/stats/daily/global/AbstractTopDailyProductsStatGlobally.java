package fr.carrefour.phenix.challenge.domain.stats.daily.global;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsSalesRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.stats.ProductsStat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;

public abstract class AbstractTopDailyProductsStatGlobally implements ProductsStat {

    private LocalDate date;
    private int limit;
    private ProductsSalesRepository repository;

    public AbstractTopDailyProductsStatGlobally(LocalDate date, int limit, ProductsSalesRepository repository) {
        this.date = date;
        this.limit = limit;
        this.repository = repository;
    }

    @Override
    public ProductsGroup getStatistics() {

        //get all stores' products - map-reduce
        ProductsGroup mergedProducts = new ProductsGroup();
        for (UUID storeId : repository.getStores(date)) {
            ProductsGroup prod = repository.getProducts(storeId, date);
            mergedProducts.merge(prod, getAggregator());
        }

        //compute top
        ProductsGroup topProducts = mergedProducts.top(limit, getComparator());
        return topProducts;
    }

    abstract Comparator<Product> getComparator();

    abstract Aggregator<Product> getAggregator();
}
