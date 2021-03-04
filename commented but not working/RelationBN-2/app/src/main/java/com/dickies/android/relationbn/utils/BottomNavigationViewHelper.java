package com.dickies.android.relationbn.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.dickies.android.relationbn.productcreation.CreateProductActivity;
import com.dickies.android.relationbn.productdisplay.HomeActivity;
import com.dickies.android.relationbn.mapdisplay.MapActivity;
import com.dickies.android.relationbn.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Phil on 17/07/2018.
 */

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHelper";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "Setting up bottom nav view");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.ic_search_products:
                        Intent intent1 = new Intent(context,HomeActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_add_product:
                        Intent intent2 = new Intent(context,CreateProductActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_open_map:
                        Intent intent3 = new Intent(context,MapActivity.class);
                        context.startActivity(intent3);
                        break;

                }
                return false;
            }
        });

    }

}
