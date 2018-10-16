package id.winpay.winpaysdk.main.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.util.Compat;

/**
 * Created by Dian Cahyono on 09/10/18.
 */
public class WPIConfirmDialog extends WPIDialog implements View.OnClickListener {

    private String title, content;

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            title = savedInstanceState.getString("title", "");
            content = savedInstanceState.getString("content", "");
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                title = bundle.getString("title", "");
                content = bundle.getString("content", "");
            } else {
                title = "";
                content = "";
            }
        }
        dialog_title.setText(title);

        TextView dialog_content = rootView.findViewById(R.id.wpi__dialog_content);
        dialog_content.setText(Compat.Html_fromHtml(content.replace("\n", "<br/>")));

        rootView.findViewById(R.id.wpi__dialog_button_yes).setOnClickListener(this);
        rootView.findViewById(R.id.wpi__dialog_button_cancel).setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("title", title);
        bundle.putString("content", content);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.wpi__widget_dialog_confirm;
    }

    @Override
    protected boolean isDismissable() {
        return true;
    }

    @Override
    protected boolean isDismissedOnBlur() {
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.wpi__dialog_button_yes || view.getId() == R.id.wpi__dialog_button_cancel) {
            if (getOnDialogCallback() != null) {
                getOnDialogCallback().onDialogCall(getTag(),
                        view.getId() == R.id.wpi__dialog_button_yes ? WPIDialogState.STATE_OK : WPIDialogState.STATE_CANCEL);
            }
            dismiss();
        }
    }

    public static Bundle getBundle(String title, String content) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);

        return bundle;
    }
}

