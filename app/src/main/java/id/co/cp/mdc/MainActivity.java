package id.co.cp.mdc;

import android.Manifest;
import android.app.AlertDialog;
import android.renderscript.ScriptGroup;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.cp.mdc.adapter.CobaAdapter;
import id.co.cp.mdc.adapter.UserListAdapter;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.controller.ApproveCountController;
import id.co.cp.mdc.controller.PermissionController;
import id.co.cp.mdc.controller.UserListController;
import id.co.cp.mdc.fragments.FarmerFragment;
import id.co.cp.mdc.fragments.GeoTagFragment;
import id.co.cp.mdc.fragments.HomeFragment;
import id.co.cp.mdc.fragments.ListFarmFragment;
import id.co.cp.mdc.fragments.UserListFragment;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.helper.gmap;
import id.co.cp.mdc.interfaces.IApproveCount;
import id.co.cp.mdc.interfaces.IBaseFragment;
import id.co.cp.mdc.interfaces.IUserList;
import id.co.cp.mdc.model.UserListModel;
import id.co.cp.mdc.model.approvecount.ApproveCountModel;
import id.co.cp.mdc.other.BaseActivity;
import id.co.cp.mdc.other.CircleTransform;


public class MainActivity extends BaseActivity implements IBaseFragment, IApproveCount {


    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private String email;
    private String token;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    private SQLiteHandler db;
    private AlertDialog.Builder adb;

    PermissionController permissionController;
    ApproveCountController approveCountController;

    //Prima for Location

    //by Prima
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";

    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();

    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        //updateGPSStatus("GPS is Enabled in your device");
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        //updateGPSStatus("GPS is Disabled in your device");
                        break;
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //If permission granted show location dialog if APIClient is not null
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                        showSettingDialog();
                    } else
                        showSettingDialog();


                } else {
                    //updateGPSStatus("Location Permission denied.");
                    Toast.makeText(MainActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }


    }

    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                    //updateGPSStatus("GPS is Enabled in your device");
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();
                    //updateGPSStatus("GPS is Disabled in your device");
                    Log.e("About GPS", "GPS is Disabled in your device");
                }

            }
        }
    };












    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionController=new PermissionController(this);
        permissionController.doPermissions();


        //Prima location
        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        adb = new AlertDialog.Builder(this);
        getData();

    }

    private void loadNavHeader() {
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        // name, website
        txtName.setText(user.get("name"));
        txtWebsite.setText(user.get("email"));
        email = user.get("email");
        token = user.get("pass");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);
        getImageProfile();

        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);

        // showing dot next to notifications label
