package id.co.cp.mdc.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.Farmer;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.helper.zGeneralFunction;


public class OneFragment extends Fragment implements OnClickListener, OnItemSelectedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private Button btnSaveLhk;
    private Button zbtnDate;
    private Button btnAddFeed1;
    private Button btnAddFeed2;
    private EditText zTxtMortality;
    private EditText zTxtWeight;
    private EditText txtdo;
    private TextView zTxtDate;
    private static String TAG = OneFragment.class.getSimpleName();
    private Spinner spinner;
    private Spinner spinnerAddress;
    private Spinner spinnerFeed1;
    private Spinner spinnerFeed2;
    private SQLiteHandler db;
    private TextView lblLat;
    private TextView lblLong;
    private ProgressDialog pDialog;
    private Date cDate = new Date();
    private LinearLayout FeedForm1;
    private LinearLayout FeedForm2;
    private View EmptyView;
    private JSONArray fedar;
    private JSONArray fedar2;
    private String fkgps;
    private int countChild;
    private int countChild2;
    private int counter=0;

    private AlertDialog.Builder adb_lpab;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        btnSaveLhk = (Button) rootView.findViewById(R.id.btnSaveLhk);
        zbtnDate = (Button) rootView.findViewById(R.id.zbtnDate);
        zTxtMortality = (EditText) rootView.findViewById(R.id.ztxtMortality);
        zTxtWeight = (EditText) rootView.findViewById(R.id.ztxtWeight);
        txtdo = (EditText) rootView.findViewById(R.id.txtdo);
