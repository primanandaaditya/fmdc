package id.co.cp.mdc.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.ModSpinner;
import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.Farmer;
import id.co.cp.mdc.helper.SQLiteHandler;


public class FarmerFragment extends Fragment implements OnMapReadyCallback,OnClickListener, OnItemSelectedListener,GoogleApiClient.ConnectionCallbacks,LocationListener,
        GoogleApiClient.OnConnectionFailedListener{
    private GoogleMap mMap;
    private ProgressDialog pDialog;
    private Spinner spinner;
    private Spinner spinnerAddress;
    private String name;
    private String email;
    private String token;


    public static final String KEY_LATIT = "latit";
    public static final String KEY_LONGI = "longi";
    public static final String KEY_ZTSID = "ztsid";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;


    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;


    // LogCat tag
    private static final String TAG = FarmerFragment.class.getSimpleName();


    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 1; // 10 meters

    // UI elements
    private TextView lblLong;
    private TextView lblLat;
    private Button btnShowLocation;
    private Button btnRegis;
    private SQLiteHandler db;
    private String selFmId;
    private AlertDialog.Builder adb_gtag;
    private ArrayList<ModSpinner> farmSpin;
    double longitude, latitude;
    int statusMock;

    public FarmerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_farmer, container, false);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        getFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //connect spinner dan add listener
        spinner = (Spinner) rootView.findViewById(R.id.spinnerFarmer);
        spinnerAddress = (Spinner) rootView.findViewById(R.id.spinnerAddress);
        spinner.setOnItemSelectedListener(this);
        spinnerAddress.setOnItemSelectedListener(this);
        adb_gtag = new AlertDialog.Builder(getActivity());
        adb_gtag.setTitle("Konfirmasi Register Farm");
        adb_gtag.setIcon(android.R.drawable.ic_dialog_alert);
        adb_gtag.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(lblLat.getText()==""||lblLong.getText()==""){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Mohon aktifkan GPS pada device dan tunggu selama 5 detik. Jalankan aplikasi Google Maps jika pesan ini tetap berlanjut.", Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    hidepDialog();
                    return;
                }
                else {
                    if(statusMock==1){
                        do_register_farm();
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),"Harap matikan mock location", Toast.LENGTH_LONG).show();
                    }
                }
            } });
        adb_gtag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });


        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        email = user.get("email");
        token = user.get("pass");
        FillSpinner();

        lblLong = (TextView) rootView.findViewById(R.id.lblLong);
        lblLat = (TextView) rootView.findViewById(R.id.lblLat);
        btnShowLocation = (Button) rootView.findViewById(R.id.btnShowLocation);
        btnRegis = (Button) rootView.findViewById(R.id.btnRegis);

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        mGoogleApiClient.connect();

        // Show location button click listener
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                displayLocation();
            }
        });

        // Toggling the periodic location updates
        btnRegis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adb_gtag.setMessage("Farm\t\t: "+spinner.getSelectedItem().toString().substring(9,spinner.getSelectedItem().toString().length())+" ?");
                adb_gtag.show();
//                do_register_farm();
            }
        });

        return rootView;
    }


    private void FillSpinner(){
        List<String> categoriesFarm = new ArrayList<String>();
        List<String> categoriesAddr = new ArrayList<String>();

        List<Farmer> Farmers = db.getAllFarmer();
        for (Farmer fm : Farmers) {
            String longi_farm = fm.get_longi();
            String flag;
            if(!longi_farm.equals("empty")){
                flag = "[X]";
            }
            else{
                flag = "";
            }
            String farmer = fm.getFarmCode()+" "+flag+" "+fm.getFarmName();
            String address = flag+" "+fm.getFarmAddress();
            categoriesFarm.add(farmer);
            categoriesAddr.add(address);
        }
//        for(int i=0;i<j.length();i++){
//            try {
//                JSONObject json = j.getJSONObject(i);
//                String farmer = json.getString(AppConfig.TAG_AREA_ID)+json.getString(AppConfig.TAG_BRANCH_ID)+json.getString(AppConfig.TAG_FARMER_ID)+" - "+json.getString(AppConfig.TAG_FARMER);
//                String address = json.getString(AppConfig.TAG_AREA_ID)+json.getString(AppConfig.TAG_BRANCH_ID)+json.getString(AppConfig.TAG_FARMER_ID)+" - "+json.getString(AppConfig.TAG_ADDRESS);
//                categoriesFarm.add(farmer);
//                categoriesAddr.add(address);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        ArrayAdapter<String> dataAdapterFarm = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoriesFarm);
        ArrayAdapter<String> dataAdapterAddr = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoriesAddr);
        dataAdapterFarm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterAddr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterFarm);
        spinnerAddress.setAdapter(dataAdapterAddr);
        spinnerAddress.setSelection(AppConfig.FmSpinNum);
        spinner.setSelection(AppConfig.FmSpinNum);

    }
    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            if(mLastLocation.isFromMockProvider()) {
                statusMock = 0;
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                lblLat.setText(String.valueOf(latitude));
                lblLong.setText(String.valueOf(longitude));
            } else {
                statusMock = 1;
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                lblLat.setText(String.valueOf(latitude));
                lblLong.setText(String.valueOf(longitude));
                LatLng mytag = new LatLng(latitude, longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(mytag).title("I'm Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mytag, 15.0f));
            }

        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Mohon aktifkan GPS pada device dan tunggu selama 5 detik. Jalankan aplikasi Google Maps jika pesan ini tetap berlanjut.", Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, (LocationListener) getActivity());
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (LocationListener) this);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    private void do_register_farm(){
        showpDialog();
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,AppConfig.urlPimsFarm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response==""){
                        }
                        else {
                            Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            hidepDialog();
                            if(response.substring(0,7).matches("Session")){
                                db.deleteTabel();

                                // Launching the login activity
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        offline_register_farm();
//                        Toast.makeText(getActivity().getApplicationContext(),"Lokasi berhasil didaftarkan secara offline",Toast.LENGTH_LONG).show();
                        hidepDialog();
//                        Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
//                        hidepDialog();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_LATIT,latit);
                params.put(KEY_LONGI,longi);
                params.put(KEY_ZTSID, zfmid);
                params.put("zuser", email);
                params.put("mobile", "android");
                params.put("type", "regis_farm");
                params.put("clien", AppConfig.clien);
                params.put("token", token);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void offline_register_farm(){
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);
        db.addGmap(zfmid, longi, latit);
        Toast.makeText(getActivity(),"Lokasi berhasil didaftarkan secara offline",Toast.LENGTH_LONG).show();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.btnRegis:
//                do_register_farm();
//                break;
//            case R.id.btnShowLocation:
//                displayLocation();
//                break;
//            default:
//                break;
//        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerAddress.setSelection(position);
        spinner.setSelection(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

//        Toast.makeText(getActivity().getApplicationContext(), "Location changed!",
//                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();

    }
}