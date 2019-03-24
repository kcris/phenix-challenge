package fr.carrefour.phenix.challenge.domain.products;

import fr.carrefour.phenix.challenge.domain.products.aggregators.Aggregator;
import fr.carrefour.phenix.challenge.domain.tools.MapUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * contains information about a list of products, grouped by the product id
 *
 * Contains the prices and quantities for all products available in a given store, at a specified date.
 *
 * For daily product information, see {@link Product}
 */
public class ProductsGroup {

    private Map<Integer, Product> products;

    public ProductsGroup() {
        this.products = new HashMap<>();
    }

    public ProductsGroup(Map<Integer, Product> products) {
        this.products = products;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        assert products.get(product.getProductId()) == null;
        products.put(product.getProductId(), product);
    }

    public void mergeProductQuantity(int productId, int deltaUnitsSold) {
        Product product = products.get(productId);
        if (product != null) {
            int quantity = product.getUnitsSold() + deltaUnitsSold;
            product.setUnitsSold(quantity);
        }
        else {
            products.put(productId, new Product(productId, deltaUnitsSold));
        }
    }

    public Product getProduct(int productId) {
        return products.get(productId);
    }

    /**
     * return subset of products, sorted by a custom comparator, limited to desired count
     *
     * @param limit desired count of elements to be returned
     * @param comparator comparator used to sort elements
     * @return the subset of top elements
     */
    public ProductsGroup top(int limit, Comparator<Product> comparator) {

        Map<Integer, Product> result = MapUtils.getTopEntries(products, limit, comparator);

        return new ProductsGroup(result);
    }

    /**
     * merge our products with another set of products
     *
     * @param src source products to be merged with
     * @return the merged set of products
     */
    public ProductsGroup merge(ProductsGroup src, Aggregator<Product> aggregator) {

        if (src == null || src.getProducts() == null || src.getProducts().isEmpty())
            return this;

        Map<Integer, Product> result = new HashMap<>(products);

        src.getProducts().forEach(
                (key, value) -> result.merge(key, value, (v1, v2) -> aggregator.apply(v1, v2))
        );

        return new ProductsGroup(result);
    }
}