//        zTxtFeed = (EditText) rootView.findViewById(R.id.ztxtFeed);
        lblLat = (TextView) rootView.findViewById(R.id.lblLat);
        lblLong = (TextView) rootView.findViewById(R.id.lblLong);
        spinner = (Spinner) rootView.findViewById(R.id.spinnerFarmer);
        spinnerAddress = (Spinner) rootView.findViewById(R.id.spinnerAddress);
        spinnerFeed1 = (Spinner) rootView.findViewById(R.id.spinnerFeed1);
        spinnerFeed2 = (Spinner) rootView.findViewById(R.id.spinnerFeed2);
        btnAddFeed1 = (Button) rootView.findViewById(R.id.btnAddFeed1);
        btnAddFeed2 = (Button) rootView.findViewById(R.id.btnAddFeed2);
        FeedForm1 = (LinearLayout) rootView.findViewById(R.id.FeedForm1);
        FeedForm2 = (LinearLayout) rootView.findViewById(R.id.FeedForm2);

        spinner.setOnItemSelectedListener(this);
        spinnerAddress.setOnItemSelectedListener(this);
        btnSaveLhk.setOnClickListener(this);
        btnAddFeed1.setOnClickListener(this);
        btnAddFeed2.setOnClickListener(this);
        zbtnDate.setOnClickListener(this);


        adb_lpab = new AlertDialog.Builder(getActivity());
        adb_lpab.setTitle("Konfirmasi LPAB");
        adb_lpab.setIcon(android.R.drawable.ic_dialog_alert);
        adb_lpab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(fkgps.equals("y")){
                    Toast.makeText(getActivity(), "Harap matikan mock location", Toast.LENGTH_SHORT).show();
                }else{
                    do_register_LHK();
                }
            } });


        adb_lpab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });
        zTxtDate = (TextView) rootView.findViewById(R.id.zTxtDate);
        String in = getActivity().getIntent().getStringExtra("tgl");
        if(in==null){
            zTxtDate.setText(DateNow);
        }else{
            zTxtDate.setText(in);
        }
        //set spinner feed

        List<String> spinnerFeedlist = new ArrayList<String>();
        spinnerFeedlist.add("s00");
        spinnerFeedlist.add("s10");
        spinnerFeedlist.add("s11");
        spinnerFeedlist.add("s12");
        ArrayAdapter<String> dataAdapterFeed1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,spinnerFeedlist);
        dataAdapterFeed1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFeed1.setAdapter(dataAdapterFeed1);
        spinnerFeed2.setAdapter(dataAdapterFeed1);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        mGoogleApiClient.connect();
        FillSpinner();

        return rootView;
    }

    public void onDeleteClicked(View v) {
        // remove the row by calling the getParent on button
        FeedForm1.removeView((View) v.getParent());
        FeedForm2.removeView((View) v.getParent());
    }

    private void AddForm(String name) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View form_list = inflater.inflate(R.layout.form_list, null);
        final TextView FeedCd = (TextView) form_list.findViewById(R.id.FeedCd);
        final Button detach = (Button) form_list.findViewById(R.id.btnDetach);
        detach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClicked(view);
            }
        });

        if (name != null && !name.isEmpty()) {
            FeedCd.setText(name);
        } else {
            EmptyView = form_list;
        }

        // A TextWatcher to control the visibility of the "Add new" button and
        // handle the exclusive empty view.

        // Inflate at the end of all rows but before the "Add new" button
        FeedForm1.addView(form_list);
    }
    private void AddForm2(String name) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View form_list = inflater.inflate(R.layout.form_list, null);
        final TextView FeedCd = (TextView) form_list.findViewById(R.id.FeedCd);
        final Button detach = (Button) form_list.findViewById(R.id.btnDetach);
        detach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClicked(view);
            }
        });

        if (name != null && !name.isEmpty()) {
            FeedCd.setText(name);
        } else {
            EmptyView = form_list;
        }

        // A TextWatcher to control the visibility of the "Add new" button and
        // handle the exclusive empty view.

        // Inflate at the end of all rows but before the "Add new" button
        FeedForm2.addView(form_list);
    }

    private void FillSpinner(){
        List<String> categoriesFarm = new ArrayList<String>();
        List<String> categoriesAddr = new ArrayList<String>();

        List<Farmer> Farmers = db.getAllFarmer();
//        categoriesFarm.add("00000000 -  Pilih");
//        categoriesAddr.add("00000000 -  Pilih");
        for (Farmer fm : Farmers) {
            String farmer = fm.getFarmCode()+" - "+fm.getFarmName();
            String address = fm.getFarmCode()+" - "+fm.getFarmAddress();
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

        String in = getActivity().getIntent().getStringExtra("nama");
        if(in!=null){
            for(int i=0;i<categoriesFarm.size();i++){
                if(categoriesFarm.get(i).equals(in)){
                    counter++;
                    spinner.setSelection(i);
                    spinnerAddress.setSelection(i);
                }
            }
        }else{
            counter++;
        }

        if(counter==0){
            getActivity().finish();
            Toast.makeText(getActivity(), "Tidak ada autorisasi atau harus melakukan update farm", Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(getActivity(), String.valueOf(counter), Toast.LENGTH_SHORT).show();
    }
    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                if(mLastLocation.isFromMockProvider()) {
                    fkgps = "y";
                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();
                    lblLat.setText(String.valueOf(latitude));
                    lblLong.setText(String.valueOf(longitude));
                } else {
                    fkgps = "n";
                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();
                    lblLat.setText(String.valueOf(latitude));
                    lblLong.setText(String.valueOf(longitude));
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
    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) getActivity());

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
    private void do_register_LHK(){
        showpDialog();
        displayLocation();
        HashMap<String, String> user = db.getUserDetails();
        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final String name = user.get("name");
        final String ztsid = user.get("ztsid");
        final String email = user.get("email");
        final String token = user.get("pass");
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);
        final String morta = zTxtMortality.getText().toString().trim().replaceAll(",", "");
        final String avgbd = zTxtWeight.getText().toString().trim().replaceAll(",", "");
        final String donum = txtdo.getText().toString().trim().replaceAll(",", "");
//        final String feedc = zTxtFeed.getText().toString().trim().replaceAll(",", "");
        final String recdt = zTxtDate.getText().toString().trim();
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(recdt);
            date2 = sdf.parse(DateNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = (date2.getTime()-date1.getTime())/(24*60*60*1000);
        JSONObject send = new JSONObject();

        if(lblLat.getText()==""||lblLong.getText()==""){
            Toast.makeText(getActivity().getApplicationContext(),
                    "Mohon aktifkan GPS pada device dan tunggu selama 5 detik. Jalankan aplikasi Google Maps jika pesan ini tetap berlanjut.", Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            hidepDialog();
            return;
        }
        else if(morta.contains("-")||avgbd.contains("-")){
            Toast.makeText(getActivity(), "Data tidak boleh negatif..", Toast.LENGTH_LONG).show();
            hidepDialog();
            return;
        }
        else if(date1.after(date2)){
            Toast.makeText(getActivity(), "Tanggal tidak valid", Toast.LENGTH_LONG).show();
            hidepDialog();
            return;
        }
        else if(diff>(AppConfig.interval_time)){//3 hari
            Toast.makeText(getActivity(), "Tanggal tidak boleh lebih dari "+AppConfig.interval_time+" hari", Toast.LENGTH_LONG).show();
            hidepDialog();
            return;
        }
        else if(!(avgbd.matches(""))){
            if(Float.parseFloat(avgbd)>4000) {
                Toast.makeText(getActivity(), "Berat ayam tidak boleh lebih dari 4 Kg", Toast.LENGTH_LONG).show();
                hidepDialog();
                return;
            }
        }
        else if(morta.equals("")&&avgbd.equals("")&&donum.equals("")&&countChild==0&&countChild2==0){
            Toast.makeText(getActivity(), "Data tidak boleh kosong", Toast.LENGTH_LONG).show();
            hidepDialog();
            return;
        }
        else if(donum.equals("")&&countChild2>0){
            Toast.makeText(getActivity(), "Harap isi nomor DO", Toast.LENGTH_LONG).show();
            hidepDialog();
            return;
        }
        else if(donum.equals("")&&countChild2==0){
            try {
                send.put("ztsid",ztsid);
                send.put("zuser",email);
                send.put("token",token);
                send.put("longi",longi);
                send.put("latit",latit);
                send.put("zfmid",zfmid);
                send.put("morta",morta);
                send.put("avgbd",avgbd);
                send.put("donum",donum);
                send.put("recdt",recdt);
                send.put("clien",AppConfig.clien);
                send.put("fedar",fedar);
                send.put("fedar2",fedar2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        Log.e("isi_send",send.toString());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlLhkFarm, send,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String stat = response.getString("stat");
                                String message = response.getString("message");
                                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                                if(stat.equals("0")){
                                    FeedForm1.removeAllViews();
                                    FeedForm2.removeAllViews();
                                    zTxtMortality.setText("");
                                    zTxtWeight.setText("");
                                    txtdo.setText("");
                                }
                                else if(stat.equals("1")){
                                    db.deleteTabel();
                                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                                hidepDialog();
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                hidepDialog();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(fedar.length()>0) {
                                offline_insert_lpab();
//                            Toast.makeText(getActivity(), "Transaksi Feed belum bisa mode offline. Harap mencari signal yang lebih baik", Toast.LENGTH_LONG).show();
                            }
                            else{
                                offline_insert_lpab();
//                            Toast.makeText(getActivity(), "Data LPAB tersimpan offline. Jangan lupa lakukan sinkronisasi ketika ada signal", Toast.LENGTH_LONG).show();
                            }
                            hidepDialog();
                        }
                    });
            // BEGIN edit backoffMultiplier retry policy dan initial timeout 20190320
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000,0, 0F));
            // END edit backoffMultiplier retry policy dan initial timeout 20190320
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsObjRequest);
            return;
        }
        try {
            send.put("ztsid",ztsid);
            send.put("zuser",email);
            send.put("token",token);
            send.put("longi",longi);
            send.put("latit",latit);
            send.put("zfmid",zfmid);
            send.put("morta",morta);
            send.put("avgbd",avgbd);
            send.put("donum",donum);
            send.put("recdt",recdt);
            send.put("clien",AppConfig.clien);
            send.put("fedar",fedar);
            send.put("fedar2",fedar2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e("isi_send",send.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlLhkFarm, send,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("stat");
                            String message = response.getString("message");
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                            if(stat.equals("0")){
                                FeedForm1.removeAllViews();
                                FeedForm2.removeAllViews();
                                zTxtMortality.setText("");
                                zTxtWeight.setText("");
                                txtdo.setText("");
                            }
                            else if(stat.equals("1")){
                                db.deleteTabel();
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            hidepDialog();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            hidepDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(fedar.length()>0) {
                            offline_insert_lpab();
//                            Toast.makeText(getActivity(), "Transaksi Feed belum bisa mode offline. Harap mencari signal yang lebih baik", Toast.LENGTH_LONG).show();
                        }
                        else{
                            offline_insert_lpab();
//                            Toast.makeText(getActivity(), "Data LPAB tersimpan offline. Jangan lupa lakukan sinkronisasi ketika ada signal", Toast.LENGTH_LONG).show();
                        }
                        hidepDialog();
                    }
                });
        // BEGIN edit backoffMultiplier retry policy dan initial timeout 20190320
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000,0, 0F));
        // END edit backoffMultiplier retry policy dan initial timeout 20190320
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsObjRequest);
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
        DatePickerFragment datepicker = new DatePickerFragment();
        datepicker.setTxtDate("zTxtDate");
        DialogFragment newFragment = datepicker;
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    private void offline_insert_lpab(){
        HashMap<String, String> user = db.getUserDetails();
        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final String name = user.get("name");
        final String ztsid = user.get("ztsid");
        final String email = user.get("email");
        final String token = user.get("pass");
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);
        final String morta = zTxtMortality.getText().toString().trim().replaceAll(",", "");
        final String avgbd = zTxtWeight.getText().toString().trim().replaceAll(",", "");
        final String donum = txtdo.getText().toString().trim().replaceAll(",", "");
        final String recdt = zTxtDate.getText().toString().trim();
        final String feedc = fedar.toString();
        final String feedi = fedar2.toString();
