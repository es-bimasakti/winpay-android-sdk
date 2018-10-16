package id.winpay.winpaysdk.main.helper;

import android.content.Context;

import java.lang.ref.WeakReference;

import id.winpay.winpaysdk.R;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class AppConfig {

    private static AppConfig config;
    private final WeakReference<Context> reference;
    private final boolean is_sandbox_mode;
    private final boolean is_loggable;

    private AppConfig(Context context) {
        reference = new WeakReference<>(context);
        is_sandbox_mode = context.getResources().getBoolean(R.bool.wpi__sandbox_mode);
        is_loggable = context.getResources().getBoolean(R.bool.wpi__is_loggable);
    }

    public static AppConfig getConfig(Context context) {
        if (config == null || config.reference == null || config.reference.get() == null) {
            config = new AppConfig(context);
        }

        return config;
    }

    public boolean is_sandbox_mode() {
        return is_sandbox_mode;
    }

    public boolean is_loggable() {
        return is_loggable;
    }

    public String getURL() {
        if (is_sandbox_mode()) {
            return "https://sandbox-payment.winpay.id/";
        } else {
            return "https://secure-payment.winpay.id/";
        }
    }

    public String getPrivateKey1() {
        if (reference.get() == null) {
            return "";
        }
        return reference.get().getString(R.string.wpi__private_key_1);
    }

    public String getPrivateKey2() {
        if (reference.get() == null) {
            return "";
        }
        return reference.get().getString(R.string.wpi__private_key_2);
    }

    public String getMerchantKey() {
        if (reference.get() == null) {
            return "";
        }
        return reference.get().getString(R.string.wpi__merchant_key);
    }

}
