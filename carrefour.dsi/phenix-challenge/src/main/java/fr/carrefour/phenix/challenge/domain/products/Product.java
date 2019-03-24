package fr.carrefour.phenix.challenge.domain.products;

/**
 * product information: id, units sold, value sold
 */
public class Product
{
    private int productId;
    private int unitsSold;
    private float valueSold;

    public Product(int productId, int unitsSold) {
        this.productId = productId;
        this.unitsSold = unitsSold;
        this.valueSold = 0;
    }

    public Product(int productId, int unitsSold, float valueSold) {
        this.productId = productId;
        this.unitsSold = unitsSold;
        this.valueSold = valueSold;
    }

    public int getProductId() {
        return productId;
    }

    public int getUnitsSold() {
        return unitsSold;
    } //sales volume
    public void setUnitsSold(int unitsSold) { this.unitsSold = unitsSold;}

    public float getValueSold() { return valueSold; } //sales value
    public void setValueSold(float valueSold) { this.valueSold = valueSold;}
}
