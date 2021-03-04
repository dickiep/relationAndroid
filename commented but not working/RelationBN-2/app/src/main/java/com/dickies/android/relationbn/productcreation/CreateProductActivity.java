package com.dickies.android.relationbn.productcreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.productdisplay.HomeActivity;
import com.dickies.android.relationbn.registerlogin.LoginActivity;
import com.dickies.android.relationbn.utils.BottomNavigationViewHelper;
import com.dickies.android.relationbn.utils.Config;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Displays the form through which users can enter new products to the system
 */
public class CreateProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * Constant that allows the activity to be shown when the user selects it in the bottom navigation
     */
    private static final int ACTIVITY_NUM = 1;

    /**
     * Edit text widgets for the user to enter the a activity_product_create description, code and barcode
     */
    EditText mName, mCode, mBarcode;

    /**
     * Spinner widget through which the user can select a activity_product_create location
     */
    Spinner BinSpinner;

    /**
     * String variables to hold the activity_product_create name, code, location and barcode
     */
    String name, code, location, barcode;

    private Toolbar toolbar;

    /**
     * Sets the activity's layout, instantiates the instance variables, sets the bottom navigation and allows the
     * product location spinner to operate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(LoginActivity.config.isADMIN()) {

        setContentView(R.layout.activity_product_create);
        mName = (EditText) findViewById(R.id.product_title);
        mCode = (EditText) findViewById(R.id.product_code);
        mBarcode = (EditText) findViewById(R.id.product_barcode);
        Spinner spinner = (Spinner) findViewById(R.id.area_spinner);
        BinSpinner = (Spinner) findViewById(R.id.bin_location_spinner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + "Product Creation" + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupBottomNavigationView();

        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shop_areas_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        } else {
            Log.d("Create Product Activity", Config.ADMIN_LOGGEDIN_SHARED_PREF);
            Intent intent = new Intent(CreateProductActivity.this, HomeActivity.class);
            startActivity(intent);
            Toast.makeText(CreateProductActivity.this,"Contact your System Administrator", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets the product's location when a spinner item is selected
     * @param parent
     * @param view
     * @param pos
     * @param id
     */
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String item = parent.getItemAtPosition(pos).toString();

        if (item.equals("Front Shop")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.front_shop_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);
        } else if (item.equals("Middle Shop")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.middle_shop_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);
        } else if (item.equals("Back Shop")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.back_shop_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);

        }else if (item.equals("Plumbing")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.plumbing_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);

        }else if (item.equals("Showroom")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.showroom_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);

        }else if (item.equals("Front Yard")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.front_yard_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);

        }else if (item.equals("Heavy Building Materials Store")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.heavy_building_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);

        }else if (item.equals("Back Yard")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.back_yard_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);

        }else if (item.equals("Radiator Store")) {
            ArrayAdapter<CharSequence> BinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.radiator_store_bins_array, android.R.layout.simple_spinner_item);
            BinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            BinSpinner.setAdapter(BinAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Sets up the bottom navigation
     */
    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(CreateProductActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /**
     * Calls the BackgroundTask class to create the product
     * @param view
     */
    public void productCreation(View view) {
        name = mName.getText().toString();
        code = mCode.getText().toString();
        location = BinSpinner.getSelectedItem().toString();
        barcode = mBarcode.getText().toString();
        String method = "creation";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, name, code,barcode, location);
        finish();
    }
}
