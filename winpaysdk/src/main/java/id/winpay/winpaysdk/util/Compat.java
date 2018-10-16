package id.winpay.winpaysdk.util;

import android.annotation.TargetApi;
import android.text.Html;
import android.text.Spanned;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.N;

/**
 * Created by Dian Cahyono on 09/10/18.
 */
public final class Compat {

    public static Spanned Html_fromHtml(String source) {
        if (SDK_INT >= N) {
            return Html_fromHtml_uN(source);
        } else {
            return Html_fromHtml_lN(source);
        }
    }

    @TargetApi(N)
    private static Spanned Html_fromHtml_uN(String source) {
        return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
    }

    @SuppressWarnings("deprecation")
    private static Spanned Html_fromHtml_lN(String source) {
        return Html.fromHtml(source);
    }

}
