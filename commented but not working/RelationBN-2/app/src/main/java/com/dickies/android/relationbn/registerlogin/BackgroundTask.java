package com.dickies.android.relationbn.registerlogin;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Writes new products to the Database
 */
public class BackgroundTask extends AsyncTask<String, Void, String> {

    /**
     * Context variable
     */
    Context mContext;

    /**
     * Constructor with context argument
     * @param mContext
     */
    BackgroundTask(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Background task pre execute method
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Writes new products to the database in the background
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        String creation_url = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/register.php";

        if (method.equals("register")) {
            String email = params[1];
            String password = params[2];
            try {
                URL url = new URL(creation_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("pword", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();

                //return "Product Creation Success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Registration Success";
    }

    /**
     * Handles progress updates
     * @param values
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Displays that the activity_product_create has successfully been added to the database
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
    }
}
