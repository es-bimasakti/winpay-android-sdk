package id.winpay.winpaysdk.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.main.WPIConstant;
import id.winpay.winpaysdk.main.helper.AffinityHelper;
import id.winpay.winpaysdk.main.helper.AppConfig;
import id.winpay.winpaysdk.main.item.Channel;
import id.winpay.winpaysdk.main.item.ChannelGroup;
import id.winpay.winpaysdk.main.item.WPIObject;
import id.winpay.winpaysdk.main.item.WPIResponse;
import id.winpay.winpaysdk.main.message.GetLinkMessage;
import id.winpay.winpaysdk.main.message.MessageSender;
import id.winpay.winpaysdk.main.message.SuperMessage;
import id.winpay.winpaysdk.main.message.ToolbarMessage;
import id.winpay.winpaysdk.util.Mapper;
import id.winpay.winpaysdk.util.Encrypt;
import id.winpay.winpaysdk.util.Var;

/**
 * Created by Dian Cahyono on 05/10/18.
 */
public abstract class WPIToolbarActivity extends WPIActivity implements WPIActivity.WPIRequestProcessor, MessageSender.MessageProcess {

    protected static final String WPI_OBJECT = "wpi_object";
    protected static final String WPI_LISTENER = "wpi_listener";
    private WPIObject wpi_object;
    private String listener;
    private Channel channel;

    public static void start(Activity activity, Class<? extends WPIToolbarActivity> cls, int request_code, WPIObject object, @Nullable String url_listener) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(WPI_OBJECT, object);
        bundle.putString(WPI_LISTENER, url_listener);

        Intent intent = new Intent(activity, cls);
        intent.putExtras(bundle);

        activity.overridePendingTransition(R.anim.wpi__fade_in, R.anim.wpi__fade_out);
        ActivityCompat.startActivityForResult(activity, intent, request_code, null);
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        Serializable object;
        if (savedInstanceState != null) {
            object = savedInstanceState.getSerializable(WPI_OBJECT);
            listener = savedInstanceState.getString(WPI_LISTENER);
        } else if (getIntent().getExtras() != null) {
            object = getIntent().getExtras().getSerializable(WPI_OBJECT);
            listener = getIntent().getExtras().getString(WPI_LISTENER);
        } else {
            object = null;
            listener = null;
        }
        if (listener == null) {
            listener = "";
        }

        if (object instanceof WPIObject) {
            wpi_object = (WPIObject) object;

            onAfterCreate();
            getToolbar();
        } else {
            AffinityHelper.beam(this, WPIConstant.RESULT_EMPTY_WPI_OBJECT, null);
        }
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(WPI_OBJECT, wpi_object);
        savedInstanceState.putString(WPI_LISTENER, listener);

