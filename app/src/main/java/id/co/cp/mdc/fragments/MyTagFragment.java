package id.co.cp.mdc.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.SQLiteHandler;


public class MyTagFragment extends Fragment implements OnMapReadyCallback,OnClickListener, OnItemSelectedListener
        {
    private GoogleMap mMap;
    private LatLng mytag;
    private ProgressDialog pDialog;

    private String name;
    private String email;
    private String token;



    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;




    // LogCat tag
    private static final String TAG = MyTagFragment.class.getSimpleName();



    // UI elements
    //private TextView lblLong;
    //private TextView lblLat;
   // private Button btnShowLocation;
    //private Button btnRegis;
    private SQLiteHandler db;
    private String selFmId;

    double longitude, latitude;

    public MyTagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_mytag, container, false);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        getFragmentManager();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mytag);
        mapFragment.getMapAsync(this);

        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);



        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        email = user.get("email");
        token = user.get("pass");


        if (checkPlayServices()) {


        }


        return rootView;
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



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onClick(View view) {


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        final JSONObject JObj = new JSONObject();

        JSONObject JList = new JSONObject();
        try {
            JList.put("zuser",email);
            JList.put("clien", AppConfig.clien);
            JList.put("token",token);
            JList.put("mapty", AppConfig.mapty);

            Log.d("dd", "zuser: " + email);
            Log.d("dd", "clien: "+ AppConfig.clien);
            Log.d("dd", "token: " + token);
            Log.d("dd", "mapty: "+ AppConfig.mapty);


        } catch (JSONException e) {
            e.printStackTrace();
        }

                showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppConfig.urlMyTag, JList, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray j;
                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG ) .show();
//                        Log.e("hasil_my_tag",response.toString());

                        try {
                            j =response.getJSONArray("data");
                            for (int i=0;i< j.length();i++){
                                JSONObject data;
                                data=j.getJSONObject(i);
                                double latit = Double.parseDouble(data.getString("latit"));
                                double longi = Double.parseDouble(data.getString("longi"));
                                String famnm = data.getString("famnm");
                                String arenm = data.getString("arenm");
                                String tsnme = data.getString("tsnme");
                                String fmadr = data.getString("fmadr");
                                String brcnm = data.getString("brcnm");
                                String famcd = data.getString("mapcd");
                                mytag = new LatLng(latit, longi);

                                mMap.addMarker(new MarkerOptions().position(mytag).title(famnm)).showInfoWindow();
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mytag,10.0f));
                        } catch (JSONException e) {
                           Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG ) .show();
                        }
                        hidepDialog();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity().getApplicationContext(), "Tidak terhubung dengan server. Harap mencari signal", Toast.LENGTH_LONG).show();
                       // Toast.makeText(getActivity(), error.toString()+AppConfig.cpis_ip+AppConfig.urlMyTag, Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsObjRequest);


    }


}
