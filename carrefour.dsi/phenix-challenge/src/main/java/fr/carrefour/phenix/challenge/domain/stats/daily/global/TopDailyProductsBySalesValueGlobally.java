package fr.carrefour.phenix.challenge.domain.stats.daily.global;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsSalesRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.products.aggregators.ProductAggregatorSumSales;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorBySoldValue;

import java.time.LocalDate;
import java.util.Comparator;

public class TopDailyProductsBySalesValueGlobally extends AbstractTopDailyProductsStatGlobally {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorBySoldValue();

    private static final Aggregator<Product> AGGREGATOR = new ProductAggregatorSumSales();

    public TopDailyProductsBySalesValueGlobally(LocalDate date, int limit, ProductsSalesRepository repository) {
        super(date, limit, repository);
    }

    @Override
    Comparator<Product> getComparator() {
        return COMPARATOR;
    }

    @Override
    Aggregator<Product> getAggregator() {
        return AGGREGATOR;
    }
}
