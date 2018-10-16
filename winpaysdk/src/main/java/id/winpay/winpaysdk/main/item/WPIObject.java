package id.winpay.winpaysdk.main.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dian Cahyono on 05/10/18.
 */
public final class WPIObject implements Serializable {

    private final List<WPIItem> spi_item;
    private String spi_billingName;
    private String spi_billingPhone;
    private String spi_billingEmail;
    private String spi_merchant_transaction_reff;
    private int spi_merchant_discount = 0;
    private String spi_billingDescription;

    public WPIObject() {
        this.spi_item = new ArrayList<>();
    }

    public String getSpi_billingName() {
        return spi_billingName;
    }

    public void setSpi_billingName(String spi_billingName) {
        this.spi_billingName = spi_billingName;
    }

    public String getSpi_billingPhone() {
        return spi_billingPhone;
    }

    public void setSpi_billingPhone(String spi_billingPhone) {
        this.spi_billingPhone = spi_billingPhone;
    }

    public String getSpi_billingEmail() {
        return spi_billingEmail;
    }

    public void setSpi_billingEmail(String spi_billingEmail) {
        this.spi_billingEmail = spi_billingEmail;
    }

    public String getSpi_merchant_transaction_reff() {
        return spi_merchant_transaction_reff;
    }

    public void setSpi_merchant_transaction_reff(String spi_merchant_transaction_reff) {
        this.spi_merchant_transaction_reff = spi_merchant_transaction_reff;
    }

    public List<WPIItem> getSpi_item() {
        return spi_item;
    }

    public void addSpi_item(WPIItem spi_item) {
        this.spi_item.add(spi_item);
    }

    public void clearSpi_item(WPIItem spi_item) {
        this.spi_item.clear();
    }

    public int getTotalAmount() {
        int amount = 0;

        for (WPIItem item : spi_item) {
            amount += item.getPrice();
        }

        return amount;
    }

    public int getTotalQuantity() {
        int qty = 0;

        for (WPIItem item : spi_item) {
            qty += item.getQty();
        }

        return qty;
    }

    public int getSpi_merchant_discount() {
        return spi_merchant_discount;
    }

    public void setSpi_merchant_discount(int spi_merchant_discount) {
        this.spi_merchant_discount = spi_merchant_discount;
    }

    public String getSpi_billingDescription() {
        return spi_billingDescription;
    }

    public void setSpi_billingDescription(String spi_billingDescription) {
        this.spi_billingDescription = spi_billingDescription;
    }

    public String getCMS() {
        return "ANDROID";
    }
}