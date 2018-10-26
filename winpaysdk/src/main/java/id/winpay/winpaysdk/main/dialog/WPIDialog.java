package id.winpay.winpaysdk.main.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import id.winpay.winpaysdk.R;

/**
 * Created by Dian Cahyono on 09/10/18.
 */
abstract class WPIDialog extends DialogFragment {

    TextView dialog_title;

    private WPIDialogState.DialogCallback onDialogCallback;
    private WPIDialogState.DialogClosingListener onDialogClosingListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        if (getActivity() != null) {
            final FrameLayout root = new FrameLayout(getActivity());
            root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            Dialog dialog = new Dialog(getActivity(), R.style.WPIAppDialog_Transparent);
            dialog.setCancelable(isDismissable());
            dialog.setCanceledOnTouchOutside(isDismissedOnBlur());
            dialog.setContentView(root);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }

            return dialog;
        } else {
            return super.onCreateDialog(savedInstanceState);
        }
    }

    @NonNull
    @CallSuper
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wpi__widget_dialog, container, false);

        dialog_title = rootView.findViewById(R.id.wpi__dialog_title);

        ViewStub stub = rootView.findViewById(R.id.wpi__dialog_stub);
        stub.setLayoutResource(getLayoutResource());
        stub.inflate();

        setCancelable(isDismissable());

        return rootView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (onDialogClosingListener != null) {
            onDialogClosingListener.onDialogClosed(getTag());
        }
        super.onDismiss(dialog);
    }

    public void setOnDialogCallback(WPIDialogState.DialogCallback onDialogCallback) {
        this.onDialogCallback = onDialogCallback;
    }

    public void setOnDialogClosingListener(WPIDialogState.DialogClosingListener onDialogClosingListener) {
        this.onDialogClosingListener = onDialogClosingListener;
    }

    WPIDialogState.DialogCallback getOnDialogCallback() {
        return onDialogCallback;
    }

    protected abstract int getLayoutResource();

    protected abstract boolean isDismissable();

    protected abstract boolean isDismissedOnBlur();

}

