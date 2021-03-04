package com.dickies.android.relationbn.mapdisplay;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.utils.BottomNavigationViewHelper;
import com.dickies.android.relationbn.utils.UserLocation;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import com.estimote.indoorsdk.EstimoteCloudCredentials;
import com.estimote.indoorsdk.IndoorLocationManagerBuilder;
import com.estimote.indoorsdk_module.algorithm.OnPositionUpdateListener;
import com.estimote.indoorsdk_module.algorithm.ScanningIndoorLocationManager;
import com.estimote.indoorsdk_module.cloud.CloudCallback;
import com.estimote.indoorsdk_module.cloud.CloudCredentials;
import com.estimote.indoorsdk_module.cloud.EstimoteCloudException;
import com.estimote.indoorsdk_module.cloud.IndoorCloudManager;
import com.estimote.indoorsdk_module.cloud.IndoorCloudManagerFactory;
import com.estimote.indoorsdk_module.cloud.Location;
import com.estimote.indoorsdk_module.cloud.LocationPosition;
import com.estimote.indoorsdk_module.view.IndoorLocationView;

/**
 * Displays the Map of the Property
 */
public class MapActivity extends Activity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final int ACTIVITY_NUM = 2;
    private MapView mapView;

    Context ctx;
    Location radStore;
    IndoorLocationView indoorView;
    Button locationButton;
    static ScanningIndoorLocationManager indoorLocationManager;


    private int REQUEST_ACCESS_FINE_LOCATION = 1;
    static double x=0, y=0;

    /**
     * Sets the Layout, gets an Instance of Mapbox maps and instantiates the Mapview
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiZGlja2llcC1nYiIsImEiOiJjampnd2V1NHAzZzc3M2tvM2FlOHgyd3JxIn0.z88nikUK6WIqQ42SgU4yhA");
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationButton = findViewById(R.id.locationButton);


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_main);
            indoorView = (IndoorLocationView) findViewById(R.id.indoor_view);
            ctx = MapActivity.this;

            // JAVA
            CloudCredentials cloudCredentials = new EstimoteCloudCredentials("helloindoor-8fz", "a71f835d237d19000206f4ffcfb3748f");
            //IndoorCloudManager cloudManager = new IndoorCloudManagerFactory().create(this, cloudCredentials);
            final IndoorCloudManager cm = new IndoorCloudManagerFactory().create(this,cloudCredentials);
            cm.getLocation("rad-store",new CloudCallback<Location>(){
                @Override
                public void success(Location location) {
                    // store the Location object for later,
                    // you will need it to initialize the IndoorLocationManager!
                    //
                    // you can also pass it to IndoorLocationView to display a map:
                    // indoorView = (IndoorLocationView) findViewById(R.id.indoor_view);
                    // indoorView.setLocation(location);

                    radStore = location;
                    indoorView.setLocation(radStore);
                    Log.d("Success",radStore.toString());
                    setIndoorLocationManager(radStore,getApplicationContext(), indoorView);

                }

                @Override
                public void failure(EstimoteCloudException e) {
                    // oops!
                }

            });
        }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACCESS_FINE_LOCATION);
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_main);
            indoorView = (IndoorLocationView) findViewById(R.id.indoor_view);
            ctx = MapActivity.this;

            // JAVA
            CloudCredentials cloudCredentials = new EstimoteCloudCredentials("helloindoor-8fz", "a71f835d237d19000206f4ffcfb3748f");
            //IndoorCloudManager cloudManager = new IndoorCloudManagerFactory().create(this, cloudCredentials);
            final IndoorCloudManager cm = new IndoorCloudManagerFactory().create(this,cloudCredentials);
            cm.getLocation("rad-store",new CloudCallback<Location>(){
                @Override
                public void success(Location location) {
                    // store the Location object for later,
                    // you will need it to initialize the IndoorLocationManager!
                    //
                    // you can also pass it to IndoorLocationView to display a map:
                    // indoorView = (IndoorLocationView) findViewById(R.id.indoor_view);
                    // indoorView.setLocation(location);

                    radStore = location;
                    indoorView.setLocation(radStore);
                    Log.d("Success",radStore.toString());
                    setIndoorLocationManager(radStore,getApplicationContext(), indoorView);

                }

                @Override
                public void failure(EstimoteCloudException e) {
                    // oops!
                }

            });
        }



    }

    public static void setIndoorLocationManager(Location location, Context ctx, final IndoorLocationView indoorView) {
        indoorLocationManager = new IndoorLocationManagerBuilder(ctx, location, new EstimoteCloudCredentials("helloindoor-8fz", "a71f835d237d19000206f4ffcfb3748f")).withDefaultScanner().build();
        indoorLocationManager.startPositioning();
        indoorLocationManager.setOnPositionUpdateListener(new OnPositionUpdateListener() {
            @Override
            public void onPositionUpdate(LocationPosition position) {
                // here, we update the IndoorLocationView with the current position,
                // but you can use the position for anything you want
                //indoorView.updatePosition(position);
                Log.d("OnPositionUpdate", position.toString());
                x = position.getX();
                y = position.getY();

            }

            @Override
            public void onPositionOutsideLocation() {
                indoorView.hidePosition();
                Log.d("OnOutside", "Outside");
                x = -1;
                y = -1;
            }
        });
    }

    /**
     * Sets up the Bottom Navigation Options
     */
    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(MapActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    UserLocation userLocation = new UserLocation();

    /**
     * Displays a Marker on the Map at the approriate location
     * @param mapboxMap
     */
    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        String location = getIntent().getStringExtra("location");
        String productTitle = getIntent().getStringExtra("title");

        double latitude=0, longitude=0;

        switch (location) {
            case "FS001":
                latitude = 54.345930;
                longitude = -7.638316;
                break;
            case "FS002":
                latitude = 54.345920;
                longitude = -7.638304;
                break;
            case "FS003":
                latitude = 54.345910;
                longitude = -7.638293;
                break;
            case "FS004":
                latitude = 54.345901;
                longitude = -7.638283;
                break;
            case "FS005":
                latitude = 54.345890;
                longitude = -7.638275;
                break;
            case "FS006":
                latitude = 54.345882;
                longitude = -7.638267;
                break;
            case "FS007":
                latitude = 54.345872;
                longitude = -7.638259;
                break;
            case "FS008":
                latitude = 54.345863;
                longitude = -7.638251;
                break;
            case "FS009":
                latitude = 54.345853;
                longitude = -7.638243;
                break;
            case "FS010":
                latitude = 54.345843;
                longitude = -7.638235;
                break;
            case "FS011":
                latitude = 54.345833;
                longitude = -7.638225;
                break;
            case "FS012":
                latitude = 54.345823;
                longitude = -7.638218;
                break;
            case "FS013":
                latitude = 54.345813;
                longitude = -7.638210;
                break;
            case "FS014":
                latitude = 54.345803;
                longitude = -7.638200;
                break;
            case "FS015":
                latitude = 54.345793;
                longitude = -7.638192;
                break;
            case "FS016":
                latitude = 54.345792;
                longitude = -7.638171;
                break;
            case "FS062":
                latitude = 54.345860;
                longitude = -7.638122;
                break;
            case "FS017":
                latitude = 54.345796;
                longitude = -7.638130;
                break;
            case "FS018":
                latitude = 54.345875;
                longitude = -7.638139;
                break;
            case "FS019":
                latitude = 54.345883;
                longitude = -7.638150;
                break;
            case "FS020":
                latitude = 54.345893;
                longitude = -7.638161;
                break;
            case "FS021":
                latitude = 54.345902;
                longitude = -7.638171;
                break;
            case "FS022":
                latitude = 54.345911;
                longitude = -7.638183;
                break;
            case "FS023":
                latitude = 54.345921;
                longitude = -7.638194;
                break;
            case "FS024":
                latitude = 54.345931;
                longitude = -7.638205;
                break;
            case "FS025":
                latitude = 54.345940;
                longitude = -7.638216;
                break;
            case "FS026":
                latitude = 54.345949;
                longitude = -7.638227;
                break;
            case "FS027":
                latitude = 54.345840;
                longitude = -7.638211;
                break;
            case "FS030":
                latitude = 54.345943;
                longitude = -7.638242;
                break;
            case "FS031":
                latitude = 54.345938;
                longitude = -7.638261;
                break;
            case "FS032":
                latitude = 54.345935;
                longitude = -7.638256;
                break;
            case "FS033":
                latitude = 54.345940;
                longitude = -7.638236;
                break;
            case "FS034":
                latitude = 54.345913;
                longitude = -7.638246;
                break;
            case "FS035":
                latitude = 54.345896;
                longitude = -7.638235;
                break;
            case "FS036":
                latitude = 54.345885;
                longitude = -7.638224;
                break;
            case "FS037":
                latitude = 54.345875;
                longitude = -7.638215;
                break;
            case "FS038":
                latitude = 54.345877;
                longitude = -7.638208;
                break;
            case "FS039":
                latitude = 54.345889;
                longitude = -7.638218;
                break;
            case "FS040":
                latitude = 54.345898;
                longitude = -7.638227;
                break;
            case "FS041":
                latitude = 54.345854;
                longitude = -7.638196;
                break;
            case "FS042":
                latitude = 54.345845;
                longitude = -7.638186;
                break;
            case "FS043":
                latitude = 54.345846;
                longitude = -7.638178;
                break;
            case "FS044":
                latitude = 54.345856;
                longitude = -7.638188;
                break;
            case "FS045":
                latitude = 54.345824;
                longitude = -7.638175;
                break;
            case "FS046":
                latitude = 54.345814;
                longitude = -7.638166;
                break;
            case "FS047":
                latitude = 54.345816;
                longitude = -7.638158;
                break;
            case "FS048":
                latitude = 54.345824;
                longitude = -7.638115;
                break;
            case "FS049":
                latitude = 54.345831;
                longitude = -7.638122;
                break;
            case "FS050":
                latitude = 54.345852;
                longitude = -7.638148;
                break;
            case "FS051":
                latitude = 54.345864;
                longitude = -7.638159;
                break;
            case "FS052":
                latitude = 54.345861;
                longitude = -7.638165;
                break;
            case "FS053":
                latitude = 54.345852;
                longitude = -7.638156;
                break;
            case "FS054":
                latitude = 54.345884;
                longitude = -7.638177;
                break;
            case "FS055":
                latitude = 54.345893;
                longitude = -7.638186;
                break;
            case "FS056":
                latitude = 54.345893;
                longitude = -7.638195;
                break;
            case "FS057":
                latitude = 54.345883;
                longitude = -7.638186;
                break;
            case "FS058":
                latitude = 54.345914;
                longitude = -7.638204;
                break;
            case "FS059":
                latitude = 54.345926;
                longitude = -7.638217;
                break;
            case "FS060":
                latitude = 54.345924;
                longitude = -7.638224;
                break;
            case "FS061":
                latitude = 54.345912;
                longitude = -7.638213;
                break;
            case "MS001":
                latitude = 54.345750;
                longitude = -7.638043;
                break;
            case "MS002":
                latitude = 54.345756;
                longitude = -7.637993;
                break;
            case "MS003":
                latitude = 54.345758;
                longitude = -7.637973;
                break;
            case "MS004":
                latitude = 54.345760;
                longitude = -7.637953;
                break;
            case "MS005":
                latitude = 54.345762;
                longitude = -7.637933;
                break;
            case "MS006":
                latitude = 54.345764;
                longitude = -7.637913;
                break;
            case "MS007":
                latitude = 54.345766;
                longitude = -7.637893;
                break;
            case "MS008":
                latitude = 54.345768;
                longitude = -7.637873;
                break;
            case "MS009":
                latitude = 54.345771;
                longitude = -7.637853;
                break;
            case "MS010":
                latitude = 54.345773;
                longitude = -7.637833;
                break;
            case "MS011":
                latitude = 54.345775;
                longitude = -7.637813;
                break;
            case "MS012":
                latitude = 54.345777;
                longitude = -7.637793;
                break;
            case "MS013":
                latitude = 54.345779;
                longitude = -7.637773;
                break;
            case "MS016":
                latitude = 54.345795;
                longitude = -7.637807;
                break;
            case "MS017":
                latitude = 54.345795;
                longitude = -7.637815;
                break;
            case "MS018":
                latitude = 54.345804;
                longitude = -7.637820;
                break;
            case "MS019":
                latitude = 54.345805;
                longitude = -7.637812;
                break;
            case "MS020":
                latitude = 54.345791;
                longitude = -7.637840;
                break;
            case "MS021":
                latitude = 54.345790;
                longitude = -7.637849;
                break;
            case "MS022":
                latitude = 54.345798;
                longitude = -7.637854;
                break;
            case "MS023":
                latitude = 54.345805;
                longitude = -7.637853;
                break;
            case "MS024":
                latitude = 54.345799;
                longitude = -7.637845;
                break;
            case "MS025":
                latitude = 54.345785;
                longitude = -7.637874;
                break;
            case "MS026":
                latitude = 54.345783;
                longitude = -7.637881;
                break;
            case "MS027":
                latitude = 54.345793;
                longitude = -7.637887;
                break;
            case "MS028":
                latitude = 54.345800;
                longitude = -7.637886;
                break;
            case "MS029":
                latitude = 54.345793;
                longitude = -7.637879;
                break;
            case "MS030":
                latitude = 54.345822;
                longitude = -7.637851;
                break;
            case "MS031":
                latitude = 54.345817;
                longitude = -7.637903;
                break;
            case "MS032":
                latitude = 54.345775;
                longitude = -7.637906;
                break;
            case "MS033":
                latitude = 54.345774;
                longitude = -7.637919;
                break;
            case "MS034":
                latitude = 54.345772;
                longitude = -7.637936;
                break;
            case "MS035":
                latitude = 54.345776;
                longitude = -7.637938;
                break;
            case "MS036":
                latitude = 54.345778;
                longitude = -7.637922;
                break;
            case "MS037":
                latitude = 54.345779;
                longitude = -7.637909;
                break;
            case "MS038":
                latitude = 54.345793;
                longitude = -7.637919;
                break;
            case "MS039":
                latitude = 54.345791;
                longitude = -7.637933;
                break;
            case "MS040":
                latitude = 54.345789;
                longitude = -7.637949;
                break;
            case "MS041":
                latitude = 54.345788;
                longitude = -7.637965;
                break;
            case "MS042":
                latitude = 54.345792;
                longitude = -7.637968;
                break;
            case "MS043":
                latitude = 54.345794;
                longitude = -7.637952;
                break;
            case "MS044":
                latitude = 54.345796;
                longitude = -7.637935;
                break;
            case "MS045":
                latitude = 54.345797;
                longitude = -7.637922;
                break;
            case "MS046":
                latitude = 54.345768;
                longitude = -7.637969;
                break;
            case "MS047":
                latitude = 54.345766;
                longitude = -7.637985;
                break;
            case "MS048":
                latitude = 54.345764;
                longitude = -7.637998;
                break;
            case "MS049":
                latitude = 54.345765;
                longitude = -7.638009;
                break;
            case "MS050":
                latitude = 54.345769;
                longitude = -7.638001;
                break;
            case "MS051":
                latitude = 54.345770;
                longitude = -7.637988;
                break;
            case "MS052":
                latitude = 54.345772;
                longitude = -7.637972;
                break;
            case "MS053":
                latitude = 54.345783;
                longitude = -7.638003;
                break;
            case "MS054":
                latitude = 54.345782;
                longitude = -7.638018;
                break;
            case "MS055":
                latitude = 54.345783;
                longitude = -7.638029;
                break;
            case "MS056":
                latitude = 54.345786;
                longitude = -7.638020;
                break;
            case "MS057":
                latitude = 54.345788;
                longitude = -7.638006;
                break;
            case "MS058":
                latitude = 54.345757;
                longitude = -7.638052;
                break;
            case "MS059":
                latitude = 54.345766;
                longitude = -7.638061;
                break;
            case "MS060":
                latitude = 54.345784;
                longitude = -7.638096;
                break;
            case "MS061":
                latitude = 54.345826;
                longitude = -7.637934;
                break;
            case "MS062":
                latitude = 54.345835;
                longitude = -7.637939;
                break;
            case "MS063":
                latitude = 54.345845;
                longitude = -7.637944;
                break;
            case "MS064":
                latitude = 54.345852;
                longitude = -7.637950;
                break;
            case "MS065":
                latitude = 54.345864;
                longitude = -7.637957;
                break;
            case "MS066":
                latitude = 54.345859;
                longitude = -7.638031;
                break;
            case "MS067":
                latitude = 54.345833;
                longitude = -7.638012;
                break;
            case "MS068":
                latitude = 54.345822;
                longitude = -7.638005;
                break;
            case "MS069":
                latitude = 54.345816;
                longitude = -7.638000;
                break;
            case "MS070":
                latitude = 54.345820;
                longitude = -7.637969;
                break;
            case "MS071":
                latitude = 54.345827;
                longitude = -7.637968;
                break;
            case "MS072":
                latitude = 54.345836;
                longitude = -7.637974;
                break;
            case "MS073":
                latitude = 54.345845;
                longitude = -7.637979;
                break;
            case "MS074":
                latitude = 54.345844;
                longitude = -7.637987;
                break;
            case "MS075":
                latitude = 54.345835;
                longitude = -7.637982;
                break;
            case "MS076":
                latitude = 54.345827;
                longitude = -7.637977;
                break;
            case "FY001":
                latitude = 54.346005;
                longitude = -7.638104;
                break;
            case "FY002":
                latitude = 54.346027;
                longitude = -7.638045;
                break;
            case "FY003":
                latitude = 54.346055;
                longitude = -7.637967;
                break;
            case "FY004":
                latitude = 54.346078;
                longitude = -7.637899;
                break;
            case "FY005":
                latitude = 54.346096;
                longitude = -7.637852;
                break;
            case "FY006":
                latitude = 54.346080;
                longitude = -7.637830;
                break;
            case "FY007":
                latitude = 54.346055;
                longitude = -7.637826;
                break;
            case "FY008":
                latitude = 54.346027;
                longitude = -7.637823;
                break;
            case "FY009":
                latitude = 54.345990;
                longitude = -7.637817;
                break;
            case "FY010":
                latitude = 54.345951;
                longitude = -7.637812;
                break;
            case "FY011":
                latitude = 54.345920;
                longitude = -7.637808;
                break;
            case "FY012":
                latitude = 54.345884;
                longitude = -7.637798;
                break;
            case "FY013":
                latitude = 54.345878;
                longitude = -7.637834;
                break;
            case "FY014":
                latitude = 54.345858;
                longitude = -7.637890;
                break;
            case "FY015":
                latitude = 54.345871;
                longitude = -7.638070;
                break;
            case "FY016":
                latitude = 54.345886;
                longitude = -7.638127;
                break;
            case "FY017":
                latitude = 54.345912;
                longitude = -7.638159;
                break;
            case "FY018":
                latitude = 54.345931;
                longitude = -7.638183;
                break;
            case "BY001":
                latitude = 54.345728;
                longitude = -7.638064;
                break;
            case "BY002":
                latitude = 54.345707;
                longitude = -7.638111;
                break;
            case "BY003":
                latitude = 54.345685;
                longitude = -7.638098;
                break;
            case "BY004":
                latitude = 54.345654;
                longitude = -7.638080;
                break;
            case "BY005":
                latitude = 54.345626;
                longitude = -7.638064;
                break;
            case "BY006":
                latitude = 54.345594;
                longitude = -7.637864;
                break;
            case "BY007":
                latitude = 54.345597;
                longitude = -7.637825;
                break;
            case "BY008":
                latitude = 54.345600;
                longitude = -7.637782;
                break;
            case "BY009":
                latitude = 54.345725;
                longitude = -7.637705;
                break;
            case "HB001":
                latitude = 54.346103;
                longitude = -7.637580;
                break;
            case "HB002":
                latitude = 54.346082;
                longitude = -7.637578;
                break;
            case "HB003":
                latitude = 54.346058;
                longitude = -7.637575;
                break;
            case "HB004":
                latitude = 54.346028;
                longitude = -7.637571;
                break;
            case "HB005":
                latitude = 54.345997;
                longitude = -7.637576;
                break;
            case "HB006":
                latitude = 54.345967;
                longitude = -7.637565;
                break;
            case "HB007":
                latitude = 54.345937;
                longitude = -7.637562;
                break;
            case "HB008":
                latitude = 54.345909;
                longitude = -7.637558;
                break;
            case "HB009":
                latitude = 54.345877;
                longitude = -7.637555;
                break;
            case "HB010":
                latitude = 54.345858;
                longitude = -7.637553;
                break;
            case "HB011":
                latitude = 54.345886;
                longitude = -7.637654;
                break;
            case "HB012":
                latitude = 54.345897;
                longitude = -7.637660;
                break;
            case "HB013":
                latitude = 54.345927;
                longitude = -7.637662;
                break;
            case "HB014":
                latitude = 54.345957;
                longitude = -7.637666;
                break;
            case "HB015":
                latitude = 54.345990;
                longitude = -7.637671;
                break;
            case "HB016":
                latitude = 54.346025;
                longitude = -7.637677;
                break;
            case "HB017":
                latitude = 54.346058;
                longitude = -7.637682;
                break;
            case "HB018":
                latitude = 54.346085;
                longitude = -7.637684;
                break;
            case "HB019":
                latitude = 54.346107;
                longitude = -7.637612;
                break;
            case "HB020":
                latitude = 54.346082;
                longitude = -7.637631;
                break;
            case "HB021":
                latitude = 54.346055;
                longitude = -7.637633;
                break;
            case "HB022":
                latitude = 54.346027;
                longitude = -7.637632;
                break;
            case "HB023":
                latitude = 54.346002;
                longitude = -7.637626;
                break;
            case "HB024":
                latitude = 54.345974;
                longitude = -7.637622;
                break;
            case "HB025":
                latitude = 54.345946;
                longitude = -7.637612;
                break;
            case "HB026":
                latitude = 54.345922;
                longitude = -7.637612;
                break;
            case "HB027":
                latitude = 54.345896;
                longitude = -7.637612;
                break;
            case "HB028":
                latitude = 54.345865;
                longitude = -7.637612;
                break;
            case "BS001":
                latitude = 54.345876;
                longitude = -7.637766;
                break;
            case "BS002":
                latitude = 54.345884;
                longitude = -7.637766;
                break;
            case "BS003":
                latitude = 54.345894;
                longitude = -7.637768;
                break;
            case "BS004":
                latitude = 54.345905;
                longitude = -7.637769;
                break;
            case "BS005":
                latitude = 54.345917;
                longitude = -7.637769;
                break;
            case "BS006":
                latitude = 54.345927;
                longitude = -7.637769;
                break;
            case "BS007":
                latitude = 54.345936;
                longitude = -7.637771;
                break;
            case "BS008":
                latitude = 54.345960;
                longitude = -7.637733;
                break;
            case "BS009":
                latitude = 54.345923;
                longitude = -7.637679;
                break;
            case "BS010":
                latitude = 54.345903;
                longitude = -7.637687;
                break;
            case "BS011":
                latitude = 54.345889;
                longitude = -7.637684;
                break;
            case "BS012":
                latitude = 54.345877;
                longitude = -7.637682;
                break;
            case "BS013":
                latitude = 54.345864;
                longitude = -7.637679;
                break;
            case "BS014":
                latitude = 54.345857;
                longitude = -7.637679;
                break;
            case "BS015":
                latitude = 54.345851;
                longitude = -7.637678;
                break;
            case "BS016":
                latitude = 54.345880;
                longitude = -7.637733;
                break;
            case "BS017":
                latitude = 54.345888;
                longitude = -7.637731;
                break;
            case "BS018":
                latitude = 54.345898;
                longitude = -7.637732;
                break;
            case "BS019":
                latitude = 54.345909;
                longitude = -7.637732;
                break;
            case "BS020":
                latitude = 54.345920;
                longitude = -7.637734;
                break;
            case "BS021":
                latitude = 54.345931;
                longitude = -7.637736;
                break;
            case "BS022":
                latitude = 54.345931;
                longitude = -7.637743;
                break;
            case "BS023":
                latitude = 54.345920;
                longitude = -7.637742;
                break;
            case "BS024":
                latitude = 54.345910;
                longitude = -7.637740;
                break;
            case "BS025":
                latitude = 54.345899;
                longitude = -7.637739;
                break;
            case "BS026":
                latitude = 54.345888;
                longitude = -7.637737;
                break;
            case "BS027":
                latitude = 54.345836;
                longitude = -7.637761;
                break;
            case "BS028":
                latitude = 54.345829;
                longitude = -7.637772;
                break;
            case "BS029":
                latitude = 54.345838;
                longitude = -7.637781;
                break;
            case "BS030":
                latitude = 54.345838;
                longitude = -7.637781;
                break;
            case "BS031":
                latitude = 54.345837;
                longitude = -7.637816;
                break;
            case "BS032":
                latitude = 54.345845;
                longitude = -7.637821;
                break;
            case "BS033":
                latitude = 54.345931;
                longitude = -7.637736;
                break;
            case "PL001":
                latitude = 54.345814;
                longitude = -7.637675;
                break;
            case "PL002":
                latitude = 54.345805;
                longitude = -7.637674;
                break;
            case "PL003":
                latitude = 54.345796;
                longitude = -7.637673;
                break;
            case "PL004":
                latitude = 54.345787;
                longitude = -7.637670;
                break;
            case "PL005":
                latitude = 54.345779;
                longitude = -7.637669;
                break;
            case "PL006":
                latitude = 54.345770;
                longitude = -7.637668;
                break;
            case "PL007":
                latitude = 54.345761;
                longitude = -7.637667;
                break;
            case "PL008":
                latitude = 54.345744;
                longitude = -7.637687;
                break;
            case "PL009":
                latitude = 54.345744;
                longitude = -7.637704;
                break;
            case "PL010":
                latitude = 54.345744;
                longitude = -7.637720;
                break;
            case "PL011":
                latitude = 54.345744;
                longitude = -7.637739;
                break;
            case "PL012":
                latitude = 54.345767;
                longitude = -7.637744;
                break;
            case "PL013":
                latitude = 54.345768;
                longitude = -7.637725;
                break;
            case "PL014":
                latitude = 54.345773;
                longitude = -7.637723;
                break;
            case "PL015":
                latitude = 54.345773;
                longitude = -7.637746;
                break;
            case "PL016":
                latitude = 54.345781;
                longitude = -7.637753;
                break;
            case "PL017":
                latitude = 54.345789;
                longitude = -7.637754;
                break;
            case "PL018":
                latitude = 54.345798;
                longitude = -7.637755;
                break;
            case "SR001":
                latitude = 54.345828;
                longitude = -7.637610;
                break;
            case "SR002":
                latitude = 54.345788;
                longitude = -7.637599;
                break;
            case "SR003":
                latitude = 54.345746;
                longitude = -7.637604;
                break;
            case "SR004":
                latitude = 54.345710;
                longitude = -7.637604;
                break;
            case "SR005":
                latitude = 54.345688;
                longitude = -7.637590;
                break;
            case "SR006":
                latitude = 54.345633;
                longitude = -7.637586;
                break;
            case "BEE001":
                latitude = 54.345686;
                longitude = -7.637666;
                break;
            case "RA001":
                latitude = 54.345637;
                longitude = -7.637653;
                break;
            case "RA002":
                latitude = 54.345630;
                longitude = -7.637653;
                break;
            case "RA003":
                latitude = 54.345624;
                longitude = -7.637652;
                break;
            case "RA004":
                latitude = 54.345618;
                longitude = -7.637652;
                break;
            case "RA005":
                latitude = 54.345609;
                longitude = -7.637656;
                break;
            case "RA006":
                latitude = 54.345609;
                longitude = -7.637680;
                break;
            case "RA007":
                latitude = 54.345608;
                longitude = -7.637697;
                break;
            case "RA008":
                latitude = 54.345603;
                longitude = -7.637722;
                break;
            case "RA009":
                latitude = 54.345611;
                longitude = -7.637726;
                break;
            case "RA010":
                latitude = 54.345616;
                longitude = -7.637727;
                break;
            case "RA011":
                latitude = 54.345623;
                longitude = -7.637728;
                break;
            case "RA012":
                latitude = 54.345629;
                longitude = -7.637728;
                break;
            case "RA013":
                latitude = 54.345627;
                longitude = -7.637691;
                break;
            case "RA014":
                latitude = 54.345654;
                longitude = -7.637670;
                break;
            default:
                    Toast.makeText(this,"Location not found",Toast.LENGTH_SHORT).show();
        }



        mapboxMap.addMarker(
                new MarkerOptions().position(new LatLng(latitude, longitude)).title(productTitle).snippet(location));


        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(userLocation.getMark()!=null) {

                    mapboxMap.removeMarker(userLocation.getMark());

                }

                double lat = 0, longitude = 0;

                if(x <= 2.55 && y > 3.935) {
                    lat = 54.345657;
                    longitude = -7.637719;
                } else if(x >= 2.56 && y > 3.935) {
                    lat = 54.345657;
                    longitude = -7.637663;
                } else if (x <= 2.55 && y <= 3.935 && x > 0 && y > 0) {
                    lat = 54.345614;
                    longitude = -7.637715;
                } else if (x >= 2.55 && y <= 3.935) {
                    lat = 54.345614;
                    longitude = -7.637675;
                } else {
                    Toast.makeText(ctx,"User location is unavailable", Toast.LENGTH_SHORT).show();
                }

                //mapboxMap.removeMarker(mark);


                // Create an Icon object for the marker to use
                Icon icon = IconFactory.getInstance(MapActivity.this).fromResource(R.drawable.ic_person_pin);

                userLocation.setMarker(new MarkerOptions().position(new LatLng(lat, longitude)).icon(icon));
                userLocation.setMark(mapboxMap.addMarker(userLocation.getMarker()));
                //mapboxMap.addMarker(userLocation.getMarker());
            }
        });
    }

    /**
     * Handles the MapView at the start of the activity's lifecycle
     */
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        if(indoorLocationManager!=null) {
            indoorLocationManager.startPositioning();
        }
    }

    /**
     * Handles the MapView upon the activity's resumption
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Handles the MapView when the Activity pauses
     */
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if(indoorLocationManager!=null) {
            indoorLocationManager.stopPositioning();
        }
    }

    /**
     * Handles the MapView on Low Memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Handles the MapView when the Activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Handles the MapView on save instance state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
