package id.winpay.winpaysdk.main.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

import id.winpay.winpaysdk.R;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class WPILoadingView extends AppCompatImageView {
    public WPILoadingView(Context context) {
        super(context);

        init();
    }

    public WPILoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public WPILoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        Glide.with(this)
                .asGif()
                .load(R.drawable.wpi__loading_anim)
                .into(this);
    }
}
