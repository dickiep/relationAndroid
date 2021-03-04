package com.dickies.android.relationbn.productdisplay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.dickies.android.relationbn.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Phil on 19/07/2018.
 */

public class DisplayRecyclerView extends Activity {

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProductAdapter mProductAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //mProductAdapter = new ProductAdapter(R.layout.row_layout);
        mRecyclerView.setAdapter(mProductAdapter);
        json_string = getIntent().getExtras().getString("json_data");


        try {
            jsonObject = new JSONObject(json_string);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String name, code, location;
            int image;
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);

                name = JO.getString("name");
                code = JO.getString("code");
                location = JO.getString("barcode");
                image = R.drawable.ic_android;


                Product activity_product_create = new Product(name, code, location, image);
                //publishProgress(activity_product_create);
                //mProductAdapter.add(activity_product_create);

                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }


}
