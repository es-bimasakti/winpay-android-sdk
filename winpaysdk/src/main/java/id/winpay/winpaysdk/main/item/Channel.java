package id.winpay.winpaysdk.main.item;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public class Channel {
    private String payment_code;
    private String payment_name;
    private String payment_logo;
    private String payment_url;
    private String description;
    private final boolean direct;

    public Channel(boolean direct) {
        this.direct = direct;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getPayment_logo() {
        return payment_logo;
    }

    public void setPayment_logo(String payment_logo) {
        this.payment_logo = payment_logo;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDirect() {
        return direct;
    }
}
