package id.winpay.winpaysdk.main.activity;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.main.WPIConstant;
import id.winpay.winpaysdk.main.dialog.WPIDialogState;
import id.winpay.winpaysdk.main.helper.AffinityHelper;
import id.winpay.winpaysdk.main.helper.AppConfig;
import id.winpay.winpaysdk.main.helper.DialogHelper;
import id.winpay.winpaysdk.main.item.Channel;
import id.winpay.winpaysdk.main.item.ChannelGroup;
import id.winpay.winpaysdk.main.item.WPIObject;
import id.winpay.winpaysdk.main.item.WPIResponse;
import id.winpay.winpaysdk.main.widget.ChannelAdapter;
import id.winpay.winpaysdk.main.widget.MessageSendingDialog;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class WPIToolbarImplActivity extends WPIToolbarActivity implements ChannelAdapter.OnChannelClickListener {

    private MessageSendingDialog dialog;

    public static void start(Activity activity, int request_code, WPIObject object, @Nullable String url_listener) {
        WPIToolbarActivity.start(activity, WPIToolbarImplActivity.class, request_code, object, url_listener);
    }

    @Override
    protected void onAfterCreate() {
        findViewById(R.id.wpi__sandbox_warning).setVisibility(
                AppConfig.getConfig(this).is_sandbox_mode() ? View.VISIBLE : View.GONE
        );

        dialog = new MessageSendingDialog(this);
    }

    @Override
    public void onBackPressed() {
        DialogHelper.confirm(this, getString(R.string.wpi__confirm_exit_toolbar), new WPIDialogState.DialogCallback() {
            @Override
            public void onDialogCall(String tag, int state) {
                if (state == WPIDialogState.STATE_OK) {
                    WPIToolbarImplActivity.super.onBackPressed();
                }
            }
        });
    }

    @Override
    protected boolean isToolbarGrouped() {
        return true;
    }

    @Override
    protected void processToolbar(List<ChannelGroup> groups) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        int wpi_toolbar_grid = getResources().getInteger(R.integer.wpi__toolbar_grid);
        LinearLayout wpi_toolbar_list = findViewById(R.id.wpi__toolbar_list);
        for (ChannelGroup group : groups) {
            View panel = View.inflate(this, R.layout.wpi__component_toolbar_group, null);

            TextView tv = panel.findViewById(R.id.wpi__cwtg_group);
            tv.setText(group.getGroup().toUpperCase());

            ChannelAdapter ca = new ChannelAdapter(this, group.getChannels(), wpi_toolbar_grid);
            ca.setListener(this);

            RecyclerView rv = panel.findViewById(R.id.wpi__cwtg_channels);
            rv.setLayoutManager(new GridLayoutManager(this, wpi_toolbar_grid));
            rv.setNestedScrollingEnabled(false);
            rv.setAdapter(ca);

            wpi_toolbar_list.addView(panel);
        }
    }

    @Override
    protected void processPaymentDirect(WPIResponse response) {
        WPIDirectActivity.start(this, response);
        AffinityHelper.beam(this, WPIConstant.RESULT_OK, AffinityHelper.responseToIntent(response));
    }

    @Override
    protected void processPaymentRedirect(WPIResponse response) {
        WPIRedirectActivity.start(this, response.getSpi_payment_url());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.wpi__activity_toolbar;
    }

    @Override
    public void onRequestToolbarProcess() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onRequestToolbarFailed(final String rc, final String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        DialogHelper.info(this, WPIDialogState.STATE_WARNING, msg, new WPIDialogState.DialogCallback() {
            @Override
            public void onDialogCall(String tag, int state) {
                WPIResponse response = new WPIResponse();
                response.setResponse_code(rc);
                response.setResponse_desc(msg);
                AffinityHelper.beam(WPIToolbarImplActivity.this, WPIConstant.RESULT_INVALID_TOKEN_RESPONSE, AffinityHelper.responseToIntent(response));
            }
        });
    }

    @Override
    public void onRequestInquiryProcess() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onRequestInquiryFailed(final String rc, final String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        DialogHelper.info(this, WPIDialogState.STATE_WARNING, msg, new WPIDialogState.DialogCallback() {
            @Override
            public void onDialogCall(String tag, int state) {
                WPIResponse response = new WPIResponse();
                response.setResponse_code(rc);
                response.setResponse_desc(msg);
            }
        });
    }

    @Override
    public void onChannelClick(Channel channel, int position) {
        getToken(channel);
    }
}
