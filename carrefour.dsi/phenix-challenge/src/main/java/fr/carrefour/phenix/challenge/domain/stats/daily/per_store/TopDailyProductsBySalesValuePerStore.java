package fr.carrefour.phenix.challenge.domain.stats.daily.per_store;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournalsRepository;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorByValueSold;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public class TopDailyProductsBySalesValuePerStore extends AbstractTopDailyProductsStatPerStore {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorByValueSold();

    public TopDailyProductsBySalesValuePerStore(UUID storeId, LocalDate date, int limit, ProductsJournalsRepository repository) {
        super(storeId, date, limit, repository);
    }

    @Override
    Comparator<Product> getComparator() {
        return COMPARATOR;
    }
}
