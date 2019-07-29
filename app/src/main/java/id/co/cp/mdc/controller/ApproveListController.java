package id.co.cp.mdc.controller;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.interfaces.IApprove;
import id.co.cp.mdc.interfaces.IUserList;
import id.co.cp.mdc.model.approve.P4Model;
import id.co.cp.mdc.model.approve.Sysrc;
import id.co.cp.mdc.model.approve.ZList;

public class ApproveListController {

    IApprove iApprove;
    Context context;
    ProgressDialog progressDialog;
    String id;

    public ApproveListController(Context context, String id, IApprove iApprove ){
        this.context=context;
        this.iApprove=iApprove;
        this.id=id;
    }

    public void getApproveList(){

        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Getting approval list...");
        progressDialog.show();


        JSONObject JObj = new JSONObject();
        try {

            JObj.put("fcrud", "fmdc_list");
            JObj.put("zfunc", "list_approve");
            JObj.put("ztsid", id);
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
                        P4Model p4Model = gson.fromJson(response.toString(), P4Model.class);
                        iApprove.onApproveSuccess(p4Model);
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iApprove.onApproveError(error.getMessage());
                        progressDialog.dismiss();

                    }
                });
        JReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(JReq);

    }
}
