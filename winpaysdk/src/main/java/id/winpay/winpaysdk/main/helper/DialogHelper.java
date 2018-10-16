package id.winpay.winpaysdk.main.helper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.main.dialog.WPIConfirmDialog;
import id.winpay.winpaysdk.main.dialog.WPIDialogState;
import id.winpay.winpaysdk.main.dialog.WPIInfoDialog;
import id.winpay.winpaysdk.util.Compat;

/**
 * Created by Dian Cahyono on 09/10/18.
 */
public final class DialogHelper {

    public static void info(FragmentActivity activity, @WPIDialogState.DialogState int state, String message) {
        info(activity, state, message, null);
    }

    public static void info(FragmentActivity activity, @WPIDialogState.DialogState int state,
                            String message, WPIDialogState.DialogCallback callback) {
        int str;
        if (state == WPIDialogState.STATE_OK) {
            str = R.string.wpi__title_info;
        } else {
            str = R.string.wpi__title_warning;
        }
        Bundle bundle = WPIInfoDialog.getBundle(activity.getString(str), message, WPIDialogState.stateToRes(state));

        WPIInfoDialog infoDialog = new WPIInfoDialog();
        infoDialog.setArguments(bundle);
        infoDialog.setOnDialogCallback(callback);
        infoDialog.show(activity.getSupportFragmentManager(), "WPIInfoDialog");
    }

    public static void confirm(FragmentActivity activity, String message, WPIDialogState.DialogCallback callback) {
        Bundle bundle = WPIConfirmDialog.getBundle(activity.getString(R.string.wpi__title_confirm), message);

        WPIConfirmDialog confirmDialog = new WPIConfirmDialog();
        confirmDialog.setArguments(bundle);
        confirmDialog.setOnDialogCallback(callback);
        confirmDialog.show(activity.getSupportFragmentManager(), "WPIConfirmDialog");
    }

    public static void toast(Context context, String msg) {
        View layout = View.inflate(context, R.layout.wpi__widget_toast, null);

        final Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        TextView text = layout.findViewById(R.id.wpi__toast_text);
        text.setText(Compat.Html_fromHtml(msg.replace("\n", "<br/>")));

        ViewGroup parent = layout.findViewById(R.id.wpi__toast_frame);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.cancel();
            }
        });

        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        };
        mainHandler.post(myRunnable);
    }

}
