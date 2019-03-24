package fr.carrefour.phenix.challenge.model.inputs;

import fr.carrefour.phenix.challenge.domain.products.ProductsJournal;

import java.time.LocalDate;

/**
 * load all product information for a given date
 */
public interface ProductsJournalBuilder {
    ProductsJournal loadProducts(LocalDate date);
}
