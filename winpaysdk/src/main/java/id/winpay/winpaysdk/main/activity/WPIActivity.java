package id.winpay.winpaysdk.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;

/**
 * Created by Dian Cahyono on 05/10/18.
 */
class WPIActivity extends AppCompatActivity {

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Locale.setDefault(new Locale("id"));
    }

    @CallSuper
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
    }

    final void hideKeyboard(View view) {
        if (view == null) {
            view = getCurrentFocus();
        }
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    final void showKeyboard(View view) {
        if (view == null) {
            view = getCurrentFocus();
        }
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.showSoftInput(view, 0);
            }
        }
    }

    protected interface WPIRequestProcessor {

        int getLayoutResource();

        void onRequestToolbarProcess();

        void onRequestToolbarFailed(String rc, String msg);

        void onRequestInquiryProcess();

        void onRequestInquiryFailed(String rc, String msg);
    }

}
