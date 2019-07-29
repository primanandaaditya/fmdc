package id.co.cp.mdc.fragments;


import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import id.co.cp.mdc.DetailLokasiActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.adapter.ListFarmAdapter;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.controller.ListFarmController;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.interfaces.ListFarmInterface;
import id.co.cp.mdc.model.ListFarmModel;
import id.co.cp.mdc.model.ListFarmModel.Model;
import id.co.cp.mdc.model.ListFarmModel.Datum;

public class ListFarmFragment extends Fragment implements ListFarmInterface {

    private GoogleMap mMap;
    private LatLng mytag;
    private ProgressDialog pDialog;

    private String name;
    private String email;
    private String token;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String TAG = MyTagFragment.class.getSimpleName();
    private SQLiteHandler db;
    private String selFmId;

    double longitude, latitude;
    ListView lv;
    SearchView sv;
    ListFarmAdapter listFarmAdapter;
    ListFarmController listFarmController;


    public ListFarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listFarmController = new ListFarmController(getActivity(), this);
        listFarmController.getList();

        return inflater.inflate(R.layout.fragment_list_farm, container, false);

    }


    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(Model model) {

        if (model.getData()==null){

        }else{
            lv=(ListView)getActivity().findViewById(R.id.lv);
            listFarmAdapter=new ListFarmAdapter(getActivity(), model.getData() );

            Collections.sort(model.getData(), ListFarmModel.datumComparator);
            listFarmAdapter.notifyDataSetChanged();
            lv.setAdapter(listFarmAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Datum datum=(Datum)parent.getAdapter().getItem(position);

                    Intent intent=new Intent(getActivity(), DetailLokasiActivity.class);
                    intent.putExtra("famnm", datum.getFamnm());
                    intent.putExtra("arenm", datum.getArenm());
                    intent.putExtra("brcnm", datum.getBrcnm());
                    intent.putExtra("fmadr", datum.getFmadr());
                    intent.putExtra("latit", datum.getLatit());
                    intent.putExtra("mapcd",datum.getMapcd());
                    intent.putExtra("tsnme", datum.getTsnme());
                    intent.putExtra("longi", datum.getLongi());
                    startActivity(intent);
                }
            });


            sv=(SearchView)getActivity().findViewById(R.id.sv);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    listFarmAdapter.filter(s);
                    return false;
                }
            });
        }

    }
}
