package id.co.cp.mdc.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

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
import java.util.List;

import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.interfaces.ListFarmInterface;
import id.co.cp.mdc.model.ListFarmModel;
import id.co.cp.mdc.model.ListFarmModel.Datum;

public class ListFarmViewModel extends ViewModel {

    Datum datum;
    private MutableLiveData<List<Datum>> datums;


    private SQLiteHandler db;
    Context context;
    String tag_json_obj = "json_obj_req";
    String url = AppConfig.urlMyTag;

    String name,email,token;

    public LiveData<List<Datum>> allDatum() {
        if (datums == null) {
            datums = new MutableLiveData<List<Datum>>();
            loadDatum();
        }
        return datums;
    }

    public void loadDatum(){


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
                        datums.setValue(listFarmModel.getData());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        datums.setValue(null);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsObjRequest);
    }

}
