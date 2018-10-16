package id.winpay.winpaysdk.main.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.main.WPIConstant;
import id.winpay.winpaysdk.main.item.WPIResponse;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class AffinityHelper {

    public static void beam(Activity activity, @WPIConstant.CustomActivityResult int result_code, @Nullable Intent intent) {
        if (intent == null) {
            activity.setResult(result_code);
        } else {
            activity.setResult(result_code, intent);
        }
        activity.finish();
    }

    @Nullable
    public static WPIResponse intentToResponse(@NonNull Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable("wpi_response");
            if (serializable instanceof WPIResponse) {
                return (WPIResponse) serializable;
            }
        }
        return null;
    }

    public static Intent responseToIntent(@NonNull WPIResponse response) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("wpi_response", response);

        Intent intent = new Intent();
        intent.putExtras(bundle);

        return intent;
    }

    public static void copy(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            return;
        }
        android.content.ClipData clip = android.content.ClipData.newPlainText("TEXT:COPY", text);
        clipboard.setPrimaryClip(clip);

        DialogHelper.toast(context, context.getString(R.string.wpi__info_text_copied));
    }

}
