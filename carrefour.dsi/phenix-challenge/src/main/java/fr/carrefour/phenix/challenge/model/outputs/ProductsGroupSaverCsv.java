package fr.carrefour.phenix.challenge.model.outputs;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.util.StringJoiner;

public class ProductsGroupSaverCsv implements ProductsGroupSaver {

    private static final Logger LOGGER = LogManager.getLogger(ProductsGroupSaverCsv.class);

    private static final String CSV_SEPARATOR = "|";

    private String dataFolder;

    public ProductsGroupSaverCsv(String dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Override
    public void saveProducts(ProductsGroup products, String title, ProductsGroupSortFilter sortFilter) {

        String csvPath = String.format("./%s/%s.csv", dataFolder, title);
        LOGGER.debug("Saving statistics to " + csvPath);

        try {
            File csvOutputFile = new File(csvPath);
            csvOutputFile.getParentFile().mkdirs(); //auto create subdir if needed

            try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
                products.getProducts().values().stream()
                        .sorted(sortFilter.getSoldFieldComparator().reversed()) //sort in descending order
                        .map(p -> createCsvLine(p, sortFilter))
                        .forEach(pw::println);
            }

            assert csvOutputFile.exists();
        }
        catch (Exception ex) {
            throw new RuntimeException("cannot write output file");
        }
    }

    private String createCsvLine(Product product, ProductsGroupSortFilter sortFilter) {

        StringJoiner joiner = new StringJoiner(CSV_SEPARATOR);

        joiner.add(String.valueOf(product.getProductId()));
        joiner.add(sortFilter.getSoldFieldValue(product));

        return joiner.toString();
    }
}
