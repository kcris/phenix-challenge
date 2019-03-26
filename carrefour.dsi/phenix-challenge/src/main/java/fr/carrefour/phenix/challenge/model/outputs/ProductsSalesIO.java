package fr.carrefour.phenix.challenge.model.outputs;

import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface ProductsSalesIO {
    Collection<UUID> findStores(LocalDate date);
    ProductsGroup loadSalesInfo(UUID storeId, LocalDate date);
    void saveSalesInfo(UUID storeId, LocalDate date, ProductsGroup productSales);
}
