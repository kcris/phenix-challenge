package fr.carrefour.phenix.challenge.domain.stats.daily.per_store;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsSalesRepository;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorBySoldValue;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public class TopDailyProductsBySalesValuePerStore extends AbstractTopDailyProductsStatPerStore {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorBySoldValue();

    public TopDailyProductsBySalesValuePerStore(UUID storeId, LocalDate date, int limit, ProductsSalesRepository repository) {
        super(storeId, date, limit, repository);
    }

    @Override
    Comparator<Product> getComparator() {
        return COMPARATOR;
    }
}
