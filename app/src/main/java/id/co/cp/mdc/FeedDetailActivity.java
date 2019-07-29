package id.co.cp.mdc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
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
import id.co.cp.mdc.helper.FeedDtlAdapter;
import id.co.cp.mdc.helper.FeedDtlList;
import id.co.cp.mdc.helper.SQLiteHandler;

public class FeedDetailActivity extends AppCompatActivity {
    private FeedDtlAdapter adapter;
    private ListView listView;
    private List<FeedDtlList> FeedDtlList = new ArrayList<FeedDtlList>();
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    TextView trtyp, trxcd, shkzg, zuser, teksrecdt, recdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConfig.Zfmid+" - "+AppConfig.Zfmnm);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trtyp = (TextView) findViewById(R.id.trtyp);
        trxcd = (TextView) findViewById(R.id.trxcd);
        zuser = (TextView) findViewById(R.id.zuser);
        recdt = (TextView) findViewById(R.id.recdt);

        trxcd.setText(AppConfig.Trxcd);

        pDialog = new ProgressDialog(FeedDetailActivity.this);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.listFeedDetail);
        adapter = new FeedDtlAdapter(FeedDetailActivity.this, FeedDtlList);
        listView.setAdapter(adapter);

        getFeedDtl();
    }

    private void getFeedDtl(){
        pDialog.show();

        db = new SQLiteHandler(FeedDetailActivity.this);
        HashMap<String, String> user = db.getUserDetails();

        JSONObject JObj = new JSONObject();
        try {
            pDialog.show();
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("zfmid", AppConfig.Zfmid);
            JObj.put("clien", AppConfig.clien);
            JObj.put("trxcd", AppConfig.Trxcd);
        }
        catch (JSONException e) {
            e.printStackTrace();
            pDialog.hide();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlTrxFeedDetail, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jarr = response.getJSONArray("data");
                            for (int i=0;i<jarr.length();i++){
                                JSONObject jobj = jarr.getJSONObject(i);
                                trtyp.setText(jobj.getString("trtyp"));
                                zuser.setText(jobj.getString("zuser"));
                                recdt.setText(jobj.getString("recdt"));
                                FeedDtlList feedDtlList = new FeedDtlList();
                                feedDtlList.setfedcd(jobj.getString("fedcd"));
                                feedDtlList.setquant(jobj.getString("quant"));
                                if(jobj.getString("shkzg").equals("h")){
                                    feedDtlList.setshkzg("- ");
                                }else if(jobj.getString("shkzg").equals("s")){
                                    feedDtlList.setshkzg("+ ");
                                }
                                FeedDtlList.add(feedDtlList);
                            }
                            pDialog.hide();
                        }
                        catch (JSONException e) {
                            pDialog.hide();
                            e.printStackTrace();
                            Toast.makeText(FeedDetailActivity.this,"Tidak ada aktivitas yang aktif pada farm tersebut.",Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(FeedDetailActivity.this,"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
                    }
                });

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
}
