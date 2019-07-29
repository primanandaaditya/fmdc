package id.co.cp.mdc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import id.co.cp.mdc.helper.CustomAdapter;
import id.co.cp.mdc.helper.Notif;
import id.co.cp.mdc.helper.SQLiteHandler;

public class NotificationActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private ListView listView;
    private List<Notif> NotifList =  new ArrayList<Notif>();
    private Toolbar toolbar;
    SQLiteHandler db;
    private ProgressDialog pDialog;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotificationActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pDialog = new ProgressDialog(NotificationActivity.this);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        db = new SQLiteHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.NotifList);
        adapter = new CustomAdapter(this, NotifList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppConfig.SelectedNotif.setLgmsg(NotifList.get(i).getLgmsg());
                AppConfig.SelectedNotif.setZtime(NotifList.get(i).getZtime());
                AppConfig.SelectedNotif.setZdate(NotifList.get(i).getZdate());
                AppConfig.SelectedNotif.setUsrfr(NotifList.get(i).getUsrfr());
                AppConfig.SelectedNotif.setNotcd(NotifList.get(i).getNotcd());
                AppConfig.SelectedNotif.setShmsg(NotifList.get(i).getShmsg());
                Intent intent = new Intent(NotificationActivity.this, NotifDetail.class);
                startActivity(intent);

            }
        });
    get_notif();
//        for(int i=2;i<12;i++){
//            Notif notif= new Notif();
//            notif.setLgmsg("panjang");
//            notif.setNotcd(String.valueOf(i));
//            notif.setShmsg("pendek");
//            if (i%2==0) {
//                notif.setShmsg("pendek ini adalah pesan pendek pendek lorem ipsum tarara koventum");
//            }
//            notif.setUsrfr("admin");
//            notif.setZdate("23-23-23");
//            notif.setZtime("09:24");
//            NotifList.add(notif);
//        }
//        adapter.notifyDataSetChanged();

    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(NotificationActivity.this, MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

    private void get_notif(){
        showpDialog();
        HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("token", user.get("pass"));
            JObj.put("type", "notif");
            JObj.put("tocod", user.get("email"));
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
            e.printStackTrace();
            hidepDialog();
        }
        JsonObjectRequest JReq = new JsonObjectRequest(Request.Method.POST, AppConfig.urlJson, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("stat");
                            hidepDialog();
                            if(stat.equals("0")) {
                                JSONArray data = response.getJSONArray("data");
//                                Log.e("isi_notif",data.toString());

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject json = data.getJSONObject(i);
                                    Notif notif = new Notif();
                                    notif.setLgmsg(json.getString("lgmsg"));
                                    notif.setNotcd(json.getString("notcd"));
                                    notif.setShmsg(json.getString("shmsg"));
                                    notif.setZdate(json.getString("zdate"));
                                    notif.setZtime(json.getString("ztime"));
                                    NotifList.add(notif);
                                }
                            }
                            else if(stat.equals("2")){
                                Toast.makeText(NotificationActivity.this,"Session Time Out.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(NotificationActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(NotificationActivity.this,"server sedang dalam perbaikan.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        catch (JSONException e) {
                            Toast.makeText(NotificationActivity.this,"Belum ada notifikasi pada saat ini",Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificationActivity.this,"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
                        hidepDialog();
                        Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

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
