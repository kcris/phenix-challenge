package fr.carrefour.phenix.challenge.domain.products;

import fr.carrefour.phenix.challenge.model.outputs.ProductsSalesIO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * holds products journals for multiple days on disk
 *
 * for each day, we have all information about product sales
 */
public class ProductsSalesRepositoryDisk implements ProductsSalesRepository {

    private final ProductsSalesIO salesIo;

    public ProductsSalesRepositoryDisk(ProductsSalesIO salesIo) {
        this.salesIo = salesIo;
    }

    @Override
    public void addProducts(UUID storeId, LocalDate date, ProductsGroup products) {
        salesIo.saveSalesInfo(storeId, date, products);
    }

    @Override
    public ProductsGroup getProducts(UUID storeId, LocalDate date) {
        return salesIo.loadSalesInfo(storeId, date);
    }

    @Override
    public Collection<UUID> getStores(LocalDate date) {
        return salesIo.findStores(date);
    }
}