//        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);
    }
    public void getImageProfile(){
//        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.urlMyImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loadImg(AppConfig.server_ip+response);
                        //   Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();
//                        hidepDialog();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"tidak terhubung dengan server...",Toast.LENGTH_LONG).show();
//                        hidepDialog();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();


                params.put("clien", AppConfig.clien);
                params.put("zuser", email);
                params.put("ztype", "download");
                params.put("token", token);


                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void loadImg(String filePath) {

        // loading header background image
        // Glide.with(this).load(urlNavHeaderBg)
        //       .crossFade()
        //    .diskCacheStrategy(DiskCacheStrategy.ALL)
        //    .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(filePath)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache( true )
                .into(imgProfile);

        // showing dot next to notifications label
//        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
//            case 1:
//                // photos
//                FarmerFragment farmerFragment = new FarmerFragment();
//                return farmerFragment;
//            case 1:
//                CheckInFragment checkInFragment = new CheckInFragment();
//                return checkInFragment;
            case 2:
                // movies fragment
                FarmerFragment farmerFragment = new FarmerFragment();
                return farmerFragment;
            case 3:
                // My Tag fragment
                //MyTagFragment myTagFragment = new MyTagFragment();
                //return myTagFragment;
                GeoTagFragment geoTagFragment = new GeoTagFragment();
                return geoTagFragment;
//
//            case 4:
//                // settings fragment
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;

            case 4:
                ListFarmFragment listFarmFragment=new ListFarmFragment();
                return listFarmFragment;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;



                    case R.id.nav_photos:
                        if (!checkPermission()) {

                            requestPermission();

                        }
                        else {
//                             launch new intent instead of loading fragment
                            Intent intent = new Intent(MainActivity.this,
                                    LhkActivity.class);
                            startActivity(intent);
                            drawer.closeDrawers();
                            return true;
                        }
                    case R.id.nav_notif:
                        if (!checkPermission()) {

                            requestPermission();

                        }
                        else {
                            // launch new intent instead of loading fragment
                            Intent intent = new Intent(MainActivity.this,
                                    NotificationActivity.class);
                            startActivity(intent);
                            drawer.closeDrawers();
                            return true;
                        }
                        break;
                    case R.id.nav_farmer:
                        navItemIndex = 2;
                        CURRENT_TAG = "Register Farm";
                        break;
                    case R.id.nav_messinput:
                        Intent intentinput = new Intent(MainActivity.this, MassInputActivity.class);
                        startActivity(intentinput);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_mytag:
                        navItemIndex = 3;
                        CURRENT_TAG = "My Geo Tag";
                        break;
                    case R.id.nav_sync:
                        syncLocationFarm();
//                        syncImage();
                        break;
                    case R.id.nav_monitoring:
                        if (!checkPermission()) {

                            requestPermission();

                        }
                        else {
                            // launch new intent instead of loading fragment
                            Intent intent = new Intent(MainActivity.this,
                                    MonitoringActivity.class);
                            startActivity(intent);
                            drawer.closeDrawers();
                            return true;
                        }
                        break;
                    case R.id.nav_about:
                            Uri uri = Uri.parse("https://cpis.cp.co.id/privacy/"); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            drawer.closeDrawers();
                            return true;

                    case R.id.nav_approve:
                       UserListFragment userListFragment=new UserListFragment();
                       loadFragment(userListFragment);
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void syncLocationFarm() {
        //get user data
        HashMap<String, String> user = db.getUserDetails();
        final String email = user.get("email");
        final String token = user.get("pass");
        //get geo tag data
        final JSONObject JObj = new JSONObject();
        final JSONArray JArr = new JSONArray();
        List<gmap> ListGmap =  db.getAllGmap();
        for (gmap g : ListGmap) {
            JSONObject JList = new JSONObject();
            try {
                JList.put("mapcd",g.getMapCode());
                JList.put("longi",g.getLongi());
                JList.put("latit",g.getLatit());
                JList.put("zuser",email);
                JList.put("clien",AppConfig.clien);
                JList.put("token",token);
                JArr.put(JList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //get lhk data
        final JSONArray JsonLhk = db.getAllLhk();

        try {
            JObj.put("LcFarm",JArr);
            JObj.put("LhFarm",JsonLhk);
            JObj.put("device","android");
            JObj.put("clien",AppConfig.clien);
            JObj.put("token",token);
            JObj.put("zuser",email);
//            Log.e(TAG, "Json_LHK_Result: " + JObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(JArr.length()==0&&JsonLhk.length()==0){
            Toast.makeText(getApplicationContext(), "tidak ada data untuk di sinkronisasi", Toast.LENGTH_LONG).show();
            return;
        }


        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppConfig.urlSync, JObj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("stat");
                            String message = response.getString("message");
//                            Toast.makeText(getApplicationContext(), JObj.toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            if(stat=="3"){
                                logoutUser();
                            }
                            else{
                                //delet tabel sqlite
                                db.DropGmap();
                                db.DropLhkh();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(), "Tidak terhubung dengan server. Harap mencari signal", Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }

    private void syncImage() {
        //get user data
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.urlPimsFarm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        db.DropImage();
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Tidak terhubung dengan server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                HashMap<String, String> Image = db.getImage();
                params.put("latit",Image.get("longi")); //sengaja dibalik karena gatau salahnya dimana
                params.put("longi",Image.get("latit")); //sengaja dibalik karena gatau salahnya dimana
                params.put("ztsid", Image.get("ztsid"));
                params.put("zfmid", Image.get("zfmid"));
                params.put("mobile", Image.get("mobile"));
                params.put("type", Image.get("type"));
                params.put("zuser", Image.get("zuser"));
                params.put("clien", Image.get("clien"));
                params.put("image", Image.get("image"));
                params.put("descr", Image.get("descr"));
                params.put("token", Image.get("token"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        adb.show();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }/* Prevent app from being killed on back */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // Back?
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Back
            moveTaskToBack(true);
            return true;
        }
        else {
            // Return
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            adb.setTitle("Warning!!!");
            adb.setMessage("Apakah anda ingin melakukan Log Out? (Anda tidak bisa login kembali jika tidak ada signal)");
            adb.setIcon(android.R.drawable.ic_dialog_alert);


            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    logoutUser();
                } });


            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                } });
            adb.show();
//            syncLocationFarm();
        }
        if (id == R.id.action_update_farm) {
            GetFarm();
        }
        if (id == R.id.action_clear_data) {
            adb.setTitle("Warning!!!");
            adb.setMessage("Apakah anda ingin menghapus data offline? (semua data offline akan hilang)");
            adb.setIcon(android.R.drawable.ic_dialog_alert);


            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final JSONArray JArr = new JSONArray();
                    List<gmap> ListGmap =  db.getAllGmap();
                    for (gmap g : ListGmap) {
                        JSONObject JList = new JSONObject();
                        try {
                            JList.put("mapcd",g.getMapCode());
                            JList.put("longi",g.getLongi());
                            JList.put("latit",g.getLatit());
                            JList.put("zuser",email);
                            JList.put("clien",AppConfig.clien);
                            JList.put("token",token);
                            JArr.put(JList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    //get lhk data
                    final JSONArray JsonLhk = db.getAllLhk();
                    if (JArr.length()>0){
                        db.DropGmap();
                    }
                    if(JsonLhk.length()>0) {
                        db.DropLhkh();
                    }
                    Toast.makeText(getApplicationContext(), "Berhasil menghapus data offline", Toast.LENGTH_LONG).show();
                } });


            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                } });
            adb.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
