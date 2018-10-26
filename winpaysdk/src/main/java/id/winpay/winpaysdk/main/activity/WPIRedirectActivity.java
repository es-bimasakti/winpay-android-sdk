package id.winpay.winpaysdk.main.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.main.WPIConstant;
import id.winpay.winpaysdk.main.dialog.WPIDialogState;
import id.winpay.winpaysdk.main.helper.AffinityHelper;
import id.winpay.winpaysdk.main.helper.AppConfig;
import id.winpay.winpaysdk.main.helper.DialogHelper;
import id.winpay.winpaysdk.main.item.WPIResponse;
import id.winpay.winpaysdk.util.Mapper;
import id.winpay.winpaysdk.util.Var;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Dian Cahyono on 22/10/18.
 */
public final class WPIRedirectActivity extends WPIActivity implements AdvancedWebView.Listener {

    private static final String WPI_URL = "wpi_url";
    private AdvancedWebView wpi_webview;
    private View wpi_loading;
    private String wpi_url;

    public static void start(Activity activity, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(WPI_URL, url);

        Intent intent = new Intent(activity, WPIRedirectActivity.class);
        intent.putExtras(bundle);

        activity.overridePendingTransition(R.anim.wpi__fade_in, R.anim.wpi__fade_out);
        ActivityCompat.startActivityForResult(activity, intent, WPIConstant.REQUEST_WEB_WPI, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wpi__activity_redirect);

        if (savedInstanceState != null) {
            wpi_url = savedInstanceState.getString(WPI_URL);
        } else if (getIntent().getExtras() != null) {
            wpi_url = getIntent().getExtras().getString(WPI_URL);
        } else {
            wpi_url = null;
        }
        wpi_url = Var.toString(wpi_url, "");

        wpi_loading = findViewById(R.id.wpi_webview_loading);
        wpi_webview = findViewById(R.id.wpi_webview);
        if (URLUtil.isValidUrl(wpi_url)) {
            final Uri uri = Uri.parse(wpi_url);
            final boolean is_sandbox_mode = getResources().getBoolean(R.bool.wpi__sandbox_mode);

//            wpi_webview.setCookiesEnabled(true);
//            wpi_webview.setThirdPartyCookiesEnabled(true);
            wpi_webview.setListener(this, this);
            wpi_webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Uri err = Uri.parse(error.getUrl());
                    if (err.getHost().equals(uri.getHost()) && is_sandbox_mode) {
                        handler.proceed();
                    }
                }

                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return handleUri(Uri.parse(url));
                }

                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return handleUri(request.getUrl());
                }
            });
            wpi_webview.loadUrl(uri.toString());
        }

        findViewById(R.id.wpi_sandbox_warning).setVisibility(
                AppConfig.getConfig(this).is_sandbox_mode() ? View.VISIBLE : View.GONE
        );
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(WPI_URL, wpi_url);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wpi_webview.onResume();
    }

    @Override
    protected void onPause() {
        wpi_webview.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        wpi_webview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        wpi_webview.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        DialogHelper.confirm(this, getString(R.string.wpi__confirm_exit_web), new WPIDialogState.DialogCallback() {
            @Override
            public void onDialogCall(String tag, int state) {
                if (state == WPIDialogState.STATE_OK) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        wpi_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String url) {
        wpi_loading.setVisibility(View.GONE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }


    private boolean handleUri(final Uri uri) {
        if (uri.getPath().endsWith("plugin/android/throwback")) {
            try {
                Map<String, String> map = Mapper.queryStringToMap(uri.getQuery());
                String rc = Var.toString(map.get("response_code"), "--");
                String rd = Var.toString(map.get("response_desc"), "GE");
                String pc = Var.toString(map.get("payment_code"), "");
                boolean is_pending_trx = rc.isEmpty() && !pc.isEmpty();

                String payment_method = Var.toString(map.get("payment_method"), "");
                String status_url = Var.toString(map.get("spi_status_url"), "");
                String title = getString(R.string.wpi__info_title_format, payment_method);

                WPIResponse response = new WPIResponse(Var.toString(map.get("reff_id"), "--"));
                response.setOrder_id(Var.toString(map.get("order_id"), ""));
                response.setMerchant_reff(Var.toString(map.get("merchant_reff"), ""));
                response.setResponse_code(is_pending_trx ? "00" : rc);
                response.setResponse_desc(is_pending_trx && rd.isEmpty() ? "Pending" : rd);
                response.setPayment_code(pc);
                response.setSpi_status_url(status_url);
                response.setPayment_method(payment_method);
                response.setPayment_method_code(Var.toString(map.get("payment_method_code"), ""));
                response.setFee_admin(Var.toDouble(map.get("fee_admin"), 0));
                response.setTotal_amount(Var.toDouble(map.get("total_amount"), 0));

                if (getResources().getBoolean(R.bool.wpi__show_redirect_url)) {
                    WPIWebActivity.start(this, title, status_url);
                }
                AffinityHelper.beam(this, WPIConstant.RESULT_OK, AffinityHelper.responseToIntent(response));

                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        this.wpi_url = uri.toString();

        return false;
    }
}
