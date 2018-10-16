package id.winpay.winpaysdk.main.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import id.winpay.winpaysdk.R;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class MessageSendingDialog extends Dialog {

    public MessageSendingDialog(Context context) {
        super(context, R.style.WPIAppDialog_Transparent);

        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wpi__widget_loading);
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}