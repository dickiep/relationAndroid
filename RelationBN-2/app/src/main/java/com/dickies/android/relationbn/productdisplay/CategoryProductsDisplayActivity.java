package com.dickies.android.relationbn.productdisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.utils.Product;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phil on 20/08/2018.
 */

public class CategoryProductsDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private LinearLayoutManager layoutManager;
    private ProductAdapter mAdapter;
    private Intent mIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Log.d("POP Oncreate : ", "In here");
        setContentView(R.layout.popupwindow);

        mIntent = getIntent();
        int id = mIntent.getIntExtra("categoryID",3);
        String title = mIntent.getStringExtra("categoryTitle");
        //Log.d("POP Oncreate : ", String.valueOf(id));
        fetchProducts(id);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }

    ApiInterface apiInterface;
    private ArrayList<Product> products;

    public void fetchProducts(int key) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Product>> call = apiInterface.getProductsByCategory(key);

        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                products = response.body();
                //Log.d("POP Fetch : ", "phild"+products.toString());
                mAdapter = new ProductAdapter(products, getApplicationContext());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Log.d("Error",t.toString());
            }
        });
    }
}
