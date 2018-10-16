package id.winpay.winpaysdk.main.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.main.item.Channel;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelHolder> {

    @ColorInt
    private final int colorBGEven, colorBGOdd;
    private final int column_count;
    private final double holder_height;
    private final boolean is_sandbox;
    private final RequestBuilder<Bitmap> manager;
    private final Context context;
    private final List<Channel> channels;
    private OnChannelClickListener listener;

    public ChannelAdapter(Context context, List<Channel> channels, int column_count) {
        this.context = context;
        this.channels = channels;
        this.column_count = column_count;
        this.colorBGOdd = 0;
        this.colorBGEven = ResourcesCompat.getColor(context.getResources(), R.color.wpi__colorGridBackground, null);
        this.is_sandbox = context.getResources().getBoolean(R.bool.wpi__show_direct_label);

        if (context instanceof Activity) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screen_width = displayMetrics.widthPixels;
            int designated_width = context.getResources().getInteger(R.integer.wpi__toolbar_image_width);
            int designated_height = context.getResources().getInteger(R.integer.wpi__toolbar_image_height);

            double v_width = screen_width / (double) column_count;
            double r_width = v_width / (double) designated_width;
            this.holder_height = designated_height * r_width;
        } else {
            this.holder_height = 0;
        }

        RequestOptions options = RequestOptions.fitCenterTransform()
                .error(R.drawable.wpi__loading_error)
                .placeholder(R.drawable.wpi__loading_placeholder);
        this.manager = Glide.with(context).asBitmap().apply(options);
    }

    @NonNull
    @Override
    public ChannelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.wpi__component_toolbar_channel, null);

        return new ChannelHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelHolder holder, int position) {
        final Channel channel = channels.get(position);

        manager.load(channel.getPayment_logo()).into(holder.payment_logo);

        ViewGroup.LayoutParams param = holder.payment_logo.getLayoutParams();
        param.height = (int) holder_height;
        holder.payment_logo.setLayoutParams(param);
        holder.payment_name.setText(channel.getPayment_name());
        holder.payment_direct.setVisibility(is_sandbox && channel.isDirect() ? View.VISIBLE : View.GONE);

        holder.itemView.setBackgroundColor(getColorStrip(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onChannelClick(channel, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public void setListener(OnChannelClickListener listener) {
        this.listener = listener;
    }

    @ColorInt
    private int getColorStrip(int position) {
        if (column_count % 2 == 0) {
            if ((position / column_count) % 2 == 0) {
                return position % 2 == 0 ? colorBGEven : colorBGOdd;
            } else {
                return position % 2 == 0 ? colorBGOdd : colorBGEven;
            }
        } else {
            return position % 2 == 0 ? colorBGEven : colorBGOdd;
        }
    }

    static class ChannelHolder extends RecyclerView.ViewHolder {

        private final ImageView payment_logo;
        private final TextView payment_name;
        private final View payment_direct;

        ChannelHolder(View itemView) {
            super(itemView);

            payment_direct = itemView.findViewById(R.id.wpi__payment_direct);
            payment_logo = itemView.findViewById(R.id.wpi__payment_logo);
            payment_name = itemView.findViewById(R.id.wpi__payment_name);
        }
    }

    public interface OnChannelClickListener {
        void onChannelClick(Channel channel, int position);
    }

}
