package fr.carrefour.phenix.challenge.domain.stats.daily.global;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsSalesRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.stats.ProductsStat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;

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

        //get all stores' products
        Collection<ProductsGroup> allStoresProducts = repository.getProducts(date);

        //merge all stores' products
        ProductsGroup mergedProducts = null;
        for(ProductsGroup products : allStoresProducts) {
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
