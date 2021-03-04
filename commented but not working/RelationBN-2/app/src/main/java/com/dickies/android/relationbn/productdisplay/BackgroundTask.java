package com.dickies.android.relationbn.productdisplay;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * NOT USED??
 * Gets all of the products and displays them in order of bin location when a user clicks on the search fragment
 */
public class BackgroundTask extends Activity {

    class BackgroundTaskReader extends AsyncTask<Void, Void, String> {
        String json_url;
        String JSON_String;

        @Override
        protected void onPreExecute() {
            json_url = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/getproductlist.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

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

                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            //TextView textView = (TextView) findViewById(R.id.Product);
            //textView.setText(result);
            JSON_String = result;
        }

        public void parseJSON(View view) {
            if (JSON_String == null) {
                Toast.makeText(getApplicationContext(), "There are no Products to view", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("json_data", JSON_String);
                startActivity(intent);
            }
        }
    }
}

