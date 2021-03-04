package com.dickies.android.relationbn.productdisplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.googleBarcodeReader.BarcodeCaptureActivity;
import com.dickies.android.relationbn.utils.Product;
import com.google.android.gms.common.api.CommonStatusCodes;

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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Creates the Home Fragment which allows users to search through a list of the stores products
 */
public class HomeFragment extends Fragment {

    /**
     * Tag used for the Log
     */
    private static final String TAG = "HomeFragment";

    private static final int RC_BARCODE_CAPTURE = 9001;

    /**
     * Recyclerview in which all of the products will be displayed
     */
    private RecyclerView recyclerView;

    /**
     * The database will return a JSON String which can be parsed and used to populate each of the products
     */
    private String JSON_String;

    /**
     * A user can enter a string in this SeachView and the database will be searched for matching strings
     */
    private SearchView search;

    /**
     * A user can hit this button to use the google barcode reader, any barcode that is detected will populate the SearchView
     */
    private ImageButton buttonBarcode;

    /**
     * A user can hit this button to use google speech reading technology, any string that is detected will populate the SearchView
     */
    private ImageButton buttonAudio;

    /**
     * The ApiInterface is used to search the database for specific strings
     */
    private ApiInterface apiInterface;

    /**
     * All products that are returned from the database and parsed will be added to this Arraylist which will populate the RecyclerView
     */
    private ArrayList<Product> products;

    /**
     * The product adapter converts each product's data into a layout that can be used to populate the RecyclerView
     */
    private ProductAdapter mAdapter;

    /**
     * The layout manager positions each prodct within the RecyclerView
     */
    RecyclerView.LayoutManager layoutManager;

    /**
     * Instantiates the HomeActivity, inflates the HomeFragment and it's various elements within it
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final HomeActivity activity = (HomeActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_product_display, container, false);

        //Instantiating the elements of the home fragment
        search=(SearchView) view.findViewById(R.id.searchView1);
        search.setQueryHint("Search...");
        search.setIconifiedByDefault(false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        buttonBarcode = (ImageButton) view.findViewById(R.id.imageButton2);
        buttonAudio = (ImageButton) view.findViewById(R.id.imageButton1);
        //User user = new User();

        //fetchUserID("dickiep@tcd.ie");

        //Setting an onclick listener for audio
        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What can we help you with?");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent,2);
            }
        });

        //setting an onclick listener for barcodes
        buttonBarcode.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        //If a user has not seached for anything then the following backround tasks returns a list of all the products
        BackgroundTaskReader backgroundTaskReader = new BackgroundTaskReader(activity);
        backgroundTaskReader.execute();

        //As a user types in the search view the database will be search for equivalent strings
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 3)
                {
                    recyclerView.setVisibility(view.VISIBLE);
                    fetchProducts(newText);
                }
                else if (newText.length() == 3)
                {
                    BackgroundTaskReader backgroundTaskReader = new BackgroundTaskReader(activity);
                    backgroundTaskReader.execute();
                }
                return false;
            }
        });
        return view;
    }

    /**
     * Interacts with the database using the string entered by the user in the SearchView.
     * Displays the results in the RecyclerView.
     * @param key
     */
    public void fetchProducts(String key) {
        final HomeActivity activity = (HomeActivity) getActivity();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Product>> call = apiInterface.getProducts(key);

        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                products = response.body();
                layoutManager = new LinearLayoutManager(activity);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                mAdapter = new ProductAdapter(products, activity);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                //Toast.makeText(activity, "Error :"+t.toString(),Toast.LENGTH_LONG).show();
                Log.d("Error",t.toString());
            }
        });
    }

    /**
     * Handles the results from the barcode capture and audio capture, setting the searchview with results of both.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1)
        {
            String barcode=data.getStringExtra("BARCODE");
            if (barcode.equals("NULL"))
            {
                //that means barcode could not be identified or user pressed the back button
                //do nothing
            }
            else
            {
                search.setQuery(barcode, true);
                search.setIconifiedByDefault(false);
            }
        }

        if (requestCode == 2) {
            final HomeActivity activity = (HomeActivity) getActivity();
            ArrayList<String> speechResults;

            if(data!=null) {
                speechResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(activity, "You searched for : " + speechResults.get(0), Toast.LENGTH_LONG).show();
                String text = speechResults.get(0).replace("'", "");
                search.setQuery(text, true);
                search.setIconifiedByDefault(false);
            }
        }

        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    com.google.android.gms.vision.barcode.Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    search.setQuery("^"+barcode.displayValue, true);
                    search.setIconifiedByDefault(false);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    //statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                //statusMessage.setText(String.format(getString(R.string.barcode_error),
                        //CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
    }

    /**
     * Reads all products from the Database
     */
    class BackgroundTaskReader extends AsyncTask<Void, Product, Void> {
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

        /**
         * Creates the Recyclerview so that it's ready to be populated with data retrieved from the database
         */
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

        /**
         * Connects to the database, runs a query that returns all of the products.
         * The JSON string is then used to create an array of product objects.
         */
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
                int id, stock;

                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    count++;

                    id = JO.getInt("id");
                    name = JO.getString("name");
                    code = JO.getString("code");
                    location = JO.getString("barcode");
                    image = JO.getString("image");
                    barcode = JO.getString("barcode1");
                    stock = JO.getInt("stock");
                    Log.d(TAG, "parseJSON in Home Fragment: "+stock);
                    Product product = new Product(name, code, location, image, id, barcode, stock);
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

    }

}

