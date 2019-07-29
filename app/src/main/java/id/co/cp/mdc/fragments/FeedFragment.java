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
import android.widget.ListView;

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

import id.co.cp.mdc.FeedDetailActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.helper.FeedAdapter;
import id.co.cp.mdc.helper.FeedList;
import id.co.cp.mdc.helper.SQLiteHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    private FeedAdapter adapter;
    private ListView listView;
    private List<FeedList> FeedList = new ArrayList<FeedList>();
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SwipeRefreshLayout refresh;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);

        listView = (ListView) rootView.findViewById(R.id.listFeed);
        adapter = new FeedAdapter(getActivity(), FeedList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppConfig.Trxcd =  FeedList.get(i).gettrxcd();
                Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
                startActivity(intent);
            }
        });

        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FeedList.clear();
                getFeed();
                refresh.setRefreshing(false);
            }
        });

        getFeed();
        return rootView;
    }

    private void getFeed(){
        pDialog.show();

        db = new SQLiteHandler(getActivity());
        HashMap<String, String> user = db.getUserDetails();

        JSONObject JObj = new JSONObject();
        try {
            pDialog.show();
            JObj.put("token", user.get("pass"));
            JObj.put("zuser", user.get("email"));
            JObj.put("zfmid", AppConfig.Zfmid);
            JObj.put("clien", AppConfig.clien);
        }
        catch (JSONException e) {
            e.printStackTrace();
            pDialog.hide();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.urlTrxFeed, JObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jarr = response.getJSONArray("data");
                            for (int i=0;i<jarr.length();i++){
                                JSONObject jobj = jarr.getJSONObject(i);
                                FeedList feedList = new FeedList();
                                int num = i+1;
                                feedList.setnum(num);
                                feedList.settrxcd(jobj.getString("trxcd"));
                                feedList.settrtyp(jobj.getString("trtyp"));
                                if (jobj.getString("shkzg").equals("s")){
                                    feedList.setshkzg("+ ");
                                }else if(jobj.getString("shkzg").equals("h")){
                                    feedList.setshkzg("- ");
                                }
                                feedList.setqtyto(jobj.getString("qtyto"));
                                feedList.setrecdt(jobj.getString("recdt"));
                                FeedList.add(feedList);
                            }
                            pDialog.hide();
                        }
                        catch (JSONException e) {
                            pDialog.hide();
                            e.printStackTrace();
//                            Toast.makeText(getActivity(),"Tidak ada aktivitas yang aktif pada farm tersebut.",Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
//                        Toast.makeText(getActivity(),"Tidak terhubung dengan server.",Toast.LENGTH_LONG).show();
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