        super.onSaveInstanceState(savedInstanceState);
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WPIConstant.REQUEST_WEB_WPI) {
            WPIResponse response = null;
            if (resultCode == RESULT_CANCELED) {
                response = new WPIResponse();
                response.setResponse_code("XX");
                response.setResponse_desc(getString(R.string.wpi__info_response_cancelled));
            } else if (data != null) {
                response = AffinityHelper.intentToResponse(data);
            }
            if (response == null) {
                response = new WPIResponse();
                response.setResponse_code("0" + resultCode);
                response.setResponse_desc(getString(R.string.wpi__info_response_unknown));
            }
            AffinityHelper.beam(this, resultCode, AffinityHelper.responseToIntent(response));
        }
    }

    @CallSuper
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);

        super.onBackPressed();
    }

    @Override
    public final void beforeProcess(String tag) {
        switch (tag) {
            case "get_toolbar":
                onRequestToolbarProcess();
                break;
            case "get_token":
            case "direct":
                onRequestInquiryProcess();
                break;
        }
    }

    @Override
    public final void afterProcess(String tag, SuperMessage msg) {
        switch (tag) {
            case "get_toolbar":
                if (msg.isOK()) {
                    ToolbarMessage _msg = new ToolbarMessage(msg);
                    if (isToolbarGrouped()) {
                        processToolbar(_msg.getGroupChannels());
                    } else {
                        processToolbar(_msg.getListChannels());
                    }
                } else {
                    onRequestToolbarFailed(msg.getResponse_code(), msg.getMessageDescription());
                }
                break;
            case "get_token":
                if (msg.isOK()) {
                    Map map = msg.getDataAsMap();
                    String token = Var.toString(map.get("token"), "");
                    if (token.isEmpty()) {
                        onRequestInquiryFailed("YY", getString(R.string.wpi__warning_connection_response));
                    } else {
                        postWPI(token);
                    }
                } else {
                    onRequestInquiryFailed(msg.getResponse_code(), msg.getMessageDescription());
                }
                break;
            case "redirect":
            case "direct":
                if (msg.isOK()) {
                    Map data = msg.getDataAsMap();
                    WPIResponse response = new WPIResponse(Var.toString(data.get("reff_id"), ""));
                    response.setResponse_code(msg.getResponse_code());
                    response.setResponse_desc(msg.getMessageDescription());
                    response.setPayment_code(Var.toString(data.get("payment_code"), ""));
                    response.setOrder_id(Var.toString(data.get("order_id"), ""));
                    response.setPayment_method(Var.toString(data.get("payment_method"), ""));
                    response.setPayment_method_code(Var.toString(data.get("payment_method_code"), ""));
                    response.setFee_admin(Var.toDouble(data.get("fee_admin"), 0));
                    response.setTotal_amount(Var.toDouble(data.get("total_amount"), 0));
                    response.setSpi_status_url(Var.toString(data.get("spi_status_url"), ""));

                    if (tag.equals("direct")) {
                        processPaymentDirect(response);
                    } else {
                        response.setSpi_payment_url(Var.toString(data.get("url_payment"), ""));

                        processPaymentRedirect(response);
                    }
                } else {
                    onRequestInquiryFailed(msg.getResponse_code(), msg.getMessageDescription());
                }
                break;
        }
    }

    protected abstract boolean isToolbarGrouped();

    protected abstract void processToolbar(List<ChannelGroup> groups);

    protected abstract void processPaymentDirect(WPIResponse response);

    protected abstract void processPaymentRedirect(WPIResponse response);

    protected void onAfterCreate() {
    }

    protected final void getToken(Channel channel) {
        this.channel = channel;

        AppConfig config = AppConfig.getConfig(this);
        String pv = config.getPrivateKey1() + ":" + config.getPrivateKey2();

        MessageSender sender = new MessageSender(this)
                .setData_type(MessageSender.FORMAT_FORM_ENCODED)
                .setRequest_method(MessageSender.GET)
                .setPath("token")
                .setTag("get_token");
        sender.addHeader("Authorization", "Basic " + Base64.encodeToString(pv.getBytes(), Base64.NO_WRAP))
                .addHeader("Referer", getApplicationContext().getPackageName());
        sender.setMessageProcess(this)
                .execute();
    }

    private void getToolbar() {
        AppConfig config = AppConfig.getConfig(this);
        String pv = config.getPrivateKey1() + ":" + config.getPrivateKey2();

        ToolbarMessage msg = new ToolbarMessage();
        msg.setGrouping(isToolbarGrouped() ? 1 : 0);

        MessageSender sender = new MessageSender(this)
                .setData_type(MessageSender.FORMAT_FORM_ENCODED)
                .setRequest_method(MessageSender.GET)
                .setPath("toolbar")
                .setTag("get_toolbar")
                .addMessage(msg);
        sender.addHeader("Authorization", "Basic " + Base64.encodeToString(pv.getBytes(), Base64.NO_WRAP))
                .addHeader("Referer", getApplicationContext().getPackageName());
        sender.setMessageProcess(this)
                .execute();
    }

    private void postWPI(String token) {
        GetLinkMessage msg = new GetLinkMessage(this, channel.isDirect(), token, wpi_object);
        msg.setReferrer(getApplicationContext().getPackageName());
        msg.setURLListener(listener);

        String orderdata;
        try {
            orderdata = Encrypt.openssl_encrypt(Mapper.mapToJSON(msg).toString(), token);
        } catch (Exception e) {
            orderdata = "";
        }

        String first = orderdata.substring(0, 10);
        String last = orderdata.substring(10);
        orderdata = first + token + last;

        MessageSender sender = new MessageSender(this, channel.getPayment_url())
                .setData_type(MessageSender.FORMAT_FORM_ENCODED)
                .setRequest_method(MessageSender.POST)
                .setTag(channel.isDirect() ? "direct" : "redirect")
                .addMessage("orderdata", orderdata);
        sender.setMessageProcess(this)
                .execute();
    }
}
