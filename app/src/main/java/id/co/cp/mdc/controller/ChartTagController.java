package id.co.cp.mdc.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.interfaces.ChartTagInterface;
import id.co.cp.mdc.interfaces.ListFarmInterface;
import id.co.cp.mdc.model.ChartModel;
import id.co.cp.mdc.model.ChartModel.Chart;
import id.co.cp.mdc.model.ChartModel.Datum;

public class ChartTagController {


    Context context;
    private SQLiteHandler db;
    ChartTagInterface chartTagInterface;
    String tag_json_obj = "json_obj_req";
    String url = AppConfig.urlMyTag;

    String name,email,token;

    public ChartTagController(Context context, ChartTagInterface chartTagInterface){
        this.context=context;
        this.chartTagInterface=chartTagInterface;
    }

    public void getChart(){

        final ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        db=new SQLiteHandler(context);
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        email = user.get("email");
        token = user.get("pass");


        JSONObject JList = new JSONObject();
        try {
            JList.put("zuser", email);
            JList.put("clien", AppConfig.clien);
            JList.put("token", token);
            JList.put("mapty", AppConfig.mapty);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppConfig.urlMyTagStatus, JList, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                            Gson gson=new Gson();
                            Chart chart= gson.fromJson(response.toString(), Chart.class);
                            chartTagInterface.onChartSuccess(chart);

                            if (chart.getStatus()==0) {
                                //Datum datum = chart.getData().get(0);


                            } else {//kalau session time out
                                Toast.makeText(context, "Session time out. Harap Login kembali", Toast.LENGTH_LONG).show();
                                db.deleteTabel();
                                Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }

                        //              hidepDialog();
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        // TODO Auto-generated method stub
                        Log.e("error tag status", error.toString());
                        chartTagInterface.onChartError(error.getMessage());
                        //Toast.makeText(context, "Tidak terhubung dengan server. Harap mencari signal 0.1", Toast.LENGTH_LONG).show();
                        //  Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsObjRequest);

    }

}
