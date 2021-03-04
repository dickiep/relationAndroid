package com.dickies.android.relationbn.productdisplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.utils.Product;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import retrofit2.Call;

/**
 * Created by Phil on 07/08/2018.
 */

public class ProductEditActivity extends Activity {

    private static final String TAG = "ProductEditActivity";
    private ApiInterface apiInterface;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        Log.d(TAG, "onCreate: started");
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for intents");

        if (getIntent().hasExtra("productID") && getIntent().hasExtra("imageURL") && getIntent().hasExtra("productDescription") && getIntent().hasExtra("productCode") && getIntent().hasExtra("productLocation")) {
            Log.d(TAG, "getIncomingIntent: found extras");

            String imageURL = getIntent().getStringExtra("imageURL");
            String productDescription = getIntent().getStringExtra("productDescription");
            String productCode = getIntent().getStringExtra("productCode");
            String productLocation = getIntent().getStringExtra("productLocation");
            String productID = getIntent().getStringExtra("productID");
            String productBarcode = getIntent().getStringExtra("productBarcode");

            setProduct(imageURL,productDescription,productCode,productLocation, productID);

        }
    }

    private void setProduct(String imageURL, String productDescription, String productCode, String productLocation, String productID) {
        Log.d(TAG, "setProduct: setting the widgets");

        EditText description = findViewById(R.id.product_description_edit);
        description.setText(productDescription);
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                description.equals(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        EditText code = findViewById(R.id.product_code_edit);
        code.setText(productCode);
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code.equals(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        EditText location = findViewById(R.id.product_location_edit);
        location.setText(productLocation);
        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                location.equals(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        EditText barcode = findViewById(R.id.product_barcode_edit);
        ImageView imageView = findViewById(R.id.product_image_edit);

        Glide.with(this).asBitmap().load(imageURL).into(imageView);

        Button editProduct = findViewById(R.id.updateProduct);

        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descriptionEdit = description.getText().toString();
                String codeEdit = code.getText().toString();
                String locationEdit = location.getText().toString();

                //Toast.makeText(view.getContext(),productID,Toast.LENGTH_LONG).show();
                BackgroundTask backgroundTask = new BackgroundTask(view.getContext());
                backgroundTask.execute(productID, imageURL, descriptionEdit, codeEdit, locationEdit);
                Intent intent = new Intent(view.getContext(),HomeActivity.class);
                view.getContext().startActivity(intent);

            }
        });

    }

    public void updateProduct(String key) {
        //final HomeActivity activity = (HomeActivity) getActivity();
        int intkey = Integer.parseInt(key);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Product> call = apiInterface.updateProduct(intkey);
        //Call<ArrayList<Product>> call = apiInterface.getProducts(key);

        };

    class BackgroundTask extends AsyncTask<String, Void, String> {

        Context mContext;

        BackgroundTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String update_url = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/updateproduct.php";

            int product = Integer.parseInt(params[0]);
            String imageURL = params[1];
            String productDescription = params[2];
            String productCode = params[3];
            String productLocation = params[4];

            try {
                URL url = new URL(update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("activity_product_create", "UTF-8") + "=" + product + " & " +
                        URLEncoder.encode("image", "UTF-8") + "=" + imageURL + " & " +
                        URLEncoder.encode("description", "UTF-8") + "=" + productDescription  + " & " +
                        URLEncoder.encode("code", "UTF-8") + "=" + productCode + " & " +
                        URLEncoder.encode("location", "UTF-8") + "=" + productLocation;
                Log.d("PEA", "doInBackground: "+data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Item added";
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(mContext, "Product Updated", Toast.LENGTH_LONG).show();
            // Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        }

    }
    }

