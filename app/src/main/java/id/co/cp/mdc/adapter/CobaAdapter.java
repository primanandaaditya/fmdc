package id.co.cp.mdc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import id.co.cp.mdc.R;
import id.co.cp.mdc.model.CobaModel;

public class CobaAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<CobaModel> cobaModels;
    //private ArrayList<Datum> arraylist;


    public CobaAdapter(Activity activity, List<CobaModel> cobaModels) {
        this.activity = activity;
        this.cobaModels=cobaModels;
    }

    @Override
    public int getCount() {
        return cobaModels.size();
    }

    @Override
    public Object getItem(int location) {
        return cobaModels.get(location);
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
            convertView = inflater.inflate(R.layout.list_coba, null);


//        TextView tv_farm_name = (TextView) convertView.findViewById(R.id.farm_name);
//        TextView tv_farm_area = (TextView) convertView.findViewById(R.id.farm_area);
//        TextView tv_farm_branch = (TextView) convertView.findViewById(R.id.farm_branch);
//        TextView tv_farm_address = (TextView) convertView.findViewById(R.id.farm_address);
//        TextView tv_farm_lat=(TextView)convertView.findViewById(R.id.farm_lat);
//        TextView tv_farm_long=(TextView)convertView.findViewById(R.id.farm_longi);

        TextView tvNama = (TextView)convertView.findViewById(R.id.tvNama);
        CheckBox cbFlag=(CheckBox)convertView.findViewById(R.id.cbFlag);

        // getting movie data for the row
        final CobaModel cobaModel = cobaModels.get(position);
        //Datum datum = datumList.get(position);

//        tv_farm_name.setText(datum.getFamnm());
//        tv_farm_address.setText("Address: "+datum.getFmadr());
//        tv_farm_area.setText("Area: "+datum.getArenm());
//        tv_farm_branch.setText("Branch: "+datum.getBrcnm());
//        tv_farm_lat.setText("Lat: "+datum.getLatit());
//        tv_farm_long.setText("Long: "+datum.getLongi());
        tvNama.setText(cobaModel.getNama());
        cbFlag.setChecked(cobaModel.getFlag());


        cbFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cobaModel.setFlag(isChecked);
            }
        });

        return convertView;
    }

    public void cekAll(){
        for (CobaModel cm: this.cobaModels){
            cm.setFlag(true);
        }
        notifyDataSetChanged();
    }

    public void uncekAll(){
        for (CobaModel cm: this.cobaModels){
            cm.setFlag(false);
        }
        notifyDataSetChanged();
    }

    public void reverseCek(){
        for (CobaModel cm: this.cobaModels){
            if (cm.getFlag()==true){
                cm.setFlag(false);
            }else{
                cm.setFlag(true);
            }
        }
        notifyDataSetChanged();
    }

}
