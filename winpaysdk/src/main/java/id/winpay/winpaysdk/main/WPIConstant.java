package id.winpay.winpaysdk.main;

import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Dian Cahyono on 05/10/18.
 */
public final class WPIConstant {

    @IntDef({RESULT_OK, RESULT_CANCELLED, RESULT_FIRST_USER, RESULT_EMPTY_WPI_OBJECT,
            RESULT_INVALID_TOKEN_RESPONSE, RESULT_NOT_AUTHORIZED_RESPONSE, RESULT_INVALID_URL,
            RESULT_FEATURE_NOT_SUPPORTED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomActivityResult {
    }

    public static final int RESULT_OK = AppCompatActivity.RESULT_OK;
    public static final int RESULT_CANCELLED = AppCompatActivity.RESULT_CANCELED;
    public static final int RESULT_FIRST_USER = AppCompatActivity.RESULT_FIRST_USER;
    public static final int RESULT_EMPTY_WPI_OBJECT = 13;
    public static final int RESULT_INVALID_TOKEN_RESPONSE = 14;
    public static final int RESULT_NOT_AUTHORIZED_RESPONSE = 15;
    public static final int RESULT_INVALID_URL = 16;
    public static final int RESULT_FEATURE_NOT_SUPPORTED = 17;

    public static final int REQUEST_WEB_WPI = 8711;

}
