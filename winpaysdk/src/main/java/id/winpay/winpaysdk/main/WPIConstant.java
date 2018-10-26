package id.winpay.winpaysdk.main;

import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Dian Cahyono on 05/10/18.
 */
public final class WPIConstant {

    @IntDef({RESULT_OK, RESULT_EMPTY_WPI_OBJECT, RESULT_INVALID_TOKEN_RESPONSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomActivityResult {
    }

    public static final int RESULT_OK = AppCompatActivity.RESULT_OK;
    public static final int RESULT_EMPTY_WPI_OBJECT = 13;
    public static final int RESULT_INVALID_TOKEN_RESPONSE = 14;

    public static final int REQUEST_WEB_WPI = 8711;

}
