package id.co.cp.mdc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.controller.UserListController;
import id.co.cp.mdc.helper.MonitoringAdapter;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.helper.UserList;
import id.co.cp.mdc.interfaces.IUserList;
import id.co.cp.mdc.model.UserListModel;


public class MonitoringActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MonitoringAdapter adapter;
    private ListView listView;
    private List<UserList> MonitorList =  new ArrayList<UserList>();
    private Toolbar toolbar;
    SQLiteHandler db;
    private ProgressDialog pDialog;
    UserListController userListController;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MonitoringActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pDialog = new ProgressDialog(MonitoringActivity.this);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        db = new SQLiteHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.MonitorList);
        adapter = new MonitoringAdapter(this, MonitorList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MonitoringActivity.this,MonitorList.get(i).getZtsid(),Toast.LENGTH_LONG).show();
//                get_farmlist(MonitorList.get(i).getZtsid());
//                AppConfig.SelectedUser.setProfile(MonitorList.get(i).getProfile());
//                AppConfig.SelectedUser.setUsrname(MonitorList.get(i).getUsername());
//
                AppConfig.SelectedTS = MonitorList.get(i).getZtsid();
                AppConfig.Ztsnm =  MonitorList.get(i).getUsername();
                Intent intent = new Intent(MonitoringActivity.this, MonitoringActivity2.class);
                startActivity(intent);

            }
        });
   //     get_url();
    get_userlist();

    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(MonitoringActivity.this, MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
    private void get_farmlist(String ztsid){
        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("ztsid", ztsid);
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
            e.printStackTrace();
//            hidepDialog();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlMonitoring2, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("monitor2",response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_monitor2",error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);

    }
    private void get_userlist(){



        showpDialog();
        //set params
        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("token", user.get("pass"));
            JObj.put("ztype", "monitor");
            JObj.put("zuser", user.get("email"));
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
            e.printStackTrace();
            hidepDialog();
        }

        JsonObjectRequest JReq = new JsonObjectRequest(Request.Method.POST, AppConfig.urlMonitoring1, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("status");
//                            Toast.makeText(MonitoringActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                            hidepDialog();
                            if(stat.equals("0")) {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject json = data.getJSONObject(i);
                                    UserList userList = new UserList();
                                    userList.setProfile(AppConfig.server_ip+json.getString("url"));
                                    userList.setUsrname(json.getString("zuser"));
                                    userList.setTag(json.getString("mytag")+"/"+json.getString("remain"));
                                    userList.setTime(json.getString("ltdte")+" : "+json.getString("lttme"));
                                    userList.setRunnm(String.valueOf(i+1));
                                    userList.setZtsid(json.getString("ztsid"));
                                    MonitorList.add(userList);
                                }
                            }
                            else if(stat.equals("2")){
                                Toast.makeText(MonitoringActivity.this,"Session Time Out.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MonitoringActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MonitoringActivity.this,"server sedang dalam perbaikan.",Toast.LENGTH_LONG).show();
                               Intent intent = new Intent(MonitoringActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        catch (JSONException e) {
//                            Toast.makeText(MonitoringActivity.this,"tidak ada otorisasi, Silahkan hubungi tim FMDC",Toast.LENGTH_LONG).show();
                            Toast.makeText(MonitoringActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                            finish();
                        }
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MonitoringActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(MonitoringActivity.this,"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
                        hidepDialog();
                        Intent intent = new Intent(MonitoringActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
        JReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(JReq);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
