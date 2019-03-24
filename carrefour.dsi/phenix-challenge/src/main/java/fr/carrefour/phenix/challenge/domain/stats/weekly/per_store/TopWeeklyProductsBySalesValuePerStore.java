package fr.carrefour.phenix.challenge.domain.stats.weekly.per_store;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournalsRepository;
import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.products.aggregators.ProductAggregatorByUnitsSold;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorByValueSold;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public class TopWeeklyProductsBySalesValuePerStore extends AbstractTopWeeklyProductsStatPerStore {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorByValueSold();

    private static final Aggregator<Product> AGGREGATOR = new ProductAggregatorByUnitsSold();

    public TopWeeklyProductsBySalesValuePerStore(UUID storeId, LocalDate date, int limit, ProductsJournalsRepository repository) {
        super(storeId, date, limit, repository);
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
