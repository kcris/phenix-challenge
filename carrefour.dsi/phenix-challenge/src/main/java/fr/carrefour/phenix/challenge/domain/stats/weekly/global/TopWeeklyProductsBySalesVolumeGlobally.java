package fr.carrefour.phenix.challenge.domain.stats.weekly.global;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsSalesRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.products.aggregators.ProductAggregatorSumSales;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorBySoldVolume;

import java.time.LocalDate;
import java.util.Comparator;

public class TopWeeklyProductsBySalesVolumeGlobally extends AbstractTopWeeklyProductsStatGlobally {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorBySoldVolume();

    private static final Aggregator<Product> AGGREGATOR = new ProductAggregatorSumSales();

    public TopWeeklyProductsBySalesVolumeGlobally(LocalDate date, int limit, ProductsSalesRepository repository) {
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


