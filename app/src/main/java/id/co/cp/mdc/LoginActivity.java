package id.co.cp.mdc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.helper.SessionManager;


public class LoginActivity extends AppCompatActivity implements OnItemSelectedListener, OneSignal.NotificationOpenedHandler {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Spinner SpinnerClien;
    private String urlGetFarm;
    private String clienId;
    private String dvcid;
    private String token;
    private String zuser;
    private CheckBox CheckShowPass;
    ArrayList<ModSpinner> clienSpin;
    private TextView txtVersion;
    private SQLiteHandler db;











    @Override
    public void onCreate(Bundle savedInstanceState) {
        Context context = this;
        String version = "";

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            Log.e("YourActivity", "Error getting version");
        }


        super.onCreate(savedInstanceState);
        OneSignal.startInit(this).init();
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                dvcid=userId;
            }
        });
        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Sync hashed email if you have a login system or collect it.
        //   Will be used to reach the user at the most optimal time of day.
        // OneSignal.syncHashedEmail(userEmail);
        setContentView(R.layout.activity_login);
        txtVersion = (TextView)findViewById(R.id.txtVersion);
        txtVersion.setText(version);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        //inputClien = (Spinner) findViewById(R.id.clien);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        SpinnerClien = (Spinner) findViewById(R.id.clien);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        CheckShowPass = (CheckBox) findViewById(R.id.CheckShowPass);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        SpinnerClien.setOnItemSelectedListener(this);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user= db.getUserDetails();
        token = user.get("pass");
//        Toast.makeText(getApplicationContext(),token, Toast.LENGTH_LONG)
//                .show();

        if(token!=null){
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            //ambil data dari ajax untuk spinner
            pDialog.setMessage("Sedang mengambil data ...");
            makeJsonArrayRequest();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String clien = clienId;
                clien = clien.substring(0,2);
                if(email.contains("@")){
                    email=email.substring(0,email.indexOf("@"));
                }

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password, clien);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        CheckShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    inputPassword.setInputType(129);
                }
            }
        });

    }
    /**
     * Method to make json array request where response starts with [
     * */
    private void login_process(){
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("usrid", inputEmail.getText().toString());
            JObj.put("paswd", inputEmail.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,"http:10.1.3.106/eis/test_login.php",  JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("stat");
                            String message = response.getString("message");
                            if(stat.equals("0")){
                                Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //reponse nya terserah mau diapain
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
//        sesuatu
    }

    private void makeJsonArrayRequest() {
        showDialog();

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                            FillSpinner(response);
//                        hideDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"tidak terhubung dengan server", Toast.LENGTH_SHORT).show();
                hideDialog();
                finish();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
    private void FillSpinner(JSONArray j){//respon
        clienSpin= new ArrayList<ModSpinner>();
        List<String> categoriesClien = new ArrayList<String>();
        //break
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);//dapet 1 data
                ModSpinner clienList = new ModSpinner();
                clienList.setZid(json.getString(AppConfig.TAG_CODE));
                clienList.setDesc(json.getString(AppConfig.TAG_NAME));
                clienList.setFlag("0");
                clienSpin.add(clienList);
                String clien = json.getString(AppConfig.TAG_NAME);
                categoriesClien.add(clien);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        hideDialog();
        ArrayAdapter<String> dataAdapterClien = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesClien);
        dataAdapterClien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerClien.setAdapter(dataAdapterClien);

    }

//    private void GetFarm(){
//        JsonArrayRequest req = new JsonArrayRequest(urlGetFarm,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        InsertFarm(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(),"tidak terhubung dengan server PIMS", Toast.LENGTH_SHORT).show();
//                db.deleteTabel();
//                hideDialog();
//            }
//        });
//
//        req.setRetryPolicy(new DefaultRetryPolicy(
//                500000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(req);
//
//    }

    private void GetFarm(){
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
                                InsertFarm(res_arr);
                                db.addJsonMsArea(arr_MsArea);
                                db.addJsonMsBranch(arr_MsBranch);
                                db.addJsonMsFarm(arr_MsFarm);
                                db.addJsonMsTs(arr_MsTs);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"tidak terhubung dengan server PIMS", Toast.LENGTH_SHORT).show();
                                db.deleteTabel();
                                hideDialog();
                            }
                        }
                        catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"tidak terhubung dengan server PIMS", Toast.LENGTH_SHORT).show();
                            db.deleteTabel();
                            hideDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"tidak terhubung dengan server PIMS", Toast.LENGTH_SHORT).show();
                        Log.e("error_get_data",error.toString());
                        db.deleteTabel();
                        hideDialog();
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
        send_device_id();
        hideDialog();
//        hideDialog();

    }


    private void checkLogin(final String email, final String password, final String clien) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";


        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,//login eldap
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //dapetin data dari json login_ajax
                        String name = jObj.getJSONObject("user").getString("name");
                        String ztsid = jObj.getJSONObject("user").getString("sapid");
                        token = jObj.getJSONObject("user").getString("token");
                        String email = jObj.getJSONObject("user").getString("email");
                        zuser = email;
                        //add login data ke sqlite
                        db.deleteTabel();
                        db.addUser(name, email, "001", clien, token, ztsid);
                        //tarik data email dari sqlite
//                        Toast.makeText(getApplicationContext(), ztsid.toString(), Toast.LENGTH_LONG).show();
                        if(ztsid.length()!=8){
                            send_device_id();
                        }
                        else {
                            final String areid = ztsid.substring(0, 2);
                            final String brcid = ztsid.substring(2, 5);
                            final String tsid = ztsid.substring(5, 8);
//                        urlGetFarm = "http://10.1.3.231/pims_farmer.php?area_id="+areid+"&branch_id="+brcid+"&ts_id="+tsid+"&farmer_id=";
//                        urlGetFarm = "http://"+AppConfig.ip+"/eis/lcfm/lcfm_load_view_farmer_ajax.php?area_id="+areid+"&branch_id="+brcid+"&ts_id="+tsid+"&farmer_id=";
                            urlGetFarm = "https://cpis.cp.co.id/lcfm/lcfm_load_view_farmer_ajax.php?area_id=" + areid + "&branch_id=" + brcid + "&ts_id=" + tsid + "&farmer_id=";
                            GetFarm();
                            session.setLogin(true);
                        }
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.toString());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ztxtEmail", email);
                params.put("zpwdPassword", password);
                params.put("mobile", "android");
                params.put("zslcClient", clien);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void send_device_id(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.urlDvcId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.matches("0")){
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            db.deleteTabel();
                            Toast.makeText(LoginActivity.this,"gagal mengirim device id",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.deleteTabel();
                        Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("clien", AppConfig.clien);
                params.put("zuser", zuser);
                params.put("token", token);
                params.put("dvcid", dvcid);
                params.put("dvcmd", Build.MODEL);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clienId=clienSpin.get(i).getZid();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        Toast.makeText(getApplicationContext(),
                "Notifikasi berhasil di klik", Toast.LENGTH_LONG)
                .show();

    }
}
