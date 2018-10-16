package id.winpay.winpaysdk.main.item;

import java.io.Serializable;

/**
 * Created by Dian Cahyono on 05/10/18.
 */
public final class WPIItem implements Serializable {

    private final String name;
    private final int price;
    private int qty;
    private double weight;
    private String sku;
    private String desc;

    public WPIItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}