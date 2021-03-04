package com.dickies.android.relationbn.productdisplay;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.registerlogin.LoginActivity;
import com.dickies.android.relationbn.utils.Category;
import com.dickies.android.relationbn.utils.Config;
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

public class CategoriesFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";

    private static final String PRODUCT_URL = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/getproductlist.php";
    RecyclerView recyclerView;
    String JSON_String;
    private ArrayList<Category> categories;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mAdapter;
    private TextView logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final HomeActivity activity = (HomeActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_product_categories, container, false);

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        logout = (TextView)  view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCategories);

        BackgroundTaskReader backgroundTaskReader = new BackgroundTaskReader(activity);
        backgroundTaskReader.execute();

        return view;
    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.putBoolean(Config.ADMIN_LOGGEDIN_SHARED_PREF, false);
                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



    ApiInterface apiInterface;
    private ArrayList<Product> products;

    public void fetchProducts(int key) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Product>> call = apiInterface.getProductsByCategory(key);
        Log.d(TAG, Integer.toString(key));

        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                final HomeActivity activity = (HomeActivity) getActivity();
                products = response.body();

               // Log.d(TAG, "phild"+products.toString());

                //Intent intent  = new Intent(getContext(), CategoryProductsDisplayActivity.class);
                //intent.putExtra("products",products.toString());

                //startActivity(intent);


                //CustomDialog customDialog = new CustomDialog(products);
               // customDialog.show(getFragmentManager(),"");

                //display these products in a dialog
               // DialogFragment dialogFragment = new DialogFragment();
                //dialogFragment.setShowsDialog(true);
                //dialogFragment.show(getFragmentManager(),"Alert");


                /*

                layoutManager = new LinearLayoutManager(activity);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                mAdapter = new ProductAdapter(products, activity);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                */

            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                //Toast.makeText(activity, "Error :"+t.toString(),Toast.LENGTH_LONG).show();
                Log.d("Error",t.toString());
            }
        });
    }

    class BackgroundTaskReader extends AsyncTask<Void, Category, Void> {
        Context ctx;
        Activity activity;
        RecyclerView.Adapter adapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<Category> arrayList = new ArrayList<>();
        String json_url = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/getcategorylist.php";

        public BackgroundTaskReader(Context ctx) {
            this.ctx = ctx;
            activity = (Activity) ctx;
        }

        @Override
        protected void onPreExecute() {
            layoutManager = new LinearLayoutManager(ctx);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new CategoryAdapter(arrayList, ctx);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseJSON();
            return null;
        }

        protected void onProgressUpdate(Category... values) {
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
                String name, description;
                int id;

                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    count++;

                    id = JO.getInt("id");
                    name = JO.getString("category");
                    description = JO.getString("description");
                    //location = JO.getString("location");
                    //image = R.drawable.ic_android;
                    Category category = new Category(id, name, description);
                    publishProgress(category);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
