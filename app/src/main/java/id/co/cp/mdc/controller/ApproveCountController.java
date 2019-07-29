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
import id.co.cp.mdc.interfaces.IApproveCount;
import id.co.cp.mdc.interfaces.IUserList;
import id.co.cp.mdc.model.UserListModel;
import id.co.cp.mdc.model.approvecount.ApproveCountModel;

public class ApproveCountController {

    IApproveCount iApproveCount;
    Context context;
    SQLiteHandler db;
    ProgressDialog progressDialog;

    public ApproveCountController(Context context, IApproveCount iApproveCount){
        this.context = context;
        this.iApproveCount = iApproveCount;
        db=new SQLiteHandler(context);

    }

    public void getData(){
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Getting data...");
        progressDialog.show();

        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            JObj.put("fcrud", "fmdc_list");
            JObj.put("zfunc", "list_ts");
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest JReq = new JsonObjectRequest(Request.Method.POST, AppConfig.url_api_fmdc, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson=new Gson();
                        ApproveCountModel approveCountModel=gson.fromJson(response.toString(), ApproveCountModel.class);
                        iApproveCount.onApproveCountSuccess(approveCountModel);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iApproveCount.onApproveCountError(error.getMessage());
                        progressDialog.dismiss();
                    }
                });
        JReq.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(JReq);
    }


}
