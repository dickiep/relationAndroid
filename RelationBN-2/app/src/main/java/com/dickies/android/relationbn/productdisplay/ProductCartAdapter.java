package com.dickies.android.relationbn.productdisplay;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.mapdisplay.MapActivity;
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
import java.util.ArrayList;

/**
 * Created by Phil on 13/07/2018.
 */

/**
 * RecyvlerView.Adapter
 * RecyvlerView.Viewholder
 */

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ProductViewHolder> {

    ArrayList<Product> arrayList = new ArrayList<>();
    Context ctx;


    public ProductCartAdapter(ArrayList<Product> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view,ctx,arrayList);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = arrayList.get(position);

        holder.title.setText(product.getName());
        holder.location.setText(product.getLocation());
        holder.code.setText(product.getCode());
        //holder.imageView.setImageResource(R.drawable.ic_android);
        //Log.d("1044",product.getImage());
        Glide.with(ctx).load(product.getImage()).into(holder.imageView);
        holder.mapButton.setImageResource(R.drawable.ic_place_marker);
        holder.slash.setImageResource(R.drawable.ic_slash);
        holder.basketButton.setImageResource(R.drawable.ic_cart_remove);
        holder.basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = Integer.toString(product.getId());
                //Log.d("PCA", id);
                String userID = "1";

                ProductCartAdapter.BackgroundTask backgroundTask = new ProductCartAdapter.BackgroundTask(ctx);
                backgroundTask.execute(id, userID);

            }
        });


        // Glide.with(mCtx).load(activity_product_create.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    /**
     *
     */
    static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, basketButton, mapButton, slash;
        TextView title, code, location;
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
            slash = view.findViewById(R.id.slash);
            mapButton = view.findViewById(R.id.mapButton);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Product product = products.get(position);
                    String location = product.getLocation();

                    Intent intent = new Intent(ctx,MapActivity.class);
                    intent.putExtra("location",product.getLocation());
                    ctx.startActivity(intent);
                }
            });

        }

    }

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

            String cart_url = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/cartsubtraction.php";

            int user = Integer.parseInt(params[1]);
            int product = Integer.parseInt(params[0]);


            try {
                URL url = new URL(cart_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("user", "UTF-8") + "=" + user + " & " +
                        URLEncoder.encode("product", "UTF-8") + "=" + product + " & ";
                Log.d("PA", "doInBackground: "+data);
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
            return "Item removed";
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(ctx, "Removed from Cart", Toast.LENGTH_LONG).show();
            // Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        }

    }

}
