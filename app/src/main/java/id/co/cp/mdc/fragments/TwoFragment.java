package id.co.cp.mdc.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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


public class TwoFragment extends Fragment implements View.OnClickListener, OnItemSelectedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private ProgressDialog pDialog;
    private Spinner spinner;
    private Spinner spinnerAddress;
    private SQLiteHandler db;
    private Button zbtnDate;
    private TextView zTxtDate1;
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
    private EditText ztxtDoc;
    private EditText ztxtToler;
    private Button btnSaveDoc;
    private Button btnGetToler;
    private String fkgps;

    private AlertDialog.Builder adb_doc;
    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);
        zbtnDate = (Button) rootView.findViewById(R.id.zbtnDate);
        btnSaveDoc = (Button) rootView.findViewById(R.id.btnSaveDoc);
        spinner = (Spinner) rootView.findViewById(R.id.spinnerFarmer);
        spinnerAddress = (Spinner) rootView.findViewById(R.id.spinnerAddress);
        spinner.setOnItemSelectedListener(this);
        spinnerAddress.setOnItemSelectedListener(this);
        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        lblLat = (TextView) rootView.findViewById(R.id.lblLat);
        lblLong = (TextView) rootView.findViewById(R.id.lblLong);
        ztxtToler = (EditText) rootView.findViewById(R.id.ztxtToler);
        zTxtDate1 = (TextView) rootView.findViewById(R.id.zTxtDate1);
        btnGetToler = (Button) rootView.findViewById(R.id.btnGetToler);
        zTxtDate1.setText(DateNow);
        ztxtDoc = (EditText) rootView.findViewById(R.id.ztxtDoc);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        db = new SQLiteHandler(getActivity().getApplicationContext());


        zbtnDate.setOnClickListener(this);
        btnSaveDoc.setOnClickListener(this);
        btnGetToler.setOnClickListener(this);
        adb_doc = new AlertDialog.Builder(getActivity());
        adb_doc.setTitle("Konfirmasi DOC IN");
        adb_doc.setIcon(android.R.drawable.ic_dialog_alert);
        adb_doc.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if(fkgps.equals("y")){
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Harap matikan mock location", Toast.LENGTH_LONG)
                                .show();
                    }else{
                        do_register_doc();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } });


        adb_doc.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });



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
        ArrayAdapter<String> dataAdapterFarm = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoriesFarm);
        ArrayAdapter<String> dataAdapterAddr = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoriesAddr);
        dataAdapterFarm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterAddr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterFarm);
        spinnerAddress.setAdapter(dataAdapterAddr);

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
        datepicker.setTxtDate("zTxtDate1");
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

    private void do_register_doc() throws ParseException {
        displayLocation();
        HashMap<String, String> user = db.getUserDetails();
        final String docin = ztxtDoc.getText().toString().trim().replaceAll(",", "");
        final String toler = ztxtToler.getText().toString().trim().replaceAll(",", "");
        final String name = user.get("name");
        final String ztsid = user.get("ztsid");
        final String email = user.get("email");
        final String token = user.get("pass");
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);
        final String recdt = zTxtDate1.getText().toString().trim();
        if(docin.matches("")||toler.matches("")){
            Toast.makeText(getActivity(), "Data tidak boleh kosong...", Toast.LENGTH_LONG).show();
            return;
        }
//        else if(Float.parseFloat(toler)>Float.parseFloat(docin)*0.02){
//            Toast.makeText(getActivity(), "Toleransi tidak boleh lebih dari 2%", Toast.LENGTH_LONG).show();
//            return;
//        }

        showpDialog();
        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
        Date date1 = sdf.parse(recdt);
        Date date2 = sdf.parse(DateNow);
        long diff = (date2.getTime()-date1.getTime())/(24*60*60*1000);
        if(lblLat.getText()==""||lblLong.getText()==""){
            Toast.makeText(getActivity().getApplicationContext(),
                    "Mohon aktifkan GPS pada device dan tunggu selama 5 detik. Jalankan aplikasi Google Maps jika pesan ini tetap berlanjut.", Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
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
                            if(response.substring(0,8).equals("Berhasil")) {
                                ztxtToler.setText("");
                                ztxtDoc.setText("");
                            }
                            hidepDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        offline_register_doc();
//                        Toast.makeText(getActivity(),"tidak terhubung dengan server...",Toast.LENGTH_LONG).show();
                        hidepDialog();
//                        Toast.makeText(FarmerActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("mobile", "android");
                params.put("type", "doc");
                params.put("latit",latit);
                params.put("longi",longi);
                params.put("ztsid", ztsid);
                params.put("zuser", name);
                params.put("clien", AppConfig.clien);
                params.put("zfmid",zfmid);
                params.put("docqt",docin);
                params.put("doctr",toler);
                params.put("recdt",recdt);
                params.put("token",token);
                params.put("zuser",email);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void offline_register_doc(){
        HashMap<String, String> user = db.getUserDetails();
        final String name = user.get("name");
        final String ztsid = user.get("ztsid");
        final String email = user.get("email");
        final String token = user.get("pass");
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);
        final String docqt = ztxtDoc.getText().toString().trim().replaceAll(",", "");
        final String doctr = ztxtToler.getText().toString().trim().replaceAll(",", "");
        final String recdt = zTxtDate1.getText().toString().trim();
        db.add_doc(ztsid, zfmid, recdt, docqt, doctr, longi, latit);
        ztxtToler.setText("");
        ztxtDoc.setText("");
        Toast.makeText(getActivity(),"DOC tersimpan offline...",Toast.LENGTH_LONG).show();

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

            case R.id.btnSaveDoc:
//                try {
//                    do_register_doc();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                if(ztxtDoc.getText().toString().equals("")){
                    ztxtToler.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "Populasi tidak boleh kosong!", Toast.LENGTH_LONG).show();
                }
                if(ztxtToler.getText().toString().equals("")){
                    GetToler();
                }else{
                    String docin = ztxtDoc.getText().toString();
                    String toler = ztxtToler.getText().toString();
                    String total = String.valueOf(Integer.parseInt(ztxtDoc.getText().toString())+Integer.parseInt(ztxtToler.getText().toString()));
                    String date = zTxtDate1.getText().toString();
                    String farm = spinner.getSelectedItem().toString().substring(11,spinner.getSelectedItem().toString().length());
                    docin = zGeneralFunction.formatCurrent(docin);
                    toler = zGeneralFunction.formatCurrent(toler);
                    total = zGeneralFunction.formatCurrent(total);
                    adb_doc.setMessage("Farm\t\t\t\t: "+farm+"\nDate\t\t\t\t\t: "+date+"\nPopulasi\t\t: "+docin+" ekor\nToleransi\t\t: "+toler+" ekor\nTotal\t\t\t\t: "+total+" ekor");
                    adb_doc.show();
                }
                break;

            case R.id.zbtnDate:
                showDatePickerDialog(view);
                break;

            case R.id.btnGetToler:
                GetToler();
                break;

            default:
                break;
        }

    }

    private void GetToler(){
        if(!ztxtDoc.getText().toString().equals("")){
            Integer qty = Integer.valueOf(ztxtDoc.getText().toString());
            Integer toler;
            toler = 1*qty/100;
            ztxtToler.setText(toler.toString());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