//        Toast.makeText(getActivity(),feedc+" dan "+feedi,Toast.LENGTH_LONG).show();
        db.addLhk(ztsid,zfmid,recdt,morta,donum,avgbd,feedc, feedi, longi,latit);
        zTxtMortality.setText("");
        zTxtWeight.setText("");
        FeedForm1.removeAllViews();
        FeedForm2.removeAllViews();
        Toast.makeText(getActivity(),"LPAB tersimpan offline...",Toast.LENGTH_LONG).show();

        }

    public void checkDuplicate(){

    }

    @Override
    public void onClick(View view) {
        boolean dupli;
        switch (view.getId()) {
            case R.id.btnSaveLhk:
                countChild = FeedForm1.getChildCount();
                countChild2 = FeedForm2.getChildCount();
                boolean duplicate=false;
                boolean empty=false;
                float totFeed = 0;
                float totFeed2 = 0;
                //get feed
                fedar = new JSONArray();//clear fedar
                fedar2 = new JSONArray();//clear fedar
                for(int i=0;i<countChild;i++) {
                    JSONObject jobj = new JSONObject();
                    View ChildView = FeedForm1.getChildAt(i);
                    EditText txtFeed = (EditText) ChildView.findViewById(R.id.txtFeed);
                    TextView FeedCd = (TextView) ChildView.findViewById(R.id.FeedCd);
                    //check duplicate and empty field
//                    if(txtFeed.getText().toString().equals("")||txtFeed.getText().toString().equals("0")){
                    if(txtFeed.getText().toString().equals("")){
                        empty=true;
                    }
                    //done check duplicate and empty field
                    try {
                        jobj.put("fedcd",FeedCd.getText().toString());
                        jobj.put("quant",txtFeed.getText().toString());
                        if (!txtFeed.getText().toString().matches("")) {
                            totFeed = totFeed + Float.valueOf(txtFeed.getText().toString());
                            fedar.put(jobj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for(int i=0;i<countChild2;i++) {
                    JSONObject jobj = new JSONObject();
                    View ChildView = FeedForm2.getChildAt(i);
                    EditText txtFeed = (EditText) ChildView.findViewById(R.id.txtFeed);
                    TextView FeedCd = (TextView) ChildView.findViewById(R.id.FeedCd);
                    //check duplicate and empty field
                    if(txtFeed.getText().toString().equals("")||txtFeed.getText().toString().equals("0")){
                        empty=true;
                    }
                    //done check duplicate and empty field
                    try {
                        jobj.put("fedcd",FeedCd.getText().toString());
                        jobj.put("quant",txtFeed.getText().toString());
                        if (!txtFeed.getText().toString().matches("")) {
                            totFeed2 = totFeed2 + Float.valueOf(txtFeed.getText().toString());
                            fedar2.put(jobj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //done get feed
//                if(countChild==0){
//                    Toast.makeText(getActivity(),"tidak ada feed yang di transfer",Toast.LENGTH_LONG).show();
//                    return;
//                }
                if(duplicate){
                    Toast.makeText(getActivity(),"terdapat duplikasi Feed Type",Toast.LENGTH_LONG).show();
                    return;
                }
                if(empty){
                    Toast.makeText(getActivity(),"terdapat quantity yang kosong",Toast.LENGTH_LONG).show();
                    return;
                }
                String morta = zTxtMortality.getText().toString();
                String avgbw = zTxtWeight.getText().toString();
                String donum = txtdo.getText().toString();
                String feedc = Float.toString(totFeed);
                String feedc2 = Float.toString(totFeed2);
                String date = zTxtDate.getText().toString();
                String farm = spinner.getSelectedItem().toString().substring(11,spinner.getSelectedItem().toString().length());
                morta= zGeneralFunction.formatCurrent(morta);
                avgbw =  zGeneralFunction.formatCurrentDecimal(avgbw);
                feedc = zGeneralFunction.formatCurrent(feedc);
                feedc2 = zGeneralFunction.formatCurrent(feedc2);
                adb_lpab.setMessage("Farm\t\t\t\t\t: "+farm+"\nDate\t\t\t\t\t\t: "+date+"\nMortality\t\t\t: "+morta+" ekor\nAvg Weight\t: "+avgbw+" gr\nNo. Do\t\t\t\t: "+donum+"\nFeed Use\t\t\t: "+feedc+" karung\nFeed In\t\t\t\t: "+feedc2+" karung");
                adb_lpab.show();
//                try {
//                    do_register_LHK();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                break;

            case R.id.zbtnDate:
                showDatePickerDialog(view);
                break;
            case R.id.btnAddFeed1:
                int countChildFeed1 = FeedForm1.getChildCount();
                dupli =  false;
                for(int i=0;i<countChildFeed1;i++){
                    View ChildView = FeedForm1.getChildAt(i);
                    TextView FeedCd = (TextView) ChildView.findViewById(R.id.FeedCd);
                    if(spinnerFeed1.getSelectedItem().toString().equals(FeedCd.getText().toString())){
                        dupli=true;
                    }
                }
//                Toast.makeText(getActivity(),"Feed 1 In Klik",Toast.LENGTH_LONG).show();
                if(dupli){
                    Toast.makeText(getActivity(),"Kode Feed tidak boleh sama",Toast.LENGTH_LONG).show();
                }
                else {
                    AddForm(spinnerFeed1.getSelectedItem().toString());
                }
                break;
            case R.id.btnAddFeed2:
                int countChildFeed2 = FeedForm2.getChildCount();
                dupli =  false;
                for(int i=0;i<countChildFeed2;i++){
                    View ChildView = FeedForm2.getChildAt(i);
                    TextView FeedCd = (TextView) ChildView.findViewById(R.id.FeedCd);
                    if(spinnerFeed2.getSelectedItem().toString().equals(FeedCd.getText().toString())){
                        dupli=true;
                    }
                }
                if(dupli){
                    Toast.makeText(getActivity(),"Kode Feed tidak boleh sama",Toast.LENGTH_LONG).show();
                }
                else {
//                Toast.makeText(getActivity(),"Feed 2 In Klik",Toast.LENGTH_LONG).show();
                    AddForm2(spinnerFeed2.getSelectedItem().toString());
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getActivity(),"jalan",Toast.LENGTH_LONG).show();
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
}
