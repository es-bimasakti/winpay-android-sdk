package id.winpay.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.winpay.winpaysdk.main.activity.WPIToolbarImplActivity;
import id.winpay.winpaysdk.main.helper.AffinityHelper;
import id.winpay.winpaysdk.main.helper.DialogHelper;
import id.winpay.winpaysdk.main.item.WPIItem;
import id.winpay.winpaysdk.main.item.WPIObject;
import id.winpay.winpaysdk.main.item.WPIResponse;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public class MainActivity extends AppCompatActivity {
    private final int REQUEST_SPI_TOOLBAR = 8787;

    @BindView(R.id.spi_merchant_transaction_reff)
    EditText spi_merchant_transaction_reff;
    @BindView(R.id.spi_billingName)
    EditText spi_billingName;
    @BindView(R.id.spi_billingPhone)
    EditText spi_billingPhone;
    @BindView(R.id.spi_billingEmail)
    EditText spi_billingEmail;
    @BindView(R.id.spi_item_name)
    EditText spi_item_name;
    @BindView(R.id.spi_item_price)
    EditText spi_item_price;
    @BindView(R.id.spi_item_description)
    EditText spi_item_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        spi_merchant_transaction_reff.setText(String.valueOf(System.currentTimeMillis()));
        spi_billingName.setText("Sukiwo");
        spi_billingPhone.setText("08123456777");
        spi_billingEmail.setText("sukiwo@kiwotengen.com");
        spi_item_name.setText("Pencuci closet");
        spi_item_price.setText("12300");
    }

    @OnClick(R.id.btn_submit)
    public void doSubmit() {
        String ref = spi_merchant_transaction_reff.getText().toString();
        String name = spi_billingName.getText().toString();
        String phone = spi_billingPhone.getText().toString();
        String email = spi_billingEmail.getText().toString();
        String iname = spi_item_name.getText().toString();
        String description = spi_item_description.getText().toString();
        int price = spi_item_price.length() > 0 ? Integer.parseInt(spi_item_price.getText().toString()) : 0;

        if (ref.isEmpty()) {
            Toast.makeText(this, String.format("Mohon isi kolom %s", spi_merchant_transaction_reff.getHint()), Toast.LENGTH_LONG).show();
        } else if (name.isEmpty()) {
            Toast.makeText(this, String.format("Mohon isi kolom %s", spi_billingName.getHint()), Toast.LENGTH_LONG).show();
        } else if (phone.isEmpty()) {
            Toast.makeText(this, String.format("Mohon isi kolom %s", spi_billingPhone.getHint()), Toast.LENGTH_LONG).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, String.format("Mohon isi kolom %s", spi_billingEmail.getHint()), Toast.LENGTH_LONG).show();
        } else if (iname.isEmpty()) {
            Toast.makeText(this, String.format("Mohon isi kolom %s", spi_item_name.getHint()), Toast.LENGTH_LONG).show();
        } else if (price <= 0) {
            Toast.makeText(this, String.format("Mohon isi kolom %s", spi_item_price.getHint()), Toast.LENGTH_LONG).show();
        } else {
            WPIItem item = new WPIItem(iname, price);
            item.setQty(1);
            item.setWeight(1000);

            WPIObject object = new WPIObject();
            object.setSpi_merchant_transaction_reff(ref);
            object.setSpi_billingName(name);
            object.setSpi_billingPhone(phone);
            object.setSpi_billingEmail(email);
            object.setSpi_billingDescription(description);
            object.addSpi_item(item);

            WPIToolbarImplActivity.start(this, REQUEST_SPI_TOOLBAR, object, "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SPI_TOOLBAR && data != null) {
            WPIResponse response = AffinityHelper.intentToResponse(data);
            if (response != null) {
                DialogHelper.toast(this, response.getResponse_desc());
            } else {
                DialogHelper.toast(this, getString(R.string.warning_unknown));
            }
        }
    }
}
