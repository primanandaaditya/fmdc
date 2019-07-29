package id.co.cp.mdc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.helper.FarmerList;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.helper.m2Adapter;
import id.co.cp.mdc.helper.m2List;

public class MonitoringActivity2 extends AppCompatActivity {
    private m2Adapter adapter;
    private ListView listView;
    private List<FarmerList> FarmerList =  new ArrayList<FarmerList>();
    private List<m2List> m2List =  new ArrayList<m2List>();
    private Toolbar toolbar;
    private SQLiteHandler db;
    private ProgressDialog pDialog;

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(MonitoringActivity2.this,
//                MonitoringActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pDialog = new ProgressDialog(MonitoringActivity2.this);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        db = new SQLiteHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConfig.SelectedTS+" - "+AppConfig.Ztsnm);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m2List.clear();
                inputListView();
                refresh.setRefreshing(false);
            }
        });

        listView = (ListView) findViewById(R.id.m2List);
        adapter = new m2Adapter(this, m2List);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppConfig.Zfmid = m2List.get(i).getZfmid();
                AppConfig.Zfmnm = m2List.get(i).getZfmnm();
                Intent intent = new Intent(MonitoringActivity2.this, MonitoringTab.class);
                startActivity(intent);
            }
        });

        inputListView();
    }

    private void inputListView(){
        showpDialog();
        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("ztsid", AppConfig.SelectedTS);
            JObj.put("ztype","list_farm");
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
            hidepDialog();
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlMonitoring2, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidepDialog();
//                        Toast.makeText(MonitoringActivity2.this,response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            String stat = response.getString("stat");
                            if (stat.equals("0")) {
                                JSONArray jarr = response.getJSONArray("data");
                                for (int i = 0; i < jarr.length(); i++) {
                                    JSONObject jobj = jarr.getJSONObject(i);
                                    m2List m2 = new m2List();
                                    m2.setRunnm(String.valueOf(i + 1));
                                    m2.setZfmid(jobj.getString("mapcd"));
                                    m2.setZfmnm(jobj.getString("NAME"));
                                    m2.setTag(jobj.getString("tag"));

                                    if (jobj.getString("stok").equals("null")) {
                                        m2.setLsstk("-");
                                    } else {
                                        m2.setLsstk(jobj.getString("stok"));
                                    }
                                    if (jobj.getString("docin").equals("null")) {
                                        m2.setDocin("-");
                                    } else {
                                        m2.setDocin(jobj.getString("docin"));
                                    }
                                    if (jobj.getString("lavbw").equals("null")) {
                                        m2.setLsabw("-");
                                    } else {
                                        m2.setLsabw(jobj.getString("lavbw"));
                                    }
                                    if (jobj.getString("fcr").equals("null")) {
                                        m2.setFcr("-");
                                    } else {
                                        m2.setFcr(jobj.getString("fcr"));
                                    }

                                    if (jobj.getString("FEEDIs00").equals("null")) {
                                        m2.setFi1("-");
                                    } else {
                                        m2.setFi1(jobj.getString("FEEDIs00"));
                                    }
                                    if (jobj.getString("FEDUSEs00").equals("null")) {
                                        m2.setFu1("-");
                                    } else {
                                        m2.setFu1(jobj.getString("FEDUSEs00"));
                                    }
                                    if (jobj.getString("FEDINs00").equals("null")) {
                                        m2.setTi1("-");
                                    } else {
                                        m2.setTi1(jobj.getString("FEDINs00"));
                                    }
                                    if (jobj.getString("FEDOUTs00").equals("null")) {
                                        m2.setTo1("-");
                                    } else {
                                        m2.setTo1(jobj.getString("FEDOUTs00"));
                                    }
                                    if (jobj.getString("TOTs00").equals("null")) {
                                        m2.setSaldo1("-");
                                    } else {
                                        m2.setSaldo1(jobj.getString("TOTs00"));
                                    }

                                    if (jobj.getString("FEEDIs10").equals("null")) {
                                        m2.setFi2("-");
                                    } else {
                                        m2.setFi2(jobj.getString("FEEDIs10"));
                                    }
                                    if (jobj.getString("FEDUSEs10").equals("null")) {
                                        m2.setFu2("-");
                                    } else {
                                        m2.setFu2(jobj.getString("FEDUSEs10"));
                                    }
                                    if (jobj.getString("FEDINs10").equals("null")) {
                                        m2.setTi2("-");
                                    } else {
                                        m2.setTi2(jobj.getString("FEDINs10"));
                                    }
                                    if (jobj.getString("FEDOUTs10").equals("null")) {
                                        m2.setTo2("-");
                                    } else {
                                        m2.setTo2(jobj.getString("FEDOUTs10"));
                                    }
                                    if (jobj.getString("TOTs10").equals("null")) {
                                        m2.setSaldo2("-");
                                    } else {
                                        m2.setSaldo2(jobj.getString("TOTs10"));
                                    }

                                    if (jobj.getString("FEEDIs11").equals("null")) {
                                        m2.setFi3("-");
                                    } else {
                                        m2.setFi3(jobj.getString("FEEDIs11"));
                                    }
                                    if (jobj.getString("FEDUSEs11").equals("null")) {
                                        m2.setFu3("-");
                                    } else {
                                        m2.setFu3(jobj.getString("FEDUSEs11"));
                                    }
                                    if (jobj.getString("FEDINs11").equals("null")) {
                                        m2.setTi3("-");
                                    } else {
                                        m2.setTi3(jobj.getString("FEDINs11"));
                                    }
                                    if (jobj.getString("FEDOUTs11").equals("null")) {
                                        m2.setTo3("-");
                                    } else {
                                        m2.setTo3(jobj.getString("FEDOUTs11"));
                                    }
                                    if (jobj.getString("TOTs11").equals("null")) {
                                        m2.setSaldo3("-");
                                    } else {
                                        m2.setSaldo3(jobj.getString("TOTs11"));
                                    }

                                    if (jobj.getString("FEEDIs12").equals("null")) {
                                        m2.setFi4("-");
                                    } else {
                                        m2.setFi4(jobj.getString("FEEDIs12"));
                                    }
                                    if (jobj.getString("FEDUSEs12").equals("null")) {
                                        m2.setFu4("-");
                                    } else {
                                        m2.setFu4(jobj.getString("FEDUSEs12"));
                                    }
                                    if (jobj.getString("FEDINs12").equals("null")) {
                                        m2.setTi4("-");
                                    } else {
                                        m2.setTi4(jobj.getString("FEDINs12"));
                                    }
                                    if (jobj.getString("FEDOUTs12").equals("null")) {
                                        m2.setTo4("-");
                                    } else {
                                        m2.setTo4(jobj.getString("FEDOUTs12"));
                                    }
                                    if (jobj.getString("TOTs12").equals("null")) {
                                        m2.setSaldo4("-");
                                    } else {
                                        m2.setSaldo4(jobj.getString("TOTs12"));
                                    }

                                    m2List.add(m2);
                                }
                            }else if(stat.equals("2")){
                                Toast.makeText(MonitoringActivity2.this,"Session Time Out.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MonitoringActivity2.this, LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MonitoringActivity2.this,"server sedang dalam perbaikan.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MonitoringActivity2.this, MainActivity.class);
                                startActivity(intent);
                            }
                            } catch(JSONException e){
                            Toast.makeText(MonitoringActivity2.this,"tidak ada otorisasi, Silahkan hubungi tim FMDC",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        Toast.makeText(MonitoringActivity2.this,"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsObjRequest);

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }
    public boolean onOptionsItemSelected(MenuItem item){
//        Intent myIntent = new Intent(MonitoringActivity2.this, MonitoringActivity.class);
//        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    private void get_farmlist(String ztsid){
        showpDialog();
        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("ztsid", ztsid);
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
            hidepDialog();
            e.printStackTrace();
//            hidepDialog();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlMonitoring2, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidepDialog();
                        try {
                            JSONArray jarr = response.getJSONArray("data");
                            for(int i =0;i<jarr.length();i++) {
                                JSONObject jobj = jarr.getJSONObject(i);
                                FarmerList farmerList = new FarmerList();
                                farmerList.setZfmid(jobj.getString("mapcd"));
                                farmerList.setZfmnm(jobj.getString("name"));
                                if(!jobj.getString("zdate").equals("null")){
                                    farmerList.setZdate(jobj.getString("zdate"));
//                                    farmerList.setZtime(jobj.getString("ztime"));
                                }
                                else{
                                    farmerList.setZdate("");
//                                    farmerList.setZtime("");
                                }
                                farmerList.setLsstk("("+jobj.getString("stock")+"/"+jobj.getString("docin")+") ekor");
                                farmerList.setLsabw(jobj.getString("lavbw")+" Kg");
                                if(jobj.getString("lavbwp").equals("0")){
                                    farmerList.setFcr("FCR : "+"0.000");
                                }
                                else{
                                    float feedcp = Float.parseFloat(jobj.getString("feedcp"));
                                    float docinp = Float.parseFloat(jobj.getString("docinp"));
                                    float mortap = Float.parseFloat(jobj.getString("mortap"));
                                    float lavbwp = Float.parseFloat(jobj.getString("lavbwp"));
                                    float fcr = feedcp*50/((docinp-mortap)*lavbwp);
                                    DecimalFormat df2 = new DecimalFormat( "0.000" );
                                    farmerList.setFcr("FCR : "+df2.format(fcr));
                                }
                                farmerList.setRunnm(String.valueOf(i+1));
                                if(!jobj.getString("latit").equals("null")){
                                    farmerList.setTag("[tagged]");
                                }
                                else{
                                    farmerList.setTag("[not tagged]");
                                }
                                FarmerList.add(farmerList);
                            }
                        } catch (JSONException e) {
                            hidepDialog();
                            e.printStackTrace();
                            Toast.makeText(MonitoringActivity2.this,"TS tidak memiliki farm. Mohon perbaiki data master di PIMS/Theos",Toast.LENGTH_LONG).show();
                            finish();
                        }
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("error_monitor2",error.toString());
                        Toast.makeText(MonitoringActivity2.this,"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsObjRequest);

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsObjRequest);

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
