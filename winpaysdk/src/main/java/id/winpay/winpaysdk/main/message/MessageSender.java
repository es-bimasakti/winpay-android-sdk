package id.winpay.winpaysdk.main.message;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.webkit.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.main.helper.AppConfig;
import id.winpay.winpaysdk.main.helper.Logger;
import id.winpay.winpaysdk.util.Encrypt;
import id.winpay.winpaysdk.util.Mapper;
import id.winpay.winpaysdk.util.Networking;
import id.winpay.winpaysdk.util.Var;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class MessageSender implements Callback {
    @IntDef({FORMAT_JSON, FORMAT_FORM_ENCODED, FORMAT_PLAIN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageFormat {
    }

    @StringDef({GET, POST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HTTPMethod {
    }

    public static final byte FORMAT_JSON = 1, FORMAT_FORM_ENCODED = 2, FORMAT_PLAIN = 3;
    public static final String GET = "GET", POST = "POST";

    private final Context context;
    private final String root_url;
    private final HashMap<String, Object> message = new HashMap<>();
    private final HashMap<String, String> header = new HashMap<>();

    private OkHttpClient okHttpClient;
    private MessageProcess messageProcess = null;
    private int data_type = FORMAT_FORM_ENCODED;
    private String request_method = GET;
    private String path = "", tag = "", encryption = "";

    public MessageSender(Context context) {
        this(context, "");
    }

    public MessageSender(Context context, String url) {
        this.context = context;
        if (!url.isEmpty() && URLUtil.isValidUrl(url)) {
            this.root_url = url;
        } else {
            this.root_url = AppConfig.getConfig(context).getURL();
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        if (call.isCanceled()) {
            Logger.getLogger(context).log(getTag(), "Response Failure: [---] " + e.getMessage());

            sendMessage(SuperMessage.RESULT_CANCELLED, context.getString(R.string.wpi__warning_connection_canceled));
        } else if (call.isExecuted()) {
            Logger.getLogger(context).log(getTag(), "Response Failure: [XXX] " + e.getMessage());

            sendMessage(SuperMessage.RESULT_UNKNOWN, context.getString(R.string.wpi__warning_connection_failure));
        } else {
            Logger.getLogger(context).log(getTag(), "Response Failure: [000] " + e.getMessage());

            sendMessage(SuperMessage.RESULT_UNKNOWN, context.getString(R.string.wpi__warning_connection_unknown));
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        ResponseBody body = response.body();
        Logger.getLogger(context).log(getTag(), "Response Header: " + response.headers().toMultimap());
        if (!response.isSuccessful()) {
            Logger.getLogger(context).log(getTag(), "Response Failure: [" + response.code() + "] " + response.message());
            if (response.code() == 408 || response.code() == 504) {
                sendMessage(String.valueOf(response.code()), context.getString(R.string.wpi__warning_connection_timeout));
            } else {
                String message = response.message();
                if (body != null) {
                    sendMessage(String.valueOf(response.code()), body.string());
                } else if (message != null && !message.isEmpty()) {
                    sendMessage(String.valueOf(response.code()), message);
                } else {
                    sendMessage(String.valueOf(response.code()),
                            String.format(context.getString(R.string.wpi__warning_connection_denied), response.code()));
                }
            }
        } else if (body != null) {
            String result = body.string();
            Logger.getLogger(context).log(getTag(), "Response Success: " + result);

            response.close();

            try {
                JSONObject root = new JSONObject(result);
                SuperMessage msg = new SuperMessage(Mapper.jsonToMap(root));

                sendMessage(msg);
            } catch (JSONException e) {
                Logger.getLogger(context).log(getTag(), "Response Failure: Respon is not JSON", e);

                sendMessage(SuperMessage.RESULT_UNPARSED, result);
            }
        } else {
            sendMessage(SuperMessage.RESULT_UNKNOWN, context.getString(R.string.wpi__warning_connection_unknown));
        }
    }

    public void cancel() {
        String tag = getTag();
        for (Call call : getClientInstance().dispatcher().queuedCalls()) {
            if (tag == null || Var.toString(call.request().tag(), "").equals(tag)) {
                call.cancel();
            }
        }
        for (Call call : getClientInstance().dispatcher().runningCalls()) {
            if (tag == null || Var.toString(call.request().tag(), "").equals(tag)) {
                call.cancel();
            }
        }
    }

    public MessageProcess getMessageProcess() {
        return messageProcess;
    }

    public MessageSender setMessageProcess(MessageProcess messageProcess) {
        this.messageProcess = messageProcess;

        return this;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public int getData_type() {
        return data_type;
    }

    public MessageSender setData_type(@MessageFormat int data_type) {
        this.data_type = data_type;

        return this;
    }

    public String getRequest_method() {
        return request_method;
    }

    public MessageSender setRequest_method(@HTTPMethod String request_method) {
        this.request_method = request_method;

        return this;
    }

    public String getPath() {
        return path;
    }

    public MessageSender setPath(String path) {
        this.path = path;

        return this;
    }

    public HashMap<String, Object> getMessage() {
        return message;
    }

    public MessageSender addMessage(String key, Object value) {
        message.put(key, value);

        return this;
    }

    public MessageSender addMessage(HashMap<String, Object> msg) {
        message.putAll(msg);

        return this;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public MessageSender addHeader(String key, String value) {
        header.put(key, value);

        return this;
    }

    public MessageSender addHeader(HashMap<String, String> msg) {
        header.putAll(msg);

        return this;
    }

    public String getTag() {
        return tag.isEmpty() ? getPath() : tag;
    }

    public MessageSender setTag(String tag) {
        this.tag = tag;

        return this;
    }

    public void execute() {
        if (!Networking.isConnectingToInternet(context)) {
            sendMessage(SuperMessage.RESULT_NOK, context.getString(R.string.wpi__warning_connection_offline));
        } else {
            /*
             * PREPROCESS
             */
            final MessageProcess process = getMessageProcess();
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (process != null) {
                                process.beforeProcess(getTag());
                            }
                        }
                    });
                }
            } else if (process != null) {
                process.beforeProcess(getTag());
            }

            /*
             * PROCESS
             */
            Headers requestHeader = Headers.of(getHeader());
            RequestBody requestBody;
            String requestUrl = root_url + getPath();
            if (getRequest_method().equals(POST)) {
                MediaType mediaType;
                String mediaBody;
                switch (getData_type()) {
                    case FORMAT_JSON:
                        mediaType = MediaType.parse("application/json");
                        mediaBody = Mapper.mapToJSON(getMessage()).toString();
                        break;
                    case FORMAT_FORM_ENCODED:
                        mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        mediaBody = Mapper.mapToQueryString(getMessage());
                        break;
                    default:
                        mediaType = MediaType.parse("text/plain");
                        mediaBody = Mapper.mapToJSON(getMessage()).toString();
                        break;
                }
                Logger.getLogger(context).log(getTag(), "Request Message: " + mediaBody);
                if (!getEncryption().isEmpty()) {
                    try {
                        mediaBody = Encrypt.openssl_encrypt(mediaBody, getEncryption());
                        Logger.getLogger(context).log(getTag(), "Request Crypt: " + mediaBody);
                    } catch (Exception ignored) {
                    }
                }
                requestBody = RequestBody.create(mediaType, mediaBody);
            } else {
                String separator = "?";
                if (requestUrl.contains(separator)) {
                    separator = "&";
                }
                if (getData_type() == FORMAT_JSON) {
                    requestUrl += separator + Mapper.mapToJSON(getMessage()).toString();
                } else {
                    requestUrl += separator + Mapper.mapToQueryString(getMessage());
                }
                requestBody = null;
            }
            Logger.getLogger(context).log(getTag(), "Request URL: [" + getPath() + "] " + requestUrl);
            Logger.getLogger(context).log(getTag(), "Request Header: [" + getPath() + "] " + getHeader());

            try {
                Request request = new Request.Builder()
                        .headers(requestHeader)
                        .url(requestUrl)
                        .method(getRequest_method(), requestBody)
                        .tag(getTag())
                        .build();

                getClientInstance().newCall(request).enqueue(this);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(context).log(getTag(), "Response Failure: [ARG] " + e.getMessage());

                sendMessage(SuperMessage.RESULT_NOK, context.getString(R.string.wpi__warning_connection_offline));
            }
        }
    }

    private OkHttpClient getClientInstance() {
        if (okHttpClient == null) {
            int connect_timeout = 10;
            int read_timeout = 60;
            int write_timeout = 300;

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(connect_timeout, TimeUnit.SECONDS)
                    .readTimeout(read_timeout, TimeUnit.SECONDS)
                    .writeTimeout(write_timeout, TimeUnit.SECONDS)
                    .followRedirects(true)
                    .followSslRedirects(true);
            okHttpClient = Networking.enableTls12OnPreLollipop(clientBuilder).build();
        }
        return okHttpClient;
    }

    private void sendMessage(String rc, String rd) {
        SuperMessage msg = new SuperMessage();
        msg.setResponse_code(rc);
        msg.setMessageDescription(rd);

        sendMessage(msg);
    }

    private void sendMessage(final SuperMessage msg) {
        final MessageProcess process = getMessageProcess();
        if (context instanceof Activity) {
            if (!((Activity) context).isFinishing()) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (process != null) {
                            process.afterProcess(getTag(), msg);
                        }
                    }
                });
            }
        } else if (process != null) {
            process.afterProcess(getTag(), msg);
        }
    }

    public interface MessageProcess {

        void beforeProcess(String tag);

        void afterProcess(String tag, SuperMessage msg);

    }

}
