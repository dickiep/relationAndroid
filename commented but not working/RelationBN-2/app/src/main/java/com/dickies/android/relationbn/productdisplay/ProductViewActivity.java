package com.dickies.android.relationbn.productdisplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.registerlogin.LoginActivity;

/**
 * Created by Phil on 07/08/2018.
 */

public class ProductViewActivity extends Activity {

    private static final String TAG = "ProductViewActivity";

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(LoginActivity.config.isADMIN()) {
            setContentView(R.layout.activity_view_product);
        } else {
            setContentView(R.layout.activity_view_product_alt);
        }
        getIncomingIntent();

    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for intents");

        if (getIntent().hasExtra("productID") && getIntent().hasExtra("imageURL") && getIntent().hasExtra("productDescription") && getIntent().hasExtra("productCode") && getIntent().hasExtra("productLocation") && getIntent().hasExtra("productBarcode")) {
            Log.d(TAG, "getIncomingIntent: found extras");

            String imageURL = getIntent().getStringExtra("imageURL");
            String productDescription = getIntent().getStringExtra("productDescription");
            String productCode = getIntent().getStringExtra("productCode");
            String productLocation = getIntent().getStringExtra("productLocation");
            String productID = getIntent().getStringExtra("productID");
            String productBarcode = getIntent().getStringExtra("productBarcode");

            Log.d(TAG, "getIncomingIntent: found barcode"+productBarcode);

            setProduct(imageURL,productDescription,productCode,productLocation,productBarcode);

            if(LoginActivity.config.isADMIN()) {
                Button editProduct = findViewById(R.id.editProduct);


                editProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ProductEditActivity.class);
                        intent.putExtra("productID", productID);
                        intent.putExtra("imageURL", imageURL);
                        intent.putExtra("productDescription", productDescription);
                        intent.putExtra("productCode", productCode);
                        intent.putExtra("productLocation", productLocation);
                        view.getContext().startActivity(intent);
                    }
                });

            }

        }
    }

    private void setProduct(String imageURL, String productDescription, String productCode, String productLocation, String productBarcode) {
        Log.d(TAG, "setProduct: setting the widgets");

        TextView description = findViewById(R.id.product_description);
        description.setText(productDescription);
        TextView code = findViewById(R.id.product_code);
        code.setText(productCode);
        TextView location = findViewById(R.id.product_location);
        location.setText(productLocation);
        TextView barcode = findViewById(R.id.product_barcode);
        barcode.setText(productBarcode);
        ImageView imageView = findViewById(R.id.product_image);

        Glide.with(this).asBitmap().load(imageURL).into(imageView);

    }
}
