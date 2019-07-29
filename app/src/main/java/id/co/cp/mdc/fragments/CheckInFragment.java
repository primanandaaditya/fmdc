package id.co.cp.mdc.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.cp.mdc.LhkActivity;
import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.Farmer;
import id.co.cp.mdc.helper.SQLiteHandler;


public class CheckInFragment extends Fragment implements View.OnClickListener, OnItemSelectedListener,GoogleApiClient.ConnectionCallbacks,LocationListener,
        GoogleApiClient.OnConnectionFailedListener {
    private ProgressDialog pDialog;
    private Spinner spinner;
    private Spinner spinnerAddress;
    private String name;
    private String image;
    private String email;
    private String token;
    private String ztsid;
    private EditText ztxtDesc;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Checkin";
    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview, imgEmpty;

    private Button btnCheckin;

    // LogCat tag
    private static final String TAG = LhkActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    // UI elements
    private Button btnCapturePicture;
    private Button btnAbsen;
    private TextView lblLong;
    private TextView lblLat;
    private SQLiteHandler db;
    private String fkgps;

    public CheckInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_check_in, container, false);

        db = new SQLiteHandler(getActivity().getApplicationContext());
        super.onCreate(savedInstanceState);
        imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);
        imgEmpty = (ImageView) rootView.findViewById(R.id.imgEmpty);

        btnCapturePicture = (Button) rootView.findViewById(R.id.btnCapturePicture);
        btnAbsen = (Button) rootView.findViewById(R.id.btnAbsen);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //connect spinner dan add listener
        spinner = (Spinner) rootView.findViewById(R.id.spinnerFarmer);
        spinnerAddress = (Spinner) rootView.findViewById(R.id.spinnerAddress);
        ztxtDesc = (EditText) rootView.findViewById(R.id.ztxtDesc);
        spinner.setOnItemSelectedListener(this);
        spinnerAddress.setOnItemSelectedListener(this);
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        email = user.get("email");
        token = user.get("pass");
        ztsid = user.get("ztsid");
        FillSpinner();
        lblLong = (TextView) rootView.findViewById(R.id.lblLong);
        lblLat = (TextView) rootView.findViewById(R.id.lblLat);



        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
        mGoogleApiClient.connect();

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });


        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            getActivity().finish();
        }

        // Toggling the periodic location updates
        btnAbsen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String text = spinner.getSelectedItem().toString().substring(0,8);
//                text += "text";
//                Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
//                Toast.makeText(CheckInActivity.this,image,Toast.LENGTH_LONG).show();
                if(image==null){
                    Toast.makeText(getActivity().getApplicationContext(),"harap mengambil foto terlebih dahulu...",Toast.LENGTH_LONG).show();
                }
                else {
//                    Toast.makeText(getActivity().getApplicationContext(),ztxtDesc.getText().toString(),Toast.LENGTH_LONG).show();
                    if(fkgps.equals("y")){
                        Toast.makeText(getActivity().getApplicationContext(),"Harap matikan mock location",Toast.LENGTH_LONG).show();
                    }else{
                        do_absence();
                    }
                }
            }
        });
        return rootView;
    }
    private void do_absence(){
        displayLocation();
        showpDialog();
        final String longi = lblLong.getText().toString().trim();
        final String latit = lblLat.getText().toString().trim();
        final String zfmid = spinner.getSelectedItem().toString().substring(0,8);
        final String descr = ztxtDesc.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.urlPimsFarm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        hidepDialog();
                        if(response.substring(0,7).matches("Session")){
                            db.deleteTabel();

                            // Launching the login activity
                            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else{
                            imgPreview.setVisibility(View.GONE);
                            imgEmpty.setVisibility(View.VISIBLE);
                            image = null;
                            ztxtDesc.setText("");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),"Take photo belum bisa offline. Harap mencari signal untuk melakukan taking photo",Toast.LENGTH_LONG).show();
//                        db.addImage(latit, longi, ztsid ,zfmid, "android", "absence", email, AppConfig.clien, image, descr, token);
//                        Toast.makeText(getActivity().getApplicationContext(),latit+":"+longi,Toast.LENGTH_LONG).show();
//                        imgPreview.setVisibility(View.GONE);
//                        imgEmpty.setVisibility(View.VISIBLE);
//                        image = null;
//                        ztxtDesc.setText("");
                        hidepDialog();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("latit",latit);
                params.put("longi",longi);
                params.put("ztsid", ztsid);
                params.put("zfmid", zfmid);
                params.put("mobile", "android");
                params.put("type", "absence");
                params.put("zuser", email);
                params.put("clien", AppConfig.clien);
                params.put("image", image);
                params.put("descr", descr);
                params.put("token", token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
        spinnerAddress.setSelection(AppConfig.FmSpinNum);
        spinner.setSelection(AppConfig.FmSpinNum);
    }


    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
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
        DialogFragment newFragment = new DatePickerFragment();
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

    private void previewCapturedImage() {
        try {
            imgPreview.setVisibility(View.VISIBLE);
            imgEmpty.setVisibility(View.GONE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 12;

            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            ExifInterface ei = new ExifInterface(fileUri.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap=rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap=rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap=rotateImage(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
            imgPreview.setImageBitmap(bitmap);
            image=getStringImage(bitmap);
//            Matrix matrix = new Matrix();
//            imgPreview.setScaleType(ImageView.ScaleType.MATRIX);   //required
//            //matrix.postRotate(90f, (imgPreview.getDrawable().getBounds().width()/2)*0.88f, (imgPreview.getDrawable().getBounds().height()/2)*1.9f);
//            imgPreview.setImageMatrix(matrix);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerAddress.setSelection(position);
        spinner.setSelection(position);
        AppConfig.FmSpinNum=position;
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

            case R.id.btnAbsen:
                do_absence();
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

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();

    }
}
