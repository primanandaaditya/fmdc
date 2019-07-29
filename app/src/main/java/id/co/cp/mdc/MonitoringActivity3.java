package id.co.cp.mdc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.helper.ActivityAdapter;
import id.co.cp.mdc.helper.ActivityList;
import id.co.cp.mdc.helper.SQLiteHandler;

public class MonitoringActivity3 extends AppCompatActivity {
    SQLiteHandler db;
    GridView gridView;
    private Toolbar toolbar;
    private ProgressDialog pDialog;
    private ActivityAdapter activityAdapter;
    List<ActivityList> activityList =  new ArrayList<ActivityList>();
    public static String zfmid, zfmnm;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.grid_act);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConfig.Zfmid+" - "+AppConfig.Zfmnm);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(MonitoringActivity3.this);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);

        gridView = (GridView) findViewById(R.id.gridview1);
        db = new SQLiteHandler(this);

        activityAdapter = new ActivityAdapter(this, activityList);

        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            showpDialog();
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("zfmid", AppConfig.Zfmid);
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
//            hidepDialog();
            e.printStackTrace();
            hidepDialog();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlMonitoring3, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hidepDialog();
//                        Log.e("tes_keluar", "testing");
//                        Log.e("hasil_farm_list", response.toString());
                        try {
                            JSONArray jarr = response.getJSONArray("data");
//                            Toast.makeText(MonitoringActivity3.this,jarr.toString(),Toast.LENGTH_LONG).show();
                            Log.e("isi_json_layer3",jarr.toString());
                            for (int i=0;i<jarr.length();i++){
                                JSONObject jobj = jarr.getJSONObject(i);
                                ActivityList actList = new ActivityList();
                                int num = i+1;
                                actList.setRecdt(jobj.getString("recdt").substring(8,10)+"-"+jobj.getString("recdt").substring(5,7)+"("+num+")");
                                actList.setDocin("doc = "+jobj.getString("docin"));
                                actList.setAvgbw("avgbw = "+jobj.getString("avgbw")+"gr");
                                actList.setMorta("mort = "+jobj.getString("morta")+"ek");
                                actList.setFeedc("feedc = "+jobj.getString("feedc"));
                                actList.setFnlbw("F abw = "+jobj.getString("fnlbw")+"gr");
                                actList.setFnlqt("sppa = "+jobj.getString("fnlqt")+"ek");
                                actList.setValid(jobj.getString("valid"));
                                activityList.add(actList);
                            }
                            hidepDialog();
                        }
                        catch (JSONException e) {
                            hidepDialog();
                            e.printStackTrace();
                            Toast.makeText(MonitoringActivity3.this,"Tidak ada aktivitas yang aktif pada farm tersebut.",Toast.LENGTH_LONG).show();
                            finish();
                        }
                        activityAdapter.notifyDataSetChanged();
                        gridView.setAdapter(activityAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("error_monitor2",error.toString());
                        hidepDialog();
                        Toast.makeText(MonitoringActivity3.this,"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsObjRequest);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


//                Toast.makeText(getApplicationContext(),
//                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();


            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item){
//        Intent myIntent = new Intent(MonitoringActivity2.this, MonitoringActivity.class);
//        startActivityForResult(myIntent, 0);
        finish();
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
}