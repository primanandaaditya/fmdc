package id.co.cp.mdc.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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

import id.co.cp.mdc.R;
import id.co.cp.mdc.adapter.ListFarmAdapter;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.interfaces.ListFarmInterface;
import id.co.cp.mdc.model.ListFarmModel;

public class ListFarmController {

    Context context;
    private SQLiteHandler db;
    ListFarmInterface listFarmInterface;
    String tag_json_obj = "json_obj_req";
    String url = AppConfig.urlMyTag;

    String name,email,token;

    public ListFarmController(Context context, ListFarmInterface listFarmInterface){
        this.context=context;
        this.listFarmInterface=listFarmInterface;
    }

    public void getList(){
        final ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        db=new SQLiteHandler(context);
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        email = user.get("email");
        token = user.get("pass");


        final JSONObject JObj = new JSONObject();

        JSONObject JList = new JSONObject();
        try {
            JList.put("zuser",email);
            JList.put("clien", AppConfig.clien);
            JList.put("token",token);
            JList.put("mapty", AppConfig.mapty);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppConfig.urlMyTag, JList, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson =new Gson();
                        ListFarmModel.Model listFarmModel=gson.fromJson(response.toString(), ListFarmModel.Model.class);
                        listFarmInterface.onSuccess(listFarmModel);
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        listFarmInterface.onError(error.getMessage());

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsObjRequest);

    }


}
