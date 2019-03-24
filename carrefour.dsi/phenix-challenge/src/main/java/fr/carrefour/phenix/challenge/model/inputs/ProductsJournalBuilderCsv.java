package fr.carrefour.phenix.challenge.model.inputs;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * reads daily journals of a store from csv files
 */
public class ProductsJournalBuilderCsv implements ProductsJournalBuilder {

    private static final Logger LOGGER = LogManager.getLogger(ProductsJournalBuilderCsv.class);

    private static final String REGEXP_CSV_SEPARATOR = "\\|"; //escaped since it's used in regex

    private String dataFolder;

    public ProductsJournalBuilderCsv(String dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Override
    public ProductsJournal loadProducts(LocalDate date) {

        final ProductsJournal journal = new ProductsJournal();
        journal.setDate(date);

        {
            //find journal file for daily transactions (all stores)
            String fileName = String.format("transactions_%04d%02d%02d.data", date.getYear(), date.getMonthValue(), date.getDayOfMonth());

            loadTransactions(journal, fileName);
        }

        {
            //find reference files for daily prices (one per store)
            String fileNameSuffix = String.format("_%04d%02d%02d.data", date.getYear(), date.getMonthValue(), date.getDayOfMonth());

            File dir = new File(dataFolder);
            File[] files = dir.listFiles((dir1, name) -> name.startsWith("reference_prod-") && name.endsWith(fileNameSuffix));
            Arrays.stream(files).forEach(f -> loadPrices(journal, f.getName()));
        }

        return journal;
    }

    /**
     * read transactions csv file which contains all transactions for all stores, at the given date
     *
     * @param csvFilename the name of the csv file to be loaded
     *
     * updates the unitsSold for each product (valueSold is missing at the moment, will be later loaded)
     */
    private void loadTransactions(ProductsJournal journal, String csvFilename)  {

        String csvPath = String.format("%s/%s", dataFolder, csvFilename);

        LOGGER.debug("Loading transactions from " + csvPath);

        try {
            final Map<UUID, ProductsGroup> allProducts = journal.getAllProducts();
            assert allProducts != null;

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(REGEXP_CSV_SEPARATOR);

                UUID storeId = UUID.fromString(fields[2]);
                int productId = Integer.valueOf(fields[3]);
                int unitsSold = Integer.valueOf(fields[4]);

                ProductsGroup storeProducts = allProducts.get(storeId);

                if (storeProducts == null) {
                    ProductsGroup newStoreProducts = new ProductsGroup();
                    newStoreProducts.addProduct(new Product(productId, unitsSold)); //add new product

                    allProducts.put(storeId, newStoreProducts); //add new store
                } else {
                    Product product = storeProducts.getProduct(productId);
                    if (product != null) {
                        product.setUnitsSold(product.getUnitsSold() + unitsSold); //update unitsSold information
                    }
                    else {
                        storeProducts.addProduct(new Product(productId, unitsSold)); //add new product
                    }
                }
            }
        }
        catch(Exception ex) {
            throw new RuntimeException("cannot read transactions file " + csvPath, ex);
        }
    }

    /**
     * read prices csv file which contains the prices for all products used by a specific store, at the given date
     *
     * @param csvFilename the name of the csv file to be loaded
     *
     * updates the valueSold for each product (unitsSold has already been loaded)
     */
    private void loadPrices(ProductsJournal journal, String csvFilename) {

        String csvPath = String.format("%s/%s", dataFolder, csvFilename);

        LOGGER.debug("Loading prices from " + csvPath);

        try{
            final UUID storeId = UUID.fromString(csvFilename.substring(15, 15+36));

            final Map<UUID, ProductsGroup> allProducts = journal.getAllProducts();
            assert allProducts != null;

            final ProductsGroup storeProducts = allProducts.get(storeId);
            //assert storeProducts != null;
            if (storeProducts == null) {
                LOGGER.debug("Ref file store not found in transactions, skipping valueSold update");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(REGEXP_CSV_SEPARATOR);

                int productId = Integer.valueOf(fields[0]);
                float unitPrice = Float.valueOf(fields[1]);

                Product prod = storeProducts.getProduct(productId);
                if (prod != null) {
                    prod.setValueSold(prod.getUnitsSold() * unitPrice); //add valueSold information
                }
                else {
                    LOGGER.debug("Ref file product not found in transactions, skipping valueSold update");
                }
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("cannot read reference file " + csvPath, ex);
        }
    }
}
