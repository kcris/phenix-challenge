package fr.carrefour.phenix.challenge.domain.stats.daily.per_store;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsSalesRepository;
import fr.carrefour.phenix.challenge.domain.stats.ProductsStat;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public abstract class AbstractTopDailyProductsStatPerStore implements ProductsStat {

    private UUID storeId;
    private LocalDate date;
    private int limit;
    private ProductsSalesRepository repository;

    public AbstractTopDailyProductsStatPerStore(UUID storeId, LocalDate date, int limit, ProductsSalesRepository repository) {
        this.storeId = storeId;
        this.date = date;
        this.limit = limit;
        this.repository = repository;
    }

    @Override
    public ProductsGroup getStatistics() {

        //get store products
        ProductsGroup storeProducts = repository.getProducts(storeId, date);

        //compute top
        ProductsGroup topProducts = storeProducts.top(limit, getComparator());
        return topProducts;
    }

    abstract Comparator<Product> getComparator();
}
