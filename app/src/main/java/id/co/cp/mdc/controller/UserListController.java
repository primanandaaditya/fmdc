package id.co.cp.mdc.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.MainActivity;
import id.co.cp.mdc.MonitoringActivity;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.model.UserListModel.UserList;
import id.co.cp.mdc.interfaces.IUserList;
import id.co.cp.mdc.model.UserListModel;


public class UserListController {

    IUserList iUserList;
    Context context;
    SQLiteHandler db;
    ProgressDialog progressDialog;



    public UserListController(Context context, IUserList iUserList){
        this.context=context;
        this.iUserList=iUserList;
        db=new SQLiteHandler(context);

    }

    public void getUserList(){

        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Getting user list...");
        progressDialog.show();

        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("token", user.get("pass"));
            //JObj.put("ztype", "monitor");
            JObj.put("zuser", user.get("email"));
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
            e.printStackTrace();
            //progressDialog.dismiss();
        }

        JsonObjectRequest JReq = new JsonObjectRequest(Request.Method.POST, AppConfig.url_api_fmdc, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson=new Gson();
                        UserList userList = gson.fromJson(response.toString(), UserList.class);
                        iUserList.onUserListSuccess(userList);
                        progressDialog.dismiss();
//                        try {
//                            String stat = response.getString("status");
////                            Toast.makeText(MonitoringActivity.this,response.toString(),Toast.LENGTH_LONG).show();
//                            //hidepDialog();
//                            if(stat.equals("0")) {
////
////                                JSONArray data = response.getJSONArray("data");
////                                for (int i = 0; i < data.length(); i++) {
////                                    JSONObject json = data.getJSONObject(i);
////                                    UserList userList = new UserList();
////                                    userList.setProfile(AppConfig.server_ip+json.getString("url"));
////                                    userList.setUsrname(json.getString("zuser"));
////                                    userList.setTag(json.getString("mytag")+"/"+json.getString("remain"));
////                                    userList.setTime(json.getString("ltdte")+" : "+json.getString("lttme"));
////                                    userList.setRunnm(String.valueOf(i+1));
////                                    userList.setZtsid(json.getString("ztsid"));
////                                    MonitorList.add(userList);
////                                }
//                            }
//                            else if(stat.equals("2")){
//                                Toast.makeText(MonitoringActivity.this,"Session Time Out.",Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(MonitoringActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                            }
//                            else{
//                                Toast.makeText(MonitoringActivity.this,"server sedang dalam perbaikan.",Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(MonitoringActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//                        catch (JSONException e) {
////                            Toast.makeText(MonitoringActivity.this,"tidak ada otorisasi, Silahkan hubungi tim FMDC",Toast.LENGTH_LONG).show();
//                            Toast.makeText(MonitoringActivity.this,e.toString(),Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//                        adapter.notifyDataSetChanged();
//                        listView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iUserList.onUserListError(error.getMessage());
                        progressDialog.dismiss();
//                        Toast.makeText(MonitoringActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                        Toast.makeText(MonitoringActivity.this,"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
//                        hidepDialog();
//                        Intent intent = new Intent(MonitoringActivity.this, MainActivity.class);
//                        startActivity(intent);
                    }
                });
        JReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(JReq);

    }

}
