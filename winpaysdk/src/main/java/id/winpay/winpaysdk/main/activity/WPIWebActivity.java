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
import android.widget.TextView;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.util.Var;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Dian Cahyono on 22/10/18.
 */
public final class WPIWebActivity extends WPIActivity implements AdvancedWebView.Listener, View.OnClickListener {

    private static final String WPI_TITLE = "wpi_title";
    private static final String WPI_URL = "wpi_url";
    private AdvancedWebView wpi_webview;
    private View wpi_loading;
    private String wpi_title, wpi_url;

    public static void start(Activity activity, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(WPI_TITLE, title);
        bundle.putString(WPI_URL, url);

        Intent intent = new Intent(activity, WPIWebActivity.class);
        intent.putExtras(bundle);

        activity.overridePendingTransition(R.anim.wpi__fade_in, R.anim.wpi__fade_out);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wpi__activity_web);

        if (savedInstanceState != null) {
            wpi_url = savedInstanceState.getString(WPI_URL);
            wpi_title = savedInstanceState.getString(WPI_TITLE);
        } else if (getIntent().getExtras() != null) {
            wpi_url = getIntent().getExtras().getString(WPI_URL);
            wpi_title = getIntent().getExtras().getString(WPI_TITLE);
        } else {
            wpi_url = null;
            wpi_title = null;
        }
        wpi_url = Var.toString(wpi_url, "");
        wpi_title = Var.toString(wpi_title, getString(R.string.app_real_name));

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

                private boolean handleUri(final Uri uri) {
                    return !uri.getHost().matches("^(sandbox|secure)-payment\\.winpay\\.id$");
                }
            });
            wpi_webview.loadUrl(uri.toString());

            TextView wpi_webview_title = findViewById(R.id.wpi_webview_title);
            wpi_webview_title.setText(wpi_title);

            TextView wpi_webview_subtitle = findViewById(R.id.wpi_webview_subtitle);
            wpi_webview_subtitle.setText(wpi_url);
        }

        findViewById(R.id.wpi_webview_icon).setOnClickListener(this);
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(WPI_TITLE, wpi_title);
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
        onExternalPageRequest(url);
    }

    @Override
    public void onExternalPageRequest(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.wpi_webview_icon) {
            finish();
        }
    }
}
