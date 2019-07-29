package id.co.cp.mdc.helper;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 29/11/2016.
 */
public class FillSpinner {
    private SQLiteHandler db;
    public FillSpinner() {
    }
    public void FillSpinFarm(Spinner spin, Activity activity){
        List<String> categoriesFarm = new ArrayList<String>();
        db = new SQLiteHandler(activity.getApplicationContext());
        List<Farmer> Farmers = db.getAllFarmer();
        for (Farmer fm : Farmers) {
            String farmer = fm.getFarmCode()+" - "+fm.getFarmName();
            categoriesFarm.add(farmer);
        }
        ArrayAdapter<String> dataAdapterFarm = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, categoriesFarm);
        dataAdapterFarm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapterFarm);

    }
    public void FillSpinTs(Spinner spin, Activity activity){
        List<String> listTs = new ArrayList<>();
        db = new SQLiteHandler(activity.getApplicationContext());
        JSONArray jarr = db.getAllTs();
        for (int i=0;i<jarr.length();i++){
            try {
                JSONObject jobj = jarr.getJSONObject(i);
                listTs.add(jobj.getString("area_id")+jobj.getString("branch_id")+jobj.getString("ts_id")+" - "+jobj.getString("ts"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> dataAdapterTs = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listTs);
        dataAdapterTs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapterTs);
    }
    public void FillSpinFarmByTs(Spinner spin, String TsId, Activity activity){
        List<String> listFarm = new ArrayList<>();
        db = new SQLiteHandler(activity.getApplicationContext());
        JSONArray jarr = db.getFarmByTs(TsId);
        for (int i=0;i<jarr.length();i++){
            try {
                JSONObject jobj = jarr.getJSONObject(i);
                listFarm.add(jobj.getString("area_id")+jobj.getString("branch_id")+jobj.getString("farmer_id")+" - "+jobj.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> dataAdapterTs = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listFarm);
        dataAdapterTs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapterTs);
    }
}
