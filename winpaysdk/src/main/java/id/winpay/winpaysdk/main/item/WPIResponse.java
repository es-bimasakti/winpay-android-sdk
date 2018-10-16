package id.winpay.winpaysdk.main.item;

import java.io.Serializable;

/**
 * Created by Dian Cahyono on 05/10/18.
 */
public final class WPIResponse implements Serializable {

    private final String reff_id;
    private String response_code;
    private String response_desc;
    private String merchant_reff;
    private String order_id;
    private String spi_status_url;
    private String spi_payment_url;
    private String payment_code;
    private String payment_method;
    private String payment_method_code;
    private double fee_admin;
    private double total_amount;

    public WPIResponse() {
        this("");
    }

    public WPIResponse(String reff_id) {
        this.reff_id = reff_id;
    }

    public String getMerchant_reff() {
        return merchant_reff;
    }

    public void setMerchant_reff(String merchant_reff) {
        this.merchant_reff = merchant_reff;
    }

    public String getReff_id() {
        return reff_id;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getResponse_desc() {
        return response_desc;
    }

    public void setResponse_desc(String response_desc) {
        this.response_desc = response_desc;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSpi_status_url() {
        return spi_status_url;
    }

    public void setSpi_status_url(String spi_status_url) {
        this.spi_status_url = spi_status_url;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_method_code() {
        return payment_method_code;
    }

    public void setPayment_method_code(String payment_method_code) {
        this.payment_method_code = payment_method_code;
    }

    public double getFee_admin() {
        return fee_admin;
    }

    public void setFee_admin(double fee_admin) {
        this.fee_admin = fee_admin;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getSpi_payment_url() {
        return spi_payment_url;
    }

    public void setSpi_payment_url(String spi_payment_url) {
        this.spi_payment_url = spi_payment_url;
    }
}

