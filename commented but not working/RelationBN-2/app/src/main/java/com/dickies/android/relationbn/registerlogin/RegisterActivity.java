package com.dickies.android.relationbn.registerlogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.dickies.android.relationbn.R;

/**
 * Created by Phil on 30/09/2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail;
    EditText editTextPassword;
    AppCompatButton buttonRegister;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);

        //Adding click listener
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        String method = "register";
        com.dickies.android.relationbn.registerlogin.BackgroundTask backgroundTask = new com.dickies.android.relationbn.registerlogin.BackgroundTask(this);
        backgroundTask.execute(method, email, password);
        finish();

    }
}
