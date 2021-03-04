package com.dickies.android.relationbn.productdisplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dickies.android.relationbn.mapdisplay.MapActivity;
import com.dickies.android.relationbn.registerlogin.LoginActivity;
import com.dickies.android.relationbn.utils.Product;
import com.dickies.android.relationbn.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Phil on 13/07/2018.
 */

/**
 * RecyvlerView.Adapter
 * RecyvlerView.Viewholder
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    ArrayList<Product> arrayList = new ArrayList<>();
    Context ctx;


    public ProductAdapter(ArrayList<Product> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(LoginActivity.config.isADMIN()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
            ProductViewHolder productViewHolder = new ProductViewHolder(view,ctx,arrayList);
            return productViewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_alt, parent, false);
            ProductViewHolder productViewHolder = new ProductViewHolder(view,ctx,arrayList);
            return productViewHolder;
        }


       // return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = arrayList.get(position);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ctx,activity_product_create.getId(),Toast.LENGTH_LONG).show();
                String id = Integer.toString(product.getId());
                Intent intent = new Intent(ctx, ProductViewActivity.class);
                intent.putExtra("productID",id);
                intent.putExtra("imageURL", product.getImage());
                intent.putExtra("productDescription", product.getName());
                intent.putExtra("productCode",product.getCode());
                intent.putExtra("productLocation",product.getLocation());
                intent.putExtra("productBarcode",product.getBarcode());
                //Log.d("ProductAdapter", "onClick: barcode"+product.getBarcode());
                ctx.startActivity(intent);

            }
        });
        holder.title.setText(product.getName());
        holder.location.setText(product.getLocation());
        holder.code.setText(product.getCode());
        Glide.with(ctx).load(product.getImage()).into(holder.imageView);
        holder.mapButton.setImageResource(R.drawable.ic_place_marker);
        holder.slash.setImageResource(R.drawable.ic_slash);
        holder.basketButton.setImageResource(R.drawable.ic_basket_add);
        holder.basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add this activity_product_create to an arraylist and display this arraylist in the checkout fragment. will it diassappear maybe sqlite
                String id = Integer.toString(product.getId());
                String userID = "1";

                String method = "cartaddition";
                BackgroundTask backgroundTask = new BackgroundTask(ctx, method);
                backgroundTask.execute(id, userID);
            }

        });


        /*
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
                R.array.stock_levels, android.R.layout.select_dialog_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        // Apply the adapter to the spinner
        holder.stock.setAdapter(adapter);
        */

        //mspin=(Spinner) findViewById(R.id.spinner1);



        if(LoginActivity.config.isADMIN()) {

        Integer[] items = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(ctx,android.R.layout.simple_spinner_item, items);
        holder.stock.setAdapter(adapter);
        holder.stock.setSelection(product.getStock());


        final int[] currentSelection = {holder.stock.getSelectedItemPosition()};


            holder.stock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (currentSelection[0] != i){


                        String id = Integer.toString(product.getId());
                        String stock = Integer.toString(i);

                        String method = "stockcount";
                        BackgroundTask backgroundTask = new BackgroundTask(ctx, method);
                        backgroundTask.execute(id, stock);
                    }
                    currentSelection[0] = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
        //


        // Glide.with(mCtx).load(activity_product_create.getImage()).into(holder.imageView);
    }



    public Bitmap getBitmapFromUrl(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    /**
     *
     */
    static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, basketButton, mapButton, slash;
        TextView title, code, location;
        Spinner stock;
        ArrayList<Product> products = new ArrayList<>();
        Context ctx;
        int position = getAdapterPosition();

        //private final View.OnClickListener mOnClickListener = new MyOnClickListener(products,ctx);

        public ProductViewHolder(View view, Context ctx, ArrayList<Product> products) {
            super(view);
            this.products = products;
            this.ctx = ctx;
            imageView = view.findViewById(R.id.imageView);
            title = view.findViewById(R.id.textViewTitle);
            code = view.findViewById(R.id.textViewCode);
            location = view.findViewById(R.id.textViewLocation);
            basketButton = view.findViewById(R.id.basketButton);
            mapButton = view.findViewById(R.id.mapButton);
            slash = view.findViewById(R.id.slash);

            if(LoginActivity.config.isADMIN()) {
                stock = view.findViewById(R.id.stockSpinner);
            }

            view.setOnClickListener(this);
            //Log.d("ProductAdapter", "PVH Con: "+position);
        }



        @Override
        public void onClick(final View view) {
            int position = getAdapterPosition();
            Product product = this.products.get(position);
            String location = product.getLocation();
            Intent intent = new Intent(this.ctx,MapActivity.class);
            intent.putExtra("location",product.getLocation());
            intent.putExtra("title",product.getName());
            this.ctx.startActivity(intent);
            //int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            //String item = mList.get(itemPosition);
            //Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
        }
    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        Context mContext;
        String method;

        BackgroundTask(Context mContext, String method) {
            this.mContext = mContext;
            this.method = method;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            if (method.equals("cartaddition")) {

                String cart_url = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/cartaddition.php";

                int user = Integer.parseInt(params[1]);
                int product = Integer.parseInt(params[0]);
                int quantity = 1;

                try {
                    URL url = new URL(cart_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("user", "UTF-8") + "=" + user + " & " +
                            URLEncoder.encode("product", "UTF-8") + "=" + product + " & " +
                            URLEncoder.encode("quantity", "UTF-8") + "=" + quantity;
                    //Log.d("PA", "doInBackground: " + data);
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

            }

            if(method.equals("stockcount")) {

                Log.d("stockcount", " in here");

                String stock_count_url = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/stockcount.php";

                int stock = Integer.parseInt(params[1]);
                int product = Integer.parseInt(params[0]);
                //int quantity = 1;

                try {
                    URL url = new URL(stock_count_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("stock", "UTF-8") + "=" + stock + " & " +
                            URLEncoder.encode("product", "UTF-8") + "=" + product; // + " & " +
                            //URLEncoder.encode("quantity", "UTF-8") + "=" + quantity;
                    Log.d("PA", "doInBackground: " + data);
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
            }

            return "Item added";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String result) {

            if (method.equals("cartaddition")) {
                Toast.makeText(ctx, "Added to Cart", Toast.LENGTH_LONG).show();
            }
            if (method.equals("stockcount")) {
                Toast.makeText(ctx,"Stock Updated", Toast.LENGTH_LONG).show();
            }


            // Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        }

    }
}



