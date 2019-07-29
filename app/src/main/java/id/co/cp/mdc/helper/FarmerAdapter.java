package id.co.cp.mdc.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.cp.mdc.R;

/**
 * Created by user on 31/10/2016.
 */
public class FarmerAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private static LayoutInflater inflater;
    private List<FarmerList> FarmerList;

    public FarmerAdapter(Activity activity, List<FarmerList> FarmerList) {
        this.activity = activity;
        this.FarmerList = FarmerList;
    }



    @Override
    public int getCount() {
        return FarmerList.size();
    }

    @Override
    public Object getItem(int i) {
        return FarmerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_farmer, null);

        TextView zfmnm = (TextView) convertView.findViewById(R.id.zfmnm);
        TextView zfmid = (TextView) convertView.findViewById(R.id.zfmid);
        TextView lsabw  = (TextView) convertView.findViewById(R.id.lsabw);
        TextView lsstk  = (TextView) convertView.findViewById(R.id.lsstk);
        TextView last_time = (TextView)convertView.findViewById(R.id.last_time);
        TextView runnm = (TextView) convertView.findViewById(R.id.runnm);
        TextView tag = (TextView) convertView.findViewById(R.id.tag);
        TextView fcr = (TextView) convertView.findViewById(R.id.fcr);

        FarmerList m = FarmerList.get(position);

        // title
        zfmnm.setText(m.getZfmnm());
        zfmid.setText(m.getZfmid().substring(5,8));
        lsabw.setText(m.getLsabw());
        lsstk.setText(m.getLsstk());
        runnm.setText(m.getRunnm());
        tag.setText(m.getTag());
        last_time.setText(m.getZdate());
        fcr.setText(m.getFcr());

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
