package id.co.cp.mdc.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.Farmer;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.helper.zGeneralFunction;


public class FourFragment extends Fragment implements View.OnClickListener, OnItemSelectedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private ProgressDialog pDialog;
    private Spinner spinner;
    private Spinner spinnerAddress;
    private SQLiteHandler db;
    private Button zbtnDate;
    private TextView zTxtDate4;
    //wajib buat maps
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private boolean mRequestingLocationUpdates = false;
    private Date cDate = new Date();
    //tutup wajib maps


    private TextView lblLat;
    private TextView lblLong;
    private EditText ztxtFnlQt;
    private EditText ztxtFnlBw;
    private TextView ztxtAvgBw;
    private Button btnSaveFnl;
    private RadioButton radioCC;
    private RadioButton radioNC;
    private AlertDialog.Builder adb_sppa;
    private String isFinal;
    private String fkgps;
    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_four, container, false);
        zbtnDate = (Button) rootView.findViewById(R.id.zbtnDate);
        btnSaveFnl = (Button) rootView.findViewById(R.id.btnSaveFnl);
        spinner = (Spinner) rootView.findViewById(R.id.spinnerFarmer);
        spinnerAddress = (Spinner) rootView.findViewById(R.id.spinnerAddress);
        radioCC = (RadioButton) rootView.findViewById(R.id.radioCC);
        radioNC = (RadioButton) rootView.findViewById(R.id.radioNC);
        spinner.setOnItemSelectedListener(this);
        spinnerAddress.setOnItemSelectedListener(this);
        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        lblLat = (TextView) rootView.findViewById(R.id.lblLat);
        lblLong = (TextView) rootView.findViewById(R.id.lblLong);
        ztxtFnlBw = (EditText) rootView.findViewById(R.id.ztxtFnlBw);
        ztxtAvgBw = (TextView) rootView.findViewById(R.id.ztxtAvgBw);
        zTxtDate4 = (TextView) rootView.findViewById(R.id.zTxtDate4);
        zTxtDate4.setText(DateNow);
        ztxtFnlQt = (EditText) rootView.findViewById(R.id.ztxtFnlQt);
        pDialog = new ProgressDialog(getActivity());
        ztxtFnlBw.setRawInputType(Configuration.KEYBOARD_12KEY);
        ztxtFnlBw.setKeyListener(DigitsKeyListener.getInstance(true,true));
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        adb_sppa = new AlertDialog.Builder(getActivity());
        adb_sppa.setIcon(android.R.drawable.ic_dialog_alert);
        adb_sppa.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if(fkgps.equals("y")){
                        Toast.makeText(getActivity(),"Harap matikan mock location",Toast.LENGTH_LONG).show();
                    }else{
                        do_register_final();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } });
        adb_sppa.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });


        zbtnDate.setOnClickListener(this);
        btnSaveFnl.setOnClickListener(this);

