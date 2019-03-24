package fr.carrefour.phenix.challenge.domain.products;

/**
 * product information: id, units sold, unit price
 */
public class Product
{
    private int productId;
    private int unitsSold;
    private float unitPrice = 0; //neutral value not affecting calculus

    public Product(int productId, int unitsSold) {
        this.productId = productId;
        this.unitsSold = unitsSold;
    }

    public Product(int productId, int unitsSold, float unitPrice) {
        this.productId = productId;
        this.unitsSold = unitsSold;
        this.unitPrice = unitPrice;
    }

    public int getProductId() {
        return productId;
    }

    public int getUnitsSold() {
        return unitsSold;
    } //sales volume

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setUnitsSold(int unitsSold) { this.unitsSold = unitsSold;}

    public float getValueSold() { return getUnitsSold() * getUnitPrice(); } //sales value
}
