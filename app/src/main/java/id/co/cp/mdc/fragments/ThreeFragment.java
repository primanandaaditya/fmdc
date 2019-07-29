package id.co.cp.mdc.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.helper.FillSpinner;
import id.co.cp.mdc.helper.SQLiteHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThreeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThreeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FillSpinner fillSpinner = new FillSpinner();

    private OnFragmentInteractionListener mListener;
    private LinearLayout FeedForm;
    private Button btnAddFeed;
    private Spinner spinnerFeed;
    private Spinner spinnerFarm;
    private Spinner spinnerTs;
    private Spinner spinnerBranch;
    private Spinner spinnerFarmTs;
    private Button zbtnDate3;
    private TextView zTxtDate3;
    private Date cDate = new Date();
    private Button btnSaveFeed;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private AlertDialog.Builder adb_trfed;

    List<String> listBranch = new ArrayList<String>();
    List<String> listTS = new ArrayList<String>();
    List<String> listFarmTS = new ArrayList<String>();
    private View EmptyView;
    private String brcid;
    private String tsid;
    private String area_id;

    public ThreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThreeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThreeFragment newInstance(String param1, String param2) {
        ThreeFragment fragment = new ThreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_three, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        FeedForm = (LinearLayout)rootView.findViewById(R.id.FeedForm);
        btnAddFeed = (Button)rootView.findViewById(R.id.btnAddFeed);
        zbtnDate3 = (Button)rootView.findViewById(R.id.zbtnDate3);
        zTxtDate3 = (TextView)rootView.findViewById(R.id.zTxtDate3);
        spinnerFarm = (Spinner) rootView.findViewById(R.id.spinnerFarm);
        fillSpinner.FillSpinFarm(spinnerFarm,getActivity());
        db = new SQLiteHandler(getActivity().getApplicationContext());

        btnSaveFeed = (Button)rootView.findViewById(R.id.btnSaveFeed);
        btnSaveFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FarmFrom=spinnerFarm.getSelectedItem().toString().substring(11,spinnerFarm.getSelectedItem().toString().length());
                String FarmTo=spinnerFarmTs.getSelectedItem().toString().substring(6,spinnerFarmTs.getSelectedItem().toString().length());
                adb_trfed.setMessage("Pengirim\t: "+FarmFrom+"\nPenerima\t: "+FarmTo);
                adb_trfed.show();
            }
        });

        btnAddFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddForm(spinnerFeed.getSelectedItem().toString());
            }
        });

        zbtnDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        spinnerBranch = (Spinner)rootView.findViewById(R.id.spinnerBranch);
        listBranch();
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listTS.clear();
                brcid = spinnerBranch.getSelectedItem().toString().substring(0,3);
                listTS();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTs = (Spinner) rootView.findViewById(R.id.spinnerTs);
        spinnerTs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listFarmTS.clear();
                tsid = spinnerTs.getSelectedItem().toString().substring(0,3);
                listFarmTS();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFarmTs = (Spinner)rootView.findViewById(R.id.spinnerFarmTs);

//        fillSpinner.FillSpinTs(spinnerTs,getActivity());

        spinnerFeed = (Spinner) rootView.findViewById(R.id.spinnerFeed);
