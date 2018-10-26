package id.winpay.winpaysdk.main.dialog;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import id.winpay.winpaysdk.R;

/**
 * Created by Dian Cahyono on 09/10/18.
 */
public class WPIDialogState {

    @IntDef({STATE_CONFIRM, STATE_OK, STATE_WARNING, STATE_CANCEL})
    public @interface DialogState {
    }

    private static final int STATE_CONFIRM = 0;
    public static final int STATE_OK = 1;
    public static final int STATE_WARNING = 2;
    public static final int STATE_CANCEL = 3;

    @DrawableRes
    public static int stateToRes(@DialogState @DrawableRes int state) {
        switch (state) {
            case STATE_CONFIRM:
                return R.drawable.wpi__ic_question;
            case STATE_OK:
                return R.drawable.wpi__ic_check;
            case STATE_WARNING:
            case STATE_CANCEL:
                return R.drawable.wpi__ic_warning;
            default:
                return state;
        }
    }

    public interface DialogCallback {

        void onDialogCall(String tag, @WPIDialogState.DialogState int state);

    }

    public interface DialogClosingListener {

        void onDialogClosing(String tag, int code, Intent data);

        void onDialogClosed(String tag);

    }
}
