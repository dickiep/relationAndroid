package com.dickies.android.relationbn.productdisplay;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Contains the category, search and cart fragments
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * Tag used for log messages
     */
    private static final String TAG = "DisplayListView";
    private static final int ACTIVITY_NUM = 0;

    /**
     * URL to php code which retrieves a list of products from the database
     */
    private static final String PRODUCT_URL = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/getproductlist.php";

    /**
     * Sets up the activity layout, the Bottom Navigation View (which allows the user to navigate between the three main activities)
     * and it sets up the three fragments in the Home Activity. This all occurs just after the user logs in and is directed to the Home Activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupBottomNavigationView();
        setupViewPager();
    }

    /**
     * Sets up the layout for the bottom navigation view, calls the required methods in BottomNavigationViewHelper,
     * Instantiates the bottom navigation view as a Menu and highlights in the Menu that we are in the home activity.
     *
     */
    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(HomeActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /**
     * Sets up three tabs in the Home Activity by adding three fragments to an arraylist in the SectionPagerAdapter
     * The ViewPager class uses this array list to allow users to scroll throught the tabs/fragments
     */
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CategoriesFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new CheckoutFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_basket);
    }

}
