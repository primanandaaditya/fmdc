package id.co.cp.mdc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.cp.mdc.FarmLocationActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.model.ListFarmModel;
import id.co.cp.mdc.model.ListFarmModel.Model;
import id.co.cp.mdc.model.ListFarmModel.Datum;

public class ListFarmAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Datum> datumList;
    private ArrayList<Datum> arraylist;


    public ListFarmAdapter(Activity activity, List<Datum> datumList) {
        this.activity = activity;
        this.datumList=datumList;

        this.arraylist = new ArrayList<Datum>();
        this.arraylist.addAll(datumList);
    }

    @Override
    public int getCount() {
        return datumList.size();
    }

    @Override
    public Object getItem(int location) {
        return datumList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_farm, null);


        TextView tv_farm_name = (TextView) convertView.findViewById(R.id.farm_name);
        TextView tv_farm_area = (TextView) convertView.findViewById(R.id.farm_area);
        TextView tv_farm_branch = (TextView) convertView.findViewById(R.id.farm_branch);
        TextView tv_farm_address = (TextView) convertView.findViewById(R.id.farm_address);
        TextView tv_farm_lat=(TextView)convertView.findViewById(R.id.farm_lat);
        TextView tv_farm_long=(TextView)convertView.findViewById(R.id.farm_longi);

        // getting movie data for the row
        final Datum datum = datumList.get(position);

        tv_farm_name.setText(datum.getFamnm());
        tv_farm_address.setText("Address: "+datum.getFmadr());
        tv_farm_area.setText("Area: "+datum.getArenm());
        tv_farm_branch.setText("Branch: "+datum.getBrcnm());
        tv_farm_lat.setText("Lat: "+datum.getLatit());
        tv_farm_long.setText("Long: "+datum.getLongi());

        tv_farm_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double lati, longi;

                lati=Double.parseDouble(datum.getLatit());
                longi=Double.parseDouble(datum.getLongi());

                Intent intent = new Intent(activity, FarmLocationActivity.class);
                intent.putExtra("lati", lati);
                intent.putExtra("longi", longi);
                intent.putExtra("farm_name", datum.getFamnm());
                activity.startActivity(intent);
                //Toast.makeText(activity, datum.getLatit().toString() + " " + datum.getLongi().toString(), Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        datumList.clear();
        if (charText.length() == 0) {
            datumList.addAll(arraylist);
        }
        else
        {
            for (Datum datum : arraylist)
            {
                if (datum.getFamnm().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    datumList.add(datum);
                }
            }
        }
        notifyDataSetChanged();
    }

}