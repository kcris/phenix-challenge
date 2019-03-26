package fr.carrefour.phenix.challenge.domain.stats.daily.per_store;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsSalesRepository;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorBySoldVolume;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public class TopDailyProductsBySalesVolumePerStore extends AbstractTopDailyProductsStatPerStore {

    private static final Comparator<Product> COMPARATOR = new ProductsComparatorBySoldVolume();

    public TopDailyProductsBySalesVolumePerStore(UUID storeId, LocalDate date, int limit, ProductsSalesRepository repository) {
        super(storeId, date, limit, repository);
    }

    @Override
    Comparator<Product> getComparator() {
        return COMPARATOR;
    }
}
