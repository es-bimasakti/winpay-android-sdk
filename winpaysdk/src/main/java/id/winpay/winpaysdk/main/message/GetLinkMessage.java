package id.winpay.winpaysdk.main.message;

import android.content.Context;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.winpay.winpaysdk.main.helper.AppConfig;
import id.winpay.winpaysdk.main.item.WPIItem;
import id.winpay.winpaysdk.main.item.WPIObject;
import id.winpay.winpaysdk.util.Encrypt;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class GetLinkMessage extends SuperMessage {
    public GetLinkMessage(Context context, boolean is_direct, String token, WPIObject wpi) {
        super();

        AppConfig config = AppConfig.getConfig(context);
        String pv = config.getPrivateKey1() + config.getPrivateKey2();

        setItem("cms", wpi.getCMS());
        setItem("authKey", "");
        setItem("spi_currency", "IDR");
        setItem("spi_item", getItemMap(wpi.getSpi_item()));
        setItem("spi_item_description", wpi.getSpi_billingDescription());
        setItem("spi_price", wpi.getTotalAmount());
        setItem("spi_quantity", wpi.getTotalQuantity());
        setItem("spi_is_escrow", 0);
        setItem("spi_amount", wpi.getTotalAmount());
        setItem("spi_token", pv);
        setItem("spi_merchant_transaction_reff", wpi.getSpi_merchant_transaction_reff());
        setItem("spi_item_expedition", 0);
        setItem("spi_billingPhone", wpi.getSpi_billingPhone());
        setItem("spi_billingEmail", wpi.getSpi_billingEmail());
        setItem("spi_billingName", wpi.getSpi_billingName());
        setItem("spi_paymentDate", "");
        setItem("spi_request_key", token);
        setItem("get_link", "no");
        setItem("skip_spi_page", 0);
        setItem("spi_signature", getSignature(token, config.getMerchantKey(), wpi));
        setItem("payment_via", "WPILIB_ANDROID");

        if (!is_direct) {
            try {
                setItem("spi_callback", config.getURL() + "plugin/android/callback/" +
                        URLEncoder.encode(wpi.getSpi_merchant_transaction_reff(), "UTF-8"));
            } catch (UnsupportedEncodingException ignored) {
                setItem("spi_callback", config.getURL() + "plugin/android/callback/" +
                        wpi.getSpi_merchant_transaction_reff().replaceAll("[^0-9a-zA-Z]", ""));
            }
        }
    }

    public void setReferrer(String url) {
        Uri uri = Uri.parse(url);

        setItem("spi_url_referer", url);
        setItem("spi_url_store", uri.getHost());
    }

    public void setURLListener(String url) {
        setItem("url_listener", url);
    }

    private String getSignature(String token, String merchantKey, WPIObject wpi) {
        if (merchantKey.isEmpty()) {
            return "";
        }
        String raw = String.format(Locale.getDefault(), "%s|%s|%s|%s|%s|%s",
                token, merchantKey, wpi.getSpi_merchant_transaction_reff(),
                String.format(Locale.US, "%.2f", (double) wpi.getTotalAmount()),
                String.format(Locale.US, "%.2f", 0d),
                String.format(Locale.US, "%.2f", (double) wpi.getSpi_merchant_discount()));

        return Encrypt.sha1(raw).toUpperCase();
    }

    private List<Map<String, String>> getItemMap(List<WPIItem> wpi) {
        List<Map<String, String>> items = new ArrayList<>();
        for (WPIItem item : wpi) {
            Map<String, String> map = new HashMap<>();
            map.put("name", item.getName());
            map.put("sku", item.getSku());
            map.put("qty", String.valueOf(item.getQty()));
            map.put("unitPrice", String.valueOf(item.getPrice()));
            map.put("weight", String.valueOf(item.getWeight()));
            map.put("desc", item.getDesc());

            items.add(map);
        }

        return items;
    }
}