//            fab.show();
            fab.hide();
        else
            fab.hide();
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }
    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(getApplicationContext(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }
    private void logoutUser() {
        showpDialog();
        HashMap<String, String> user = db.getUserDetails();
        final String name = user.get("name");
        final String email = user.get("email");
        final String token = user.get("pass");
        final String ztsid = user.get("ztsid");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.urlPimsFarm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response==""){
                        }
                        else {
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                            hidepDialog();
                            if(response.substring(0,7).matches("Session")){
                                db.deleteTabel();

                                // Launching the login activity
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
//                                session.setLogin(false);
                                db.deleteTabel();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Mohon mencari signal untuk melakukan logout.",Toast.LENGTH_LONG).show();
                        hidepDialog();
//                        Toast.makeText(FarmerActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                        hidepDialog();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ztsid", ztsid);
                params.put("zuser", email);
                params.put("mobile", "android");
                params.put("type", "logout");
                params.put("clien", AppConfig.clien);
                params.put("token", token);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void GetFarm(){
        showpDialog();
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        String ztsid = user.get("ztsid");
        String email = user.get("email");
        String token = user.get("pass");
        String areid = ztsid.substring(0, 2);
        String brcid = ztsid.substring(2, 5);
        String tsid = ztsid.substring(5, 8);
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("token",token);
            JObj.put("clien",AppConfig.clien);
            JObj.put("area_id",areid);
            JObj.put("branch_id",brcid);
            JObj.put("ts_id",tsid);
            JObj.put("zuser",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlGetData, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("stat");
                            if(stat.equals("0")){
                                JSONArray res_arr=response.getJSONArray("data");
                                JSONArray arr_MsArea = response.getJSONArray("area");
                                JSONArray arr_MsBranch = response.getJSONArray("branch");
                                JSONArray arr_MsTs = response.getJSONArray("ts");
                                JSONArray arr_MsFarm = response.getJSONArray("farmer");
//                                Log.e("ms_ts",arr_MsTs.toString());
                                db.collTabel();
                                InsertFarm(res_arr);
                                db.addJsonMsArea(arr_MsArea);
                                db.addJsonMsBranch(arr_MsBranch);
                                db.addJsonMsFarm(arr_MsFarm);
                                db.addJsonMsTs(arr_MsTs);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"tidak terhubung dengan server PIMS", Toast.LENGTH_SHORT).show();
//                                db.deleteTabel();
                                hidepDialog();
                            }
                        }
                        catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"tidak terhubung dengan server PIMS", Toast.LENGTH_SHORT).show();
//                            db.deleteTabel();
                            hidepDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"tidak terhubung dengan server PIMS", Toast.LENGTH_SHORT).show();
//                        Log.e("error_get_data",error.toString());
//                        db.deleteTabel();
                        hidepDialog();
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }
    private void InsertFarm(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                String famnm = json.getString(AppConfig.TAG_FARMER);
                String famad = json.getString(AppConfig.TAG_ADDRESS);
                String famid = json.getString(AppConfig.TAG_AREA_ID)+json.getString(AppConfig.TAG_BRANCH_ID)+json.getString(AppConfig.TAG_FARMER_ID);
                String longi = json.getString("longi");
                String latit = json.getString("latit");
                db.addFarm(famid,famnm, famad, longi, latit);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getApplicationContext(),"Update farm success", Toast.LENGTH_SHORT).show();

        hidepDialog();
//        hideDialog();

    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void findID() {

    }

    @Override
    public void getData() {
        approveCountController=new ApproveCountController(MainActivity.this, this);
        approveCountController.getData();
    }



    void hideMenuApproval(){
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_approve).setVisible(false);
    }


    @Override
    public void onApproveCountError(String error) {
        Toast.makeText(MainActivity.this, error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApproveCountSuccess(ApproveCountModel approveCountModel) {

        int status = approveCountModel.getSysrc().getStatus();
        String strStatus = approveCountModel.getSysrc().getMessage();
        switch (status){
            case 0:
//                int jmlFarmer = approveCountModel.getZlist().size();
//                if (jmlFarmer<=1) {
//                    hideMenuApproval();
//                }
                break;

            case 1:
                this.sessionTimeOut();
                break;

            case 4:
//                hideMenuApproval();
                break;


        }


    }
}