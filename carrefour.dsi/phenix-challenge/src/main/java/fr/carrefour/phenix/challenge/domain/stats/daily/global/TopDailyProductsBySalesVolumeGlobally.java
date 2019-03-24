package fr.carrefour.phenix.challenge.domain.stats.daily.global;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournalsRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.products.aggregators.ProductAggregatorByUnitsSold;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorByUnitsSold;

import java.time.LocalDate;
import java.util.Comparator;

public class TopDailyProductsBySalesVolumeGlobally extends AbstractTopDailyProductsStatGlobally {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorByUnitsSold();

    private static final Aggregator<Product> AGGREGATOR = new ProductAggregatorByUnitsSold();

    public TopDailyProductsBySalesVolumeGlobally(LocalDate date, int limit, ProductsJournalsRepository repository) {
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
