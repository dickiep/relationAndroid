package com.dickies.android.relationbn.registerlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.productdisplay.ApiClient;
import com.dickies.android.relationbn.productdisplay.ApiInterface;
import com.dickies.android.relationbn.productdisplay.HomeActivity;
import com.dickies.android.relationbn.utils.Config;
import com.dickies.android.relationbn.utils.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends Activity implements View.OnClickListener {

    //Defining views
    private EditText editTextEmail;
    private EditText editTextPassword;
    private AppCompatButton buttonLogin;
    private TextView linkSignup;
    public static Config config = new Config();
    private ArrayList<User> users;
    private ApiInterface apiInterface;
    public static User currentUser = new User();

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);

        //Adding click listener
        buttonLogin.setOnClickListener(this);

        linkSignup = (TextView) findViewById(R.id.linkSignup);
        Intent intent = new Intent(this, RegisterActivity.class);
        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void login(){
        //Getting values from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        currentUser.setUserName(email);
        Log.d("Username 1 : ", email);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.ADMIN_LOGGEDIN_SHARED_PREF,"NotLoggedIn");
                            editor.putBoolean(Config.ADMIN_LOGGEDIN_SHARED_PREF, false);
                            editor.putString(Config.EMAIL_SHARED_PREF, email);


                            config.setADMIN(false);

                            //Saving values to editor
                            editor.commit();

                            //fetchUserID("pdickie01@qub.ac.uk");

                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }else if(response.equalsIgnoreCase(Config.ADMIN_LOGIN_SUCCESS)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putBoolean(Config.ADMIN_LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.EMAIL_SHARED_PREF, email);

                            config.setADMIN(true);

                            //Saving values to editor
                            editor.commit();

                            //fetchUserID("pdickie01@qub.ac.uk");


                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                            Toast.makeText(LoginActivity.this,"Logged in as admin", Toast.LENGTH_LONG).show();
                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        //Calling the login function
        login();
    }


    User user = new User();
    public void fetchUserID(String key) {
        //final LoginActivity activity = (LoginActivity) getActivity();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<User>> call = apiInterface.getUser(key);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, retrofit2.Response<ArrayList<User>> response) {
                users = response.body();

                //Log.d("1431", user.toString());
                int userid = Integer.parseInt(users.toString());
                Toast.makeText(LoginActivity.this, users.toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error :"+t.toString(),Toast.LENGTH_LONG).show();
                Log.d("Error",t.toString());
            }
        });
    }
}

