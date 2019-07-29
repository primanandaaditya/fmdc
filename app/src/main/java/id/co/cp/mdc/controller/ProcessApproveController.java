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
import id.co.cp.mdc.interfaces.IProcessApprove;
import id.co.cp.mdc.model.approve.P4Model;
import id.co.cp.mdc.model.processapprove.ProcessApproveModel;

public class ProcessApproveController {

    IProcessApprove iProcessApprove;
    Context context;
    ProgressDialog progressDialog;
    SQLiteHandler db;
    String batch;

    public ProcessApproveController(Context context, String batch, IProcessApprove iProcessApprove){
        this.context=context;
        this.iProcessApprove=iProcessApprove;
        this.batch=batch;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void doProcess(){
        db=new SQLiteHandler(context);
        HashMap<String, String> userDetail = db.getUserDetails();

        JSONObject JObj = new JSONObject();
        try {
            JObj.put("fcrud", "fmdc_list");
            JObj.put("zfunc", "upda_approve");
            JObj.put("token", userDetail.get("pass"));
            JObj.put("zuser", userDetail.get("email"));
            JObj.put("clien", AppConfig.clien);
            JObj.put("batch", batch);
            JObj.put("status", "Y");
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
                        ProcessApproveModel processApproveModel=gson.fromJson(response.toString(),ProcessApproveModel.class);
                        iProcessApprove.onProcessSuccess(processApproveModel);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       iProcessApprove.onProcessError(error.getMessage());
                    }
                });
        JReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(JReq);
    }
}