//        spinnerFeed.setOnItemSelectedListener(this);

        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        zTxtDate3.setText(DateNow);
        List<String> spinner3list = new ArrayList<String>();
        spinner3list.add("s00");
        spinner3list.add("s10");
        spinner3list.add("s11");
        spinner3list.add("s12");
        // BEGIN ADD by Michael Julian 20190502 penambahan list VACC/MEDC
        spinner3list.add("VACC");
        spinner3list.add("MEDC");
        ArrayAdapter<String> dataAdapterFeed = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,spinner3list);
        dataAdapterFeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFeed.setAdapter(dataAdapterFeed);
        adb_trfed = new AlertDialog.Builder(getActivity());
        adb_trfed.setTitle("Konfirmasi Transfer Feed");
        adb_trfed.setIcon(android.R.drawable.ic_dialog_alert);
        adb_trfed.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                do_save_feed();
            } });
        adb_trfed.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });

        return rootView;
    }
    public void onDeleteClicked(View v) {
        // remove the row by calling the getParent on button
        FeedForm.removeView((View) v.getParent());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
//
//    private void FillSpinner(Spinner spin){
//        List<String> categoriesFarm = new ArrayList<String>();
//
//        List<Farmer> Farmers = db.getAllFarmer();
//        for (Farmer fm : Farmers) {
//            String farmer = fm.getFarmCode()+" - "+fm.getFarmName();
//            categoriesFarm.add(farmer);
//        }
//        ArrayAdapter<String> dataAdapterFarm = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoriesFarm);
//        dataAdapterFarm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin.setAdapter(dataAdapterFarm);
//
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void AddForm(String name) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View form_list = inflater.inflate(R.layout.form_list, null);
        final TextView FeedCd = (TextView) form_list.findViewById(R.id.FeedCd);
        final Button detach = (Button) form_list.findViewById(R.id.btnDetach);
        detach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClicked(view);
            }
        });

        if (name != null && !name.isEmpty()) {
            FeedCd.setText(name);
        } else {
            EmptyView = form_list;
        }

        // A TextWatcher to control the visibility of the "Add new" button and
        // handle the exclusive empty view.

        // Inflate at the end of all rows but before the "Add new" button
        FeedForm.addView(form_list);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String ztsid = spinnerTs.getSelectedItem().toString();
        ztsid = ztsid.substring(0,8);
        FillSpinner fillSpinner = new FillSpinner();
        fillSpinner.FillSpinFarmByTs(spinnerFarmTs, ztsid, getActivity());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    private void listBranch(){
        pDialog.show();
        final JSONObject send = new JSONObject();
        try {
            send.put("clien",AppConfig.clien);
            send.put("zuser",db.getEmail());
            send.put("token",db.getToken());
            send.put("ztsid",db.getUserDetails().get("ztsid"));
            send.put("ztype","get_branch");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlSpinner, send,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
//                        Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                JSONObject jobj = data.getJSONObject(i);
                                listBranch.add(jobj.getString("BRANCH_ID")+" - "+jobj.getString("BRANCH"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> dataAdapterBranch = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,listBranch);
                        dataAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerBranch.setAdapter(dataAdapterBranch);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Log.e("volley_error",error.toString());
                        Toast.makeText(getActivity(),"Harap mencari signal untuk melakukan transfer feed. Jika pesan ini terus berlanjut harap menghubungi tim FMDC.",Toast.LENGTH_LONG).show();
                        hidepDialog();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsObjRequest);
    }

    private void listTS(){
        pDialog.show();
        final JSONObject send = new JSONObject();
        try {
            send.put("clien",AppConfig.clien);
            send.put("zuser",db.getEmail());
            send.put("token",db.getToken());
            send.put("ztsid",db.getUserDetails().get("ztsid"));
            send.put("ztype","get_ts");
            send.put("brcid",brcid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlSpinner, send,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
//                        Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                JSONObject jobj = data.getJSONObject(i);
                                listTS.add(jobj.getString("TS_ID")+" - "+jobj.getString("TS"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> dataAdapterTs = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,listTS);
                        dataAdapterTs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTs.setAdapter(dataAdapterTs);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Log.e("volley_error",error.toString());
                        Toast.makeText(getActivity(),"Harap mencari signal untuk melakukan transfer feed. Jika pesan ini terus berlanjut harap menghubungi tim FMDC.",Toast.LENGTH_LONG).show();
                        hidepDialog();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsObjRequest);
    }

    private void listFarmTS(){
        pDialog.show();
        final JSONObject send = new JSONObject();
        try {
            send.put("clien",AppConfig.clien);
            send.put("zuser",db.getEmail());
            send.put("token",db.getToken());
            send.put("ztsid",db.getUserDetails().get("ztsid"));
            send.put("ztype","get_farm");
            send.put("brcid",brcid);
            send.put("tsid",tsid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlSpinner, send,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
//                        Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                JSONObject jobj = data.getJSONObject(i);
                                listFarmTS.add(jobj.getString("FARMER_ID")+" - "+jobj.getString("NAME"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> dataAdapterFarmTS = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,listFarmTS);
                        dataAdapterFarmTS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerFarmTs.setAdapter(dataAdapterFarmTS);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Log.e("volley_error",error.toString());
                        Toast.makeText(getActivity(),"Harap mencari signal untuk melakukan transfer feed. Jika pesan ini terus berlanjut harap menghubungi tim FMDC.",Toast.LENGTH_LONG).show();
                        hidepDialog();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsObjRequest);
    }

    private void do_save_feed(){
        int countChild = FeedForm.getChildCount();
        String DateNow = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JSONArray fedar = new JSONArray();
        try {
            JSONObject allts = db.getAllTs().getJSONObject(0);
            area_id = allts.getString("area_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean duplicate=false;
        boolean empty=false;
        //get feed
        for(int i=0;i<countChild;i++) {
            JSONObject jobj = new JSONObject();
            View ChildView = FeedForm.getChildAt(i);
            EditText txtFeed = (EditText) ChildView.findViewById(R.id.txtFeed);
            TextView FeedCd = (TextView) ChildView.findViewById(R.id.FeedCd);
            //check duplicate and empty field
            if(txtFeed.getText().toString().equals("")){
                empty=true;
            }
            for(int j=0;j<i;j++){
                try {
                    String check = fedar.getJSONObject(j).getString("fedcd");
                    if(FeedCd.getText().toString().equals(check)){
                        duplicate=true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //done check duplicate and empty field
            try {
                jobj.put("fedcd",FeedCd.getText().toString());
                jobj.put("quant",txtFeed.getText().toString());
                fedar.put(jobj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //done get feed
        String famfr = spinnerFarm.getSelectedItem().toString().substring(0,8);
        String famto =  area_id+spinnerBranch.getSelectedItem().toString().substring(0,3)+spinnerFarmTs.getSelectedItem().toString().substring(0,3);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(zTxtDate3.getText().toString());
            date2 = sdf.parse(DateNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(countChild==0){
            Toast.makeText(getActivity(),"tidak ada feed yang di transfer",Toast.LENGTH_LONG).show();
            return;
        }
        if(duplicate){
            Toast.makeText(getActivity(),"terdapat duplikasi Feed Type",Toast.LENGTH_LONG).show();
            return;
        }
        if(empty){
            Toast.makeText(getActivity(),"terdapat quantity yang kosong",Toast.LENGTH_LONG).show();
            return;
        }
        if(famfr.equals(famto)){
            Toast.makeText(getActivity(),"farm pengirim dan penerima tidak boleh sama",Toast.LENGTH_LONG).show();
            return;
        }
        if(date1.after(date2)){
            Toast.makeText(getActivity(), "Tanggal tidak valid", Toast.LENGTH_LONG).show();
            hidepDialog();
            return;
        }
        JSONObject send = new JSONObject();
        try {
            send.put("famfr",famfr);
            send.put("famto",famto);
            send.put("fedar",fedar);
            send.put("recdt",zTxtDate3.getText().toString());
            send.put("token",db.getToken());
            send.put("zuser",db.getEmail());
            send.put("clien",AppConfig.clien);
            send.put("ztype","trfed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlTrfFeed, send,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("stat");
                            String message = response.getString("message");
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                            if(stat.equals("0")){
                                FeedForm.removeAllViews();
                            }
                            else if(stat.equals("2")){
                                db.deleteTabel();
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            hidepDialog();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            hidepDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley_error",error.toString());
                        Toast.makeText(getActivity(),"Harap mencari signal untuk melakukan transfer feed. Jika pesan ini terus berlanjut harap menghubungi tim FMDC.",Toast.LENGTH_LONG).show();
                        hidepDialog();
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0, 1));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsObjRequest);
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment datepicker = new DatePickerFragment();
        datepicker.setTxtDate("zTxtDate3");
        DialogFragment newFragment = datepicker;
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