//        ztxtFnlQt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(ztxtFnlBw.getText().toString().matches("") || ztxtFnlQt.getText().toString().matches("")){
//
//                }
//                else {
//                    Integer weight = Integer.valueOf(ztxtFnlBw.getText().toString().replaceAll(",", ""));
//                    Integer qty = Integer.valueOf(ztxtFnlQt.getText().toString().replaceAll(",", ""));
//                    Integer avg = weight / qty;
//                    ztxtAvgBw.setText(avg.toString());
//                }
//            }
//        });
//        ztxtFnlBw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(ztxtFnlBw.getText().toString().matches("") || ztxtFnlQt.getText().toString().matches("")){
//
//                }
//                else {
//                    Integer weight = Integer.valueOf(ztxtFnlBw.getText().toString().replaceAll(",", ""));
//                    Integer qty = Integer.valueOf(ztxtFnlQt.getText().toString().replaceAll(",", ""));
//                    Integer avg = weight / qty;
//                    ztxtAvgBw.setText(avg.toString());
//                }
//            }
//        });

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        mGoogleApiClient.connect();

        FillSpinner();
        return rootView;
    }
    private void FillSpinner(){
        List<String> categoriesFarm = new ArrayList<String>();
        List<String> categoriesAddr = new ArrayList<String>();

        List<Farmer> Farmers = db.getAllFarmer();
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

    }
    private void offline_register_final(){
        HashMap<String, String> user = db.getUserDetails();
        final String fnlqt = ztxtFnlQt.getText().toString().trim().replaceAll(",", "");
        final String fnlbw = ztxtFnlBw.getText().toString().trim().replaceAll(",", "");
        final String name = user.get("name");
        final String ztsid = user.get("ztsid");
        final String email = user.get("email");
        final String token = user.get("pass");
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);
        final String recdt = zTxtDate4.getText().toString().trim();
        if(radioCC.isChecked()) {
            isFinal = "X";
        }
        else{
            isFinal = "";
        }
        db.add_final(ztsid, zfmid, recdt, fnlqt, fnlbw, isFinal, longi, latit);
        ztxtFnlBw.setText("");
        ztxtFnlQt.setText("");
        radioCC.setChecked(false);
        Toast.makeText(getActivity(),"Data tersimpan offline...",Toast.LENGTH_LONG).show();

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
    public void showDatePickerDialog(View v) {
        DatePickerFragment datepicker = new DatePickerFragment();
        datepicker.setTxtDate("zTxtDate4");
        DialogFragment newFragment = datepicker;
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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

    private void do_register_final() throws ParseException {
        displayLocation();
        HashMap<String, String> user = db.getUserDetails();
        final String fnlqt = ztxtFnlQt.getText().toString().trim().replaceAll(",", "");
        final String fnlbw = ztxtFnlBw.getText().toString().trim().replaceAll(",", "");
        final String name = user.get("name");
        final String ztsid = user.get("ztsid");
        final String email = user.get("email");
        final String token = user.get("pass");
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);
        final String recdt = zTxtDate4.getText().toString().trim();
        final String isFinal;
        if(radioCC.isChecked()) {
            isFinal = "X";
        }
        else if(radioNC.isChecked()){
            isFinal = "N";
        }
        else{
            Toast.makeText(getActivity(), "Harap centang tanda 'Close Cycle' untuk mengakhiri siklus kandang atau 'New Cycle' untuk memulai siklus kandang", Toast.LENGTH_LONG).show();
            return;
        }
//        if(fnlqt.matches("")||fnlbw.matches("")){
//            Toast.makeText(getActivity(), "Data tidak boleh kosong...", Toast.LENGTH_LONG).show();
//            return;
//        }
//        else if(fnlbw.contains("-")){
//            Toast.makeText(getActivity(), "Berat tidak boleh minus...", Toast.LENGTH_LONG).show();
//            return;
//        }
//        else if(Float.parseFloat(fnlbw)>4000) {
//            Toast.makeText(getActivity(), "Berat ayam tidak boleh lebih dari 4 Kg", Toast.LENGTH_LONG).show();
//            hidepDialog();
//            return;
//        }

        showpDialog();
        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
        Date date1 = sdf.parse(recdt);
        Date date2 = sdf.parse(DateNow);
        long diff = (date2.getTime()-date1.getTime())/(24*60*60*1000);
//        if(lblLat.getText()==""||lblLong.getText()==""){
//            Toast.makeText(getActivity().getApplicationContext(),
//                    "Mohon aktifkan GPS pada device dan tunggu selama 5 detik. Jalankan aplikasi Google Maps jika pesan ini tetap berlanjut.", Toast.LENGTH_LONG)
//                    .show();
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//            hidepDialog();
//            return;
//        }
//        else if(date1.after(date2)){
//            Toast.makeText(getActivity(), "Tanggal tidak valid", Toast.LENGTH_LONG).show();
//            hidepDialog();
//            return;
//        }
//        else if(diff>(AppConfig.interval_time)){//3 hari
//            Toast.makeText(getActivity(), "Tanggal tidak boleh lebih dari "+AppConfig.interval_time+" hari", Toast.LENGTH_LONG).show();
//            hidepDialog();
//            return;
//        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.urlPimsFarm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response==""){
                        }
                        else {
                            Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                            if(response.substring(0,7).matches("Session")){
                                db.deleteTabel();

                                // Launching the login activity
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            if(response.substring(0,8).matches("Berhasil")) {
                                ztxtFnlBw.setText("");
                                ztxtFnlQt.setText("");
                                radioCC.setChecked(false);
                            }
                            hidepDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        offline_register_final();
                        Toast.makeText(getActivity(),"Harap mencari signal untuk melakukan new/close cycle. Jika pesan ini terus berlanjut harap menghubungi tim FMDC.",Toast.LENGTH_LONG).show();
                        hidepDialog();
//                        Toast.makeText(FarmerActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("mobile", "android");
                params.put("type", "final");
                params.put("latit",latit);
                params.put("longi",longi);
                params.put("ztsid", ztsid);
                params.put("zuser", name);
                params.put("clien", AppConfig.clien);
                params.put("zfmid",zfmid);
                params.put("fnlqt",fnlqt);
                params.put("fnlbw",fnlbw);
                params.put("recdt",recdt);
                params.put("token",token);
                params.put("zuser",email);
                params.put("isFinal",isFinal);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSaveFnl:
//                try {
//                    do_register_final();
//                }
//                catch (ParseException e) {
//                    e.printStackTrace();
//                }

                String fnlqt = ztxtFnlQt.getText().toString();
                String fnlbw = ztxtFnlBw.getText().toString();
                String date = zTxtDate4.getText().toString();
                String farm = spinner.getSelectedItem().toString().substring(11,spinner.getSelectedItem().toString().length());
                Float AvgBw;
                if(!ztxtFnlQt.getText().toString().equals("") && !ztxtFnlBw.getText().toString().equals("")) {
                    Float bw = Float.valueOf(ztxtFnlBw.getText().toString());
                    Float qt = Float.valueOf(ztxtFnlQt.getText().toString());
                    AvgBw = bw / qt;
                }
                else{
                    AvgBw = Float.valueOf("0");
                }
                fnlqt = zGeneralFunction.formatCurrent(fnlqt);
                fnlbw = zGeneralFunction.formatCurrentDecimal(fnlbw);
                if(radioCC.isChecked()) {
                    adb_sppa.setTitle("Konfirmasi Close Cycle");
                    adb_sppa.setMessage("Apakah anda yakin akan close cycle dan sudah melakukan transaksi TRANSFER FEED?\nFarm\t\t\t\t\t\t: " + farm + "\n");
                    adb_sppa.show();
                }
                else if(radioNC.isChecked()){
                    adb_sppa.setTitle("Konfirmasi New Cycle");
                    adb_sppa.setMessage("Apakah anda yakin akan new cycle?\nFarm\t\t\t\t\t\t: " + farm + "\n");
                    adb_sppa.show();
                }
                else{
                    Toast.makeText(getActivity(),"Anda belum memilih pilihan.",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.zbtnDate:
                showDatePickerDialog(view);
                break;

            default:
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
