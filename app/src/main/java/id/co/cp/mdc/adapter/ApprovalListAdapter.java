package id.co.cp.mdc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import id.co.cp.mdc.R;
import id.co.cp.mdc.model.approve.ZList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ApprovalListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ZList> zlists;
    private ArrayList<ZList> arrayList;
    String tgl, P4Date, P4No;
    int noUrut, adjM, adjF;

    public ApprovalListAdapter(Context context, List<ZList> zlists){
        this.context = context;
        this.zlists = zlists;

        if (zlists==null){

        }else{
            this.arrayList=new ArrayList<>();
            this.arrayList.addAll(zlists);
        }

    }

    @Override
    public int getCount() {
        if (zlists==null){
            return 0;
        }else{
            return zlists.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return zlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.approval_list, null);


        TextView tv_batch = (TextView)convertView.findViewById(R.id.tv_batch);
        TextView tv_P4_m = (TextView)convertView.findViewById(R.id.tv_P4_m);
        TextView tv_P4_f = (TextView)convertView.findViewById(R.id.tv_P4_f);
        TextView tv_LPAB_m = (TextView)convertView.findViewById(R.id.tv_LPAB_m);
        TextView tv_LPAB_f = (TextView)convertView.findViewById(R.id.tv_LPAB_f);
        TextView tv_adj_m = (TextView)convertView.findViewById(R.id.tv_adj_m);
        TextView tv_adj_f = (TextView)convertView.findViewById(R.id.tv_adj_f);
        TextView tv_date = (TextView)convertView.findViewById(R.id.tv_date);
        TextView tvNoUrut=(TextView)convertView.findViewById(R.id.tvNoUrut);
        TextView tvP4Date=(TextView)convertView.findViewById(R.id.tv_P4_Date);
        TextView tvP4No=(TextView)convertView.findViewById(R.id.tv_P4_Number);
        TextView tv_farmer = (TextView)convertView.findViewById(R.id.tv_farmer);
        Switch switch_approve = (Switch)convertView.findViewById(R.id.switch_approve);


        final ZList zlist = zlists.get(position);

        noUrut=position+1;
        tvNoUrut.setText(String.valueOf(noUrut)+".");
        tv_batch.setText(zlist.getBatch());
        tv_P4_m.setText(pemisah_ribuan(zlist.getMortaPims()));
        tv_P4_f.setText(pemisah_ribuan(zlist.getFeedusePims()));
        tv_LPAB_m.setText(pemisah_ribuan(zlist.getMortaFmdc()));
        tv_LPAB_f.setText(pemisah_ribuan(zlist.getFeeduseFmdc()));
        tv_adj_m.setText(pemisah_ribuan(zlist.getSelisihMorta()));
        tv_adj_f.setText(pemisah_ribuan(zlist.getSelisihFeed()));
        tv_farmer.setText(zlist.getZfmid() + " - " + zlist.getFamnm());

        tgl=zlist.getRecdt();
        tv_date.setText(tgl.substring(8,10) + "-" + tgl.substring(5,7) + "-" + tgl.substring(0,4));



        if (zlist.getP4_date() !=null){
            P4Date=zlist.getP4_date();
            tvP4Date.setText(P4Date.substring(8,10) + "-" + P4Date.substring(5,7) + "-" + P4Date.substring(0,4));
        }else{
            tvP4Date.setText("");
        }

        if (zlist.getP4_no()!=null){
            P4No=zlist.getP4_no();
            tvP4No.setText(P4No);
        }else{
            tvP4No.setText("");
        }



        switch_approve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               zlist.setCek(isChecked);
            }
        });
        //harus dibawah switch_approve.setOnCheckedChangeListener
        //kalau nggak, data tidak konsisten
        switch_approve.setChecked(zlist.getCek());

        return convertView;
    }

    public void cekAll(){
        for (ZList zl: this.zlists){
            zl.setCek(true);
        }
        notifyDataSetChanged();
    }

    public void uncekAll(){
        for (ZList zl: this.zlists){
            zl.setCek(false);
        }
        notifyDataSetChanged();
    }

    public void filter(String charText) {
        if (zlists==null){

        }else{
            charText = charText.toLowerCase(Locale.getDefault());
            zlists.clear();
            if (charText.length() == 0) {
                zlists.addAll(arrayList);
            }
            else
            {
                for (ZList zList : arrayList)
                {
                    if (zList.getFamnm().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        zlists.add(zList);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public String pemisah_ribuan(String angka){
        String hasil = "";
        Integer iAngka = Integer.parseInt(angka);
        hasil = String.format("%,d", iAngka).replace(',', '.');
        return  hasil;

    }

    public void hideCek(){

    }
}
