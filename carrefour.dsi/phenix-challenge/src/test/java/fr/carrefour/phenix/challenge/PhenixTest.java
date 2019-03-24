package fr.carrefour.phenix.challenge;

import fr.carrefour.phenix.challenge.domain.products.Product;
import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.aggregators.ProductAggregatorSumSales;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorBySoldVolume;
import fr.carrefour.phenix.challenge.domain.products.comparators.ProductsComparatorBySoldValue;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PhenixTest {

    private final static Map<Integer, Product> products1 = new HashMap<Integer, Product>()
    {{
        put(1, new Product(1, 1, 10));
        put(2, new Product(2, 2, 10));
        put(3, new Product(3, 10, 100));
        put(4, new Product(4, 100, 50));
    }};

    private final static Map<Integer, Product> products2 = new HashMap<Integer, Product>()
    {{
        put(30, new Product(30, 15, 1));
        put(40, new Product(40, 25, 2));
        put(1, new Product(1, 40, 5));
        put(2, new Product(2, 60, 8));
    }};

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testTopSalesByVolume() {

        ProductsGroup p1 = new ProductsGroup(products1);

        Map<Integer, Product> result = p1.top(2, new ProductsComparatorBySoldVolume())
                                         .getProducts();

        assertTrue(result.size() == 2);
        assertNotNull(result.get(3));
        assertNotNull(result.get(4));

        assertNull(result.get(1));
        assertNull(result.get(2));
    }

    @Test
    public void testTopSalesByValue() {

        ProductsGroup p2 = new ProductsGroup(products2);

        Map<Integer, Product> result = p2.top(2, new ProductsComparatorBySoldValue())
                                         .getProducts();

        assertTrue(result.size() == 2);
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));

        assertNull(result.get(30));
        assertNull(result.get(40));
    }

    @Test
    public void testAggregatedSales() {
        ProductsGroup p1 = new ProductsGroup(products1);
        ProductsGroup p2 = new ProductsGroup(products2);

        Map<Integer, Product> result = p1.merge(p2, new ProductAggregatorSumSales())
                                         .getProducts();

        assertTrue(result.size() == 6);
        assertTrue(result.get(1).getUnitsSold() == 41); //merged
        assertTrue(result.get(2).getUnitsSold() == 62); //merged
        assertTrue(result.get(3).getUnitsSold() == 10);
        assertTrue(result.get(4).getUnitsSold() == 100);
        assertTrue(result.get(30).getUnitsSold() == 15);
        assertTrue(result.get(40).getUnitsSold() == 25);
    }
}