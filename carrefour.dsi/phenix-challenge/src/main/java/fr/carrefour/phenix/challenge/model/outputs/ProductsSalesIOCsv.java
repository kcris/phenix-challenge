package fr.carrefour.phenix.challenge.model.outputs;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ProductsSalesIOCsv implements ProductsSalesIO {

    private static final Logger LOGGER = LogManager.getLogger(ProductsSalesIOCsv.class);

    private static final String REGEXP_CSV_SEPARATOR = "\\|"; //escaped since it's used in regex
    private static final String CSV_SEPARATOR = "|";

    private String dataFolder;

    public ProductsSalesIOCsv(String dataFolder) {
        this.dataFolder = dataFolder;
    }


    @Override
    public Collection<UUID> findStores(LocalDate date) {

        String dirName = String.format("./%s/sales/%s", dataFolder, date);
        File dir = new File(dirName);
        File[] files = dir.listFiles();
        Collection<UUID> storeIds = Arrays.stream(files).map(f -> UUID.fromString(f.getName()))
                .collect(toList());
        return storeIds;
    }

    @Override
    public ProductsGroup loadSalesInfo(UUID storeId, LocalDate date) {

        String csvPath = String.format("./%s/sales/%s/%s/product_sales.csv", dataFolder, date, storeId);
        LOGGER.debug("Loading product sales for date={} storeId={} from {}", date, storeId, csvPath);

        ProductsGroup productSales = new ProductsGroup();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(REGEXP_CSV_SEPARATOR);

                int productId = Integer.valueOf(fields[0]);
                int unitsSold = Integer.valueOf(fields[1]);
                float valueSold = Float.valueOf(fields[2]);

                productSales.addProduct(new Product(productId, unitsSold, valueSold));
            }
        }
        catch(Exception ex) {
            throw new RuntimeException("cannot read sales file " + csvPath, ex);
        }

        return productSales;
    }

    @Override
    public void saveSalesInfo(UUID storeId, LocalDate date, ProductsGroup productSales) {

        String csvPath = String.format("./%s/sales/%s/%s/product_sales.csv", dataFolder, date, storeId);
        LOGGER.debug("Saving product sales for date={} storeId={} to {}", date, storeId, csvPath);

        try {
            File csvOutputFile = new File(csvPath);
            csvOutputFile.getParentFile().mkdirs(); //auto create subdir if needed

            try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
                productSales.getProducts().values().stream()
                        .map(this::saveCsvLine)
                        .forEach(pw::println);
            }

            assert csvOutputFile.exists();
        }
        catch (Exception ex) {
            throw new RuntimeException("cannot write sales file");
        }
    }

    private String saveCsvLine(Product product) {

        StringJoiner joiner = new StringJoiner(CSV_SEPARATOR);

        joiner.add(String.valueOf(product.getProductId()));
        joiner.add(String.valueOf(product.getUnitsSold())); //top sales volume
        joiner.add(String.valueOf(product.getValueSold())); //top sales value

        return joiner.toString();
    }
}
