package com.dickies.android.relationbn.productdisplay;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.registerlogin.LoginActivity;
import com.dickies.android.relationbn.utils.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phil on 26/07/2018.
 */

public class CheckoutFragment extends Fragment {
    private static final String TAG = "CheckoutFragment";

    RecyclerView recyclerView;
    String JSON_String;
    private ArrayList<Product> products;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductCartAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private ApiInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final HomeActivity activity = (HomeActivity) getActivity();

        int userID = 1;

        View view = inflater.inflate(R.layout.fragment_product_checkout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCart);
        //CheckoutFragment.BackgroundTaskReader backgroundTaskReader = new CheckoutFragment.BackgroundTaskReader(activity);
        //backgroundTaskReader.execute();
        //fetchCart(userID);
        String userName = LoginActivity.currentUser.getUserName();
        //Log.d("Username : ", userName);
        fetchCartEmail(userName);

        return view;
    }

    public void fetchCartEmail(String userEmail) {
        final HomeActivity activity = (HomeActivity) getActivity();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Product>> call = apiInterface.getCartEmail(userEmail);

        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                products = response.body();

                Log.d(TAG, products.toString());

                layoutManager = new LinearLayoutManager(activity);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                mAdapter = new ProductCartAdapter(products, activity);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Toast.makeText(activity, "Error :"+t.toString(),Toast.LENGTH_LONG).show();
                Log.d("Error",t.toString());
            }
        });
    }

    public void fetchCart(int userID) {
        final HomeActivity activity = (HomeActivity) getActivity();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Product>> call = apiInterface.getCart(userID);

        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                products = response.body();

                Log.d(TAG, products.toString());

                layoutManager = new LinearLayoutManager(activity);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                mAdapter = new ProductCartAdapter(products, activity);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Toast.makeText(activity, "Error :"+t.toString(),Toast.LENGTH_LONG).show();
                Log.d("Error",t.toString());
            }
        });
    }


class BackgroundTaskReader extends AsyncTask<Void, Product, Void> {

    RecyclerView recyclerView;
    String JSON_String;
    Context ctx;
    Activity activity;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Product> arrayList = new ArrayList<>();
    String json_url = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/getproductlist.php";

    public BackgroundTaskReader(Context ctx) {
        this.ctx = ctx;
        activity = (Activity) ctx;
    }

    @Override
    protected void onPreExecute() {
        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ProductAdapter(arrayList, ctx);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        parseJSON();
        return null;
    }

    protected void onProgressUpdate(Product... values) {
        arrayList.add(values[0]);
        adapter.notifyDataSetChanged();
    }

    protected void onPostExecute(String result) {

    }

    public void parseJSON() {
        try {
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((JSON_String = br.readLine()) != null) {
                stringBuilder.append(JSON_String + "\n");
            }

            br.close();
            inputStream.close();
            httpURLConnection.disconnect();
            String json_string = stringBuilder.toString().trim();
            Log.d("JSON_STRING", json_string);
            JSONObject jsonObject = new JSONObject(json_string);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String name, code, location;
            String image, barcode;
            int id;

            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                count++;

                id = JO.getInt("id");
                name = JO.getString("title");
                code = JO.getString("code");
                location = JO.getString("location");
                image = JO.getString("image");
                barcode = JO.getString("barcode1");
                Product product = new Product(name, code, location, image, id, barcode, 5);
                publishProgress(product);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}}
