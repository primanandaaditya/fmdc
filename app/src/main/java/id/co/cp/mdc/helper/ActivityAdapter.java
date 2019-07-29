package id.co.cp.mdc.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import id.co.cp.mdc.R;

/**
 * Created by user on 13/03/2017.
 */

public class ActivityAdapter extends BaseAdapter implements View.OnClickListener {
    private Activity activity;
    private static LayoutInflater inflater;
    private List<ActivityList> ActivityList;
    private TestCall Koci;

    interface TestCall{
        void MyMethod();
    }

    public ActivityAdapter(Activity activity, List<ActivityList> ActivityList) {
        this.activity = activity;
        this.ActivityList = ActivityList;
    }

    @Override
    public int getCount() {
        return ActivityList.size();
    }

    @Override
    public Object getItem(int position) {
        return ActivityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_activity, null);

        RelativeLayout rl1 = (RelativeLayout) convertView.findViewById(R.id.rl1);
        RelativeLayout rl2 = (RelativeLayout) convertView.findViewById(R.id.rl2);
        RelativeLayout rl3 = (RelativeLayout) convertView.findViewById(R.id.rl3);
        TextView bgteks1 = (TextView) convertView.findViewById(R.id.bgteks1);
        TextView bgteks2 = (TextView) convertView.findViewById(R.id.bgteks2);
        TextView bgteks3 = (TextView) convertView.findViewById(R.id.bgteks3);
        TextView teks2 = (TextView) convertView.findViewById(R.id.teks2);
        TextView teks3 = (TextView) convertView.findViewById(R.id.teks3);
        TextView teks4 = (TextView) convertView.findViewById(R.id.teks4);
        TextView txtDoc = (TextView) convertView.findViewById(R.id.txtDoc);
        TextView txtAvbw = (TextView) convertView.findViewById(R.id.txtAvbw);
        TextView txtFedc = (TextView) convertView.findViewById(R.id.txtFedc);
        TextView txtFnlq = (TextView) convertView.findViewById(R.id.txtFnlq);
        TextView txtMorta = (TextView) convertView.findViewById(R.id.txtMorta);
        TextView txtRecd = (TextView) convertView.findViewById(R.id.txtRecd);
        TextView txtTobw = (TextView) convertView.findViewById(R.id.txtTobw);
        TextView txtReasp = (TextView) convertView.findViewById(R.id.txtReasp);

        ActivityList a = ActivityList.get(position);
        if(a.getValid().equals("2")){
            txtRecd.setBackgroundColor(Color.RED);
        }
        else if(a.getValid().equals("1")){
            txtRecd.setBackgroundColor(Color.GREEN);
        }
        else if(a.getValid().equals("0")){
            txtRecd.setBackgroundColor(Color.YELLOW);
        }
        else{
            txtRecd.setBackgroundColor(Color.RED);
        }

        if(a.getDocin()==null||a.getDocin().equals("")){
            txtDoc.setText("-");
        }else{
            txtDoc.setText(a.getDocin());
        }

        if(a.getFnlqt()==null||a.getFnlqt().equals("")){
            txtFnlq.setText("-");
        }else{
            txtFnlq.setText(a.getFnlqt()+" ek");
        }
        if(a.getAvgbw().equals("merah")||a.getAvgbw().equals("")||a.getAvgbw().equals("null")){
            txtAvbw.setText("-");
        }else{
            txtAvbw.setText(a.getAvgbw()+" gr");
        }

        if(a.getFeedc().equals("merah")||a.getFeedc().equals("")){
            txtFedc.setText("-");
        }else{
            txtFedc.setText(a.getFeedc());
        }

        if(a.getFnlqt()==null||a.getFnlqt().equals("")){
            txtFnlq.setText("-");
        }else{
            txtFnlq.setText(a.getFnlqt()+" ek");
        }

        if(a.getMorta().equals("merah")||a.getMorta().equals("")){
            txtMorta.setText("-");
        }else{
            txtMorta.setText(a.getMorta()+" ek");
        }

        txtRecd.setText(a.getRecdt().substring(8,10)+"-"+a.getRecdt().substring(5,7)+" ("+a.getRecdt().substring(11,a.getRecdt().length())+")");
        if(a.getFnlbw()==null||a.getFnlbw().equals("")){
            txtTobw.setText("-");
        }else{
            txtTobw.setText(a.getFnlbw()+" kg");
        }

