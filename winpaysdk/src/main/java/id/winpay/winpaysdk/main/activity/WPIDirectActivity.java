package id.winpay.winpaysdk.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import id.winpay.winpaysdk.R;
import id.winpay.winpaysdk.main.helper.AffinityHelper;
import id.winpay.winpaysdk.main.item.WPIResponse;
import id.winpay.winpaysdk.util.Var;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class WPIDirectActivity extends WPIActivity implements View.OnClickListener {

    private static final String WPI_PAYMENT_CODE = "payment_code";
    private static final String WPI_DESCRIPTION = "description";
    private static final String WPI_URL_STATUS = "url_status";
    private static final String WPI_TITLE = "title";
    private String payment_code, title, description, url_status;
    private TextView direct_payment_code;

    public static void start(WPIToolbarActivity activity, WPIResponse response) {
        String title = activity.getString(R.string.wpi__info_title_format, response.getPayment_method());

        Bundle bundle = new Bundle();
        bundle.putString(WPI_PAYMENT_CODE, response.getPayment_code());
        bundle.putString(WPI_DESCRIPTION, response.getResponse_desc());
        bundle.putString(WPI_URL_STATUS, response.getSpi_status_url());
        bundle.putString(WPI_TITLE, title);

        Intent intent = new Intent(activity, WPIDirectActivity.class);
        intent.putExtras(bundle);

        activity.overridePendingTransition(R.anim.wpi__fade_in, R.anim.wpi__fade_out);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wpi__activity_direct);

        if (savedInstanceState != null) {
            payment_code = savedInstanceState.getString(WPI_PAYMENT_CODE);
            description = savedInstanceState.getString(WPI_DESCRIPTION);
            url_status = savedInstanceState.getString(WPI_URL_STATUS);
            title = savedInstanceState.getString(WPI_TITLE);
        } else if (getIntent().getExtras() != null) {
            payment_code = getIntent().getExtras().getString(WPI_PAYMENT_CODE);
            description = getIntent().getExtras().getString(WPI_DESCRIPTION);
            url_status = getIntent().getExtras().getString(WPI_URL_STATUS);
            title = getIntent().getExtras().getString(WPI_TITLE);
        } else {
            payment_code = "";
            description = "";
            url_status = "";
            title = "";
        }

        String[] part = description.split(payment_code);

        TextView direct_content_1 = findViewById(R.id.wpi__direct_content_1);
        direct_content_1.setText(Var.getIgnoreBound(part, 0, ""));
        TextView direct_content_2 = findViewById(R.id.wpi__direct_content_2);
        direct_content_2.setText(Var.getIgnoreBound(part, 1, ""));

        direct_payment_code = findViewById(R.id.wpi__direct_payment_code);
        direct_payment_code.setText(payment_code);
        direct_payment_code.setOnClickListener(this);

        findViewById(R.id.wpi__direct_copy).setOnClickListener(this);
        findViewById(R.id.wpi__btn_visit).setOnClickListener(this);
        findViewById(R.id.wpi__btn_ok).setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(WPI_PAYMENT_CODE, payment_code);
        savedInstanceState.putString(WPI_DESCRIPTION, description);
        savedInstanceState.putString(WPI_URL_STATUS, url_status);
        savedInstanceState.putString(WPI_TITLE, title);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.wpi__direct_payment_code || view.getId() == R.id.wpi__direct_copy) {
            AffinityHelper.copy(this, direct_payment_code.getText().toString());
        } else if (view.getId() == R.id.wpi__btn_visit) {
            WPIWebActivity.start(this, title, url_status);
        } else {
            finish();
        }
    }
}
