package id.winpay.winpaysdk.main.dialog;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.util.Compat;

/**
 * Created by Dian Cahyono on 09/10/18.
 */
public final class WPIInfoDialog extends WPIDialog implements View.OnClickListener {

    private String title, content;
    private int res;

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            title = savedInstanceState.getString("title", "");
            content = savedInstanceState.getString("content", "");
            res = savedInstanceState.getInt("res", R.drawable.wpi__ic_check);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                title = bundle.getString("title", "");
                content = bundle.getString("content", "");
                res = bundle.getInt("res", R.drawable.wpi__ic_check);
            } else {
                title = "";
                content = "";
                res = R.drawable.wpi__ic_check;
            }
        }
        dialog_title.setText(title);

        TextView dialog_content = rootView.findViewById(R.id.wpi__dialog_content);
        dialog_content.setText(Compat.Html_fromHtml(content.replace("\n", "<br/>")));

        ImageView dialog_icon = rootView.findViewById(R.id.wpi__dialog_icon);
        dialog_icon.setImageResource(res);

        rootView.findViewById(R.id.wpi__dialog_button_ok).setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("res", res);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.wpi__widget_dialog_info;
    }

    @Override
    protected boolean isDismissable() {
        return getOnDialogCallback() == null;
    }

    @Override
    protected boolean isDismissedOnBlur() {
        return getOnDialogCallback() == null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.wpi__dialog_button_ok) {
            if (getOnDialogCallback() != null) {
                getOnDialogCallback().onDialogCall(getTag(), WPIDialogState.STATE_OK);
            }
            dismiss();
        }
    }

    public static Bundle getBundle(String title, String content, @DrawableRes int res) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("res", res);

        return bundle;
    }
}