        //set default color
        teks2.setTextColor(Color.BLACK);
        teks3.setTextColor(Color.BLACK);
        teks4.setTextColor(Color.BLACK);
        txtDoc.setTextColor(Color.BLACK);
        txtAvbw.setTextColor(Color.BLACK);
        txtFedc.setTextColor(Color.BLACK);
        txtFnlq.setTextColor(Color.BLACK);
        txtMorta.setTextColor(Color.BLACK);
        txtRecd.setTextColor(Color.BLACK);
        txtTobw.setTextColor(Color.BLACK);
        txtReasp.setTextColor(Color.BLACK);
        bgteks1.setBackgroundColor(Color.TRANSPARENT);
        bgteks2.setBackgroundColor(Color.TRANSPARENT);
        bgteks3.setBackgroundColor(Color.TRANSPARENT);
        teks2.setBackgroundColor(Color.TRANSPARENT);
        teks3.setBackgroundColor(Color.TRANSPARENT);
        teks4.setBackgroundColor(Color.TRANSPARENT);
        txtDoc.setBackgroundColor(Color.TRANSPARENT);
        txtAvbw.setBackgroundColor(Color.TRANSPARENT);
        txtFedc.setBackgroundColor(Color.TRANSPARENT);
        txtFnlq.setBackgroundColor(Color.TRANSPARENT);
        txtMorta.setBackgroundColor(Color.TRANSPARENT);
        txtTobw.setBackgroundColor(Color.TRANSPARENT);
        txtReasp.setBackgroundColor(Color.TRANSPARENT);
        if(a.getDocin().equals("null")){
            txtDoc.setText("-");
        }

        if(a.getMorta().equals("merah")||a.getMorta().equals("")){
            txtMorta.setText("-");
            bgteks1.setBackgroundColor(Color.rgb(255,150,150));
//            rl1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(parent.getContext(), LhkActivity.class);
//                    parent.getContext().startActivity(intent);
//                    Toast.makeText(activity, AyamFragment.id, Toast.LENGTH_SHORT).show();
//                }
//            });
//            teks2.setTextColor(Color.RED);
        }
//        else if(a.getMorta().equals("merah")){
//            txtMorta.setText("-");
//            txtMorta.setTextColor(Color.RED);
//            teks2.setTextColor(Color.RED);
//        }

        if(a.getAvgbw().equals("merah")){
            txtAvbw.setText("-");
//            if(Integer.parseInt(a.getRecdt().substring(11,a.getRecdt().length()))==7||Integer.parseInt(a.getRecdt().substring(11,a.getRecdt().length()))==14||Integer.parseInt(a.getRecdt().substring(11,a.getRecdt().length()))==21||Integer.parseInt(a.getRecdt().substring(11,a.getRecdt().length()))>=25) {
            bgteks2.setBackgroundColor(Color.rgb(255,150,150));
//            rl2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(parent.getContext(), LhkActivity.class);
//                    parent.getContext().startActivity(intent);
//                    Toast.makeText(activity, AyamFragment.id, Toast.LENGTH_SHORT).show();
//                }
//            });
//            }
        }
//        else if(a.getAvgbw().equals("merah")){
//            txtAvbw.setText("-");
//            txtAvbw.setTextColor(Color.RED);
//            teks3.setTextColor(Color.RED);
//        }

        if(a.getFeedc().equals("merah")||a.getFeedc().equals("")){
            txtFedc.setText("-");
            bgteks3.setBackgroundColor(Color.rgb(255,150,150));
//            rl3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(parent.getContext(), LhkActivity.class);
//                    parent.getContext().startActivity(intent);
//                    Toast.makeText(activity, AyamFragment.id, Toast.LENGTH_SHORT).show();
//                }
//            });
        }
//        else if(a.getFeedc().equals("merah")){
//            txtFedc.setText("-");
//            txtFedc.setTextColor(Color.RED);
//            teks4.setTextColor(Color.RED);
//        }

        if(a.getFnlqt().equals("null")||a.getFnlqt()==null||a.getFnlqt().equals("")){
            txtFnlq.setText("-");
        }
        if(a.getFnlbw().equals("null")||a.getFnlbw()==null||a.getFnlbw().equals("")){
            txtTobw.setText("-");
        }
        if(a.getReasp().equals("null")||a.getReasp()==null||a.getReasp().equals("")){
            txtReasp.setText("-");
        }
        else{
            txtReasp.setText(a.getReasp());
        }


        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}