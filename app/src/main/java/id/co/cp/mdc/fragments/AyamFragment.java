package id.co.cp.mdc.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

import id.co.cp.mdc.LhkActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.helper.ActivityAdapter;
import id.co.cp.mdc.helper.ActivityList;
import id.co.cp.mdc.helper.SQLiteHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class AyamFragment extends Fragment {

    List<ActivityList> activityList =  new ArrayList<ActivityList>();
    int b;
    int c = 0;
    Integer num = 0;
    Integer countermerah = 0;
    public String nama;
    Integer age;
    private SwipeRefreshLayout refresh;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private ActivityAdapter activityAdapter;

    public AyamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ayam, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getActivity());

        nama = AppConfig.Zfmid+" - "+AppConfig.Zfmnm;

        final GridView gridView = (GridView) rootView.findViewById(R.id.gridview1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String morta = activityList.get(position).getMorta();
                String avgbw = activityList.get(position).getAvgbw();
                String feedc = activityList.get(position).getFeedc();
                Integer urutan = Integer.parseInt(activityList.get(position).getRecdt().substring(11,activityList.get(position).getRecdt().length()));
                String tgl = activityList.get(position).getRecdt().substring(0,10);
                if(morta.equals("merah")||morta.equals("")||feedc.equals("")||feedc.equals("merah")||urutan==7&&avgbw.equals("merah")||urutan==14&&avgbw.equals("merah")||urutan==21&&avgbw.equals("merah")||urutan>=25&&avgbw.equals("merah")){
                    Intent i = new Intent(getActivity(), LhkActivity.class);
                    i.putExtra("intent", "2");
                    i.putExtra("nama", nama);
                    i.putExtra("tgl", tgl);
                    i.putExtra("close","y");
                    startActivity(i);
                }
//                Toast.makeText(getActivity(), id+" "+morta+" "+avgbw+" "+feedc+" "+recdt, Toast.LENGTH_SHORT).show();
            }
        });

        activityAdapter = new ActivityAdapter(getActivity(), activityList);
        gridView.setAdapter(activityAdapter);

        loadData();

        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                num=0;
                activityList.clear();
                activityAdapter.notifyDataSetChanged();
                loadData();
                refresh.setRefreshing(false);
            }
        });
        return rootView;
    }
    private void loadData(){
        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            pDialog.show();
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("zfmid", AppConfig.Zfmid);
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
//            hidepDialog();
            e.printStackTrace();
            pDialog.hide();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlMonitoring3, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            countermerah=0;
                            JSONArray jarr = response.getJSONArray("data");
//                            Toast.makeText(getActivity(),jarr.toString(),Toast.LENGTH_LONG).show();
//                            Log.e("isi_json_layer3",jarr.toString());
                            for (int i=0;i<jarr.length();i++){
                                JSONObject jobj = jarr.getJSONObject(i);
                                ActivityList actList = new ActivityList();
                                //cek counter apabila ada field yg tidak diisi pada 1 hari
                                if(countermerah>0){
                                    actList.setValid("2");
                                    actList.setDocin(jobj.getString("docin"));
                                    actList.setRecdt(jobj.getString("recdt")+" "+String.valueOf(i+1));
                                    actList.setFnlbw(jobj.getString("fnlbw"));
                                    actList.setFnlqt(jobj.getString("fnlqt"));
                                    actList.setReasp(jobj.getString("reasp"));
                                    actList.setAge(jobj.getString("age"));

                                    //untuk memulai hitungan hari setelah doc in
                                    if(jobj.getString("docin").equals("")||jobj.getString("docin").equals("0")){
                                        c+=0;
                                    }else{
                                        c++;
                                    }
                                    if(c==0){
                                        num = 0;
                                    }else{
                                        num++;
                                    }
                                    //memasukkan umur
                                    if(jobj.getString("age").equals("")){
                                    }else{
                                        age = Integer.parseInt(jobj.getString("age"));
                                    }
                                    //avgbw tidak boleh kosong (berlaku untuk hari ke 7,14,21,25++)
                                    if(jobj.getString("avgbw")==null&&num==7||
                                            jobj.getString("avgbw").equals("null")&&num==7||
                                            jobj.getString("avgbw").equals("")&&num==7||
                                            jobj.getString("avgbw")==null&&num==14||
                                            jobj.getString("avgbw").equals("null")&&num==14||
                                            jobj.getString("avgbw").equals("")&&num==14||
                                            jobj.getString("avgbw")==null&&num==21||
                                            jobj.getString("avgbw").equals("null")&&num==21||
                                            jobj.getString("avgbw").equals("")&&num==21||
                                            jobj.getString("avgbw")==null&&num>=25||
                                            jobj.getString("avgbw").equals("null")&&num>=25||
                                            jobj.getString("avgbw").equals("")&&num>=25){
                                        actList.setAvgbw("merah");
                                        countermerah++;
                                    }else{
                                        actList.setAvgbw(jobj.getString("avgbw"));
                                    }
                                    //morta tidak boleh kosong
                                    if(jobj.getString("morta")==null||jobj.getString("morta").equals("null")||jobj.getString("morta").equals("")){
                                        actList.setMorta("merah");
                                        countermerah++;
                                    }else{
                                        actList.setMorta(jobj.getString("morta"));
                                    }
                                    //feedc tidak boleh kosong
                                    if(jobj.getString("feedc")==null||jobj.getString("feedc").equals("null")||jobj.getString("feedc").equals("")){
                                        actList.setFeedc("merah");
                                        countermerah++;
                                    }else{
                                        actList.setFeedc(jobj.getString("feedc"));
                                    }
                                }else if(countermerah==0){
                                    actList.setDocin(jobj.getString("docin"));
                                    actList.setRecdt(jobj.getString("recdt")+" "+String.valueOf(i+1));
                                    actList.setFnlbw(jobj.getString("fnlbw"));
                                    actList.setFnlqt(jobj.getString("fnlqt"));
                                    actList.setReasp(jobj.getString("reasp"));
                                    actList.setAge(jobj.getString("age"));

                                    //cek apakah ada doc ditengah2 cycle
                                    if(jobj.getString("valid").equals("")){
                                        b=0;
                                        for(int j=i;j<jarr.length()-i;j++){
                                            JSONObject jobj2 = jarr.getJSONObject(j);
                                            if(jobj2.getString("docin").equals("0")||jobj2.getString("docin").equals("")){
                                                b+=0;
                                            }else{
                                                b++;
                                            }
                                        }
                                        if(b==0){
                                            actList.setValid("");
                                        }else{
                                            actList.setValid("2");
                                        }
                                    }else{
                                        actList.setValid(jobj.getString("valid"));
                                    }
                                    //untuk memulai hitungan hari setelah doc in
                                    if(jobj.getString("docin").equals("")||jobj.getString("docin").equals("0")){
                                        c+=0;
                                    }else{
                                        c++;
                                    }
                                    if(c==0){
                                        num = 0;
                                    }else{
                                        num++;
                                    }
                                    //memasukkan umur
                                    if(jobj.getString("age").equals("")){
                                    }else{
                                        age = Integer.parseInt(jobj.getString("age"));
                                    }
                                    //avgbw tidak boleh kosong (berlaku untuk hari ke 7,14,21,25++)
                                    if(jobj.getString("avgbw")==null&&num==7||
                                            jobj.getString("avgbw").equals("null")&&num==7||
                                            jobj.getString("avgbw").equals("")&&num==7||
                                            jobj.getString("avgbw")==null&&num==14||
                                            jobj.getString("avgbw").equals("null")&&num==14||
                                            jobj.getString("avgbw").equals("")&&num==14||
                                            jobj.getString("avgbw")==null&&num==21||
                                            jobj.getString("avgbw").equals("null")&&num==21||
                                            jobj.getString("avgbw").equals("")&&num==21||
                                            jobj.getString("avgbw")==null&&num>=25||
                                            jobj.getString("avgbw").equals("null")&&num>=25||
                                            jobj.getString("avgbw").equals("")&&num>=25){
                                        actList.setAvgbw("merah");
                                        actList.setValid("2");
                                        countermerah++;
                                    }else{
                                        actList.setAvgbw(jobj.getString("avgbw"));
                                    }
                                    //morta tidak boleh kosong
                                    if(jobj.getString("morta")==null||jobj.getString("morta").equals("null")||jobj.getString("morta").equals("")){
                                        actList.setMorta("merah");
                                        actList.setValid("2");
                                        countermerah++;
                                    }else{
                                        actList.setMorta(jobj.getString("morta"));
                                    }
                                    //feedc tidak boleh kosong
                                    if(jobj.getString("feedc")==null||jobj.getString("feedc").equals("null")||jobj.getString("feedc").equals("")){
                                        actList.setFeedc("merah");
                                        actList.setValid("2");
                                        countermerah++;
                                    }else{
                                        actList.setFeedc(jobj.getString("feedc"));
                                    }
                                }
                                activityList.add(actList);
                            }
                            pDialog.hide();
                        }
                        catch (JSONException e) {
                            pDialog.hide();
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Tidak ada data",Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        }
                        activityAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("error_monitor2",error.toString());
                        pDialog.hide();
                        Toast.makeText(getActivity(),"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }




    private void loadData_bu(){
        final HashMap<String, String> user = db.getUserDetails();
        JSONObject JObj = new JSONObject();
        try {
            pDialog.show();
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("zfmid", AppConfig.Zfmid);
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
//            hidepDialog();
            e.printStackTrace();
            pDialog.hide();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlMonitoring3, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            countermerah=0;
                            JSONArray jarr = response.getJSONArray("data");
//                            Toast.makeText(getActivity(),jarr.toString(),Toast.LENGTH_LONG).show();
//                            Log.e("isi_json_layer3",jarr.toString());
                            for (int i=0;i<jarr.length();i++){
                                JSONObject jobj = jarr.getJSONObject(i);
                                ActivityList actList = new ActivityList();
                                //cek counter apabila ada field yg tidak diisi pada 1 hari
                                if(countermerah>0){
                                    actList.setValid("2");
                                    actList.setDocin(jobj.getString("docin"));
                                    actList.setRecdt(jobj.getString("recdt")+" "+String.valueOf(num+1));
                                    actList.setFnlbw(jobj.getString("fnlbw"));
                                    actList.setFnlqt(jobj.getString("fnlqt"));
                                    actList.setReasp(jobj.getString("reasp"));
                                    actList.setAge(jobj.getString("age"));

                                    //untuk memulai hitungan hari setelah doc in
                                    if(jobj.getString("docin").equals("")||jobj.getString("docin").equals("0")){
                                        c+=0;
                                    }else{
                                        c++;
                                    }
                                    if(c==0){
                                        num = 0;
                                    }else{
                                        num++;
                                    }
                                    //memasukkan umur
                                    if(jobj.getString("age").equals("")){
                                    }else{
                                        age = Integer.parseInt(jobj.getString("age"));
                                    }
                                    //avgbw tidak boleh kosong (berlaku untuk hari ke 7,14,21,25++)
                                    if(jobj.getString("avgbw")==null&&num==7||
                                        jobj.getString("avgbw").equals("null")&&num==7||
                                        jobj.getString("avgbw").equals("")&&num==7||
                                        jobj.getString("avgbw")==null&&num==14||
                                        jobj.getString("avgbw").equals("null")&&num==14||
                                        jobj.getString("avgbw").equals("")&&num==14||
                                        jobj.getString("avgbw")==null&&num==21||
                                        jobj.getString("avgbw").equals("null")&&num==21||
                                        jobj.getString("avgbw").equals("")&&num==21||
                                        jobj.getString("avgbw")==null&&num>=25||
                                        jobj.getString("avgbw").equals("null")&&num>=25||
                                        jobj.getString("avgbw").equals("")&&num>=25){
                                            actList.setAvgbw("merah");
                                            countermerah++;
                                    }else{
                                            actList.setAvgbw(jobj.getString("avgbw"));
                                    }
                                    //morta tidak boleh kosong
                                    if(jobj.getString("morta")==null||jobj.getString("morta").equals("null")||jobj.getString("morta").equals("")){
                                        actList.setMorta("merah");
                                        countermerah++;
                                    }else{
                                        actList.setMorta(jobj.getString("morta"));
                                    }
                                    //feedc tidak boleh kosong
                                    if(jobj.getString("feedc")==null||jobj.getString("feedc").equals("null")||jobj.getString("feedc").equals("")){
                                        actList.setFeedc("merah");
                                        countermerah++;
                                    }else{
                                        actList.setFeedc(jobj.getString("feedc"));
                                    }
                                }else if(countermerah==0){
                                    actList.setDocin(jobj.getString("docin"));
                                    actList.setRecdt(jobj.getString("recdt")+" "+num.toString());
                                    actList.setFnlbw(jobj.getString("fnlbw"));
                                    actList.setFnlqt(jobj.getString("fnlqt"));
                                    actList.setReasp(jobj.getString("reasp"));
                                    actList.setAge(jobj.getString("age"));

                                    //cek apakah ada doc ditengah2 cycle
                                    if(jobj.getString("valid").equals("")){
                                        b=0;
                                        for(int j=i;j<jarr.length()-i;j++){
                                            JSONObject jobj2 = jarr.getJSONObject(j);
                                            if(jobj2.getString("docin").equals("0")||jobj2.getString("docin").equals("")){
                                                b+=0;
                                            }else{
                                                b++;
                                            }
                                        }
                                        if(b==0){
                                            actList.setValid("");
                                        }else{
                                            actList.setValid("2");
                                        }
                                    }else{
                                        actList.setValid(jobj.getString("valid"));
                                    }
                                    //untuk memulai hitungan hari setelah doc in
                                    if(jobj.getString("docin").equals("")||jobj.getString("docin").equals("0")){
                                        c+=0;
                                    }else{
                                        c++;
                                    }
                                    if(c==0){
                                        num = 0;
                                    }else{
                                        num++;
                                    }
                                    //memasukkan umur
                                    if(jobj.getString("age").equals("")){
                                    }else{
                                        age = Integer.parseInt(jobj.getString("age"));
                                    }
                                    //avgbw tidak boleh kosong (berlaku untuk hari ke 7,14,21,25++)
                                    if(jobj.getString("avgbw")==null&&num==7||
                                            jobj.getString("avgbw").equals("null")&&num==7||
                                            jobj.getString("avgbw").equals("")&&num==7||
                                            jobj.getString("avgbw")==null&&num==14||
                                            jobj.getString("avgbw").equals("null")&&num==14||
                                            jobj.getString("avgbw").equals("")&&num==14||
                                            jobj.getString("avgbw")==null&&num==21||
                                            jobj.getString("avgbw").equals("null")&&num==21||
                                            jobj.getString("avgbw").equals("")&&num==21||
                                            jobj.getString("avgbw")==null&&num>=25||
                                            jobj.getString("avgbw").equals("null")&&num>=25||
                                            jobj.getString("avgbw").equals("")&&num>=25){
                                        actList.setAvgbw("merah");
                                        actList.setValid("2");
                                        countermerah++;
                                    }else{
                                        actList.setAvgbw(jobj.getString("avgbw"));
                                    }
                                    //morta tidak boleh kosong
                                    if(jobj.getString("morta")==null||jobj.getString("morta").equals("null")||jobj.getString("morta").equals("")){
                                        actList.setMorta("merah");
                                        actList.setValid("2");
                                        countermerah++;
                                    }else{
                                        actList.setMorta(jobj.getString("morta"));
                                    }
                                    //feedc tidak boleh kosong
                                    if(jobj.getString("feedc")==null||jobj.getString("feedc").equals("null")||jobj.getString("feedc").equals("")){
                                        actList.setFeedc("merah");
                                        actList.setValid("2");
                                        countermerah++;
                                    }else{
                                        actList.setFeedc(jobj.getString("feedc"));
                                    }
                                }
                                activityList.add(actList);
                            }
                            pDialog.hide();
                        }
                        catch (JSONException e) {
                            pDialog.hide();
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Tidak ada data",Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        }
                        activityAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("error_monitor2",error.toString());
                        pDialog.hide();
                        Toast.makeText(getActivity(),"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }
}
