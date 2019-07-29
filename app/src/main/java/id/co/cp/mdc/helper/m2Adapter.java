package id.co.cp.mdc.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.other.CircularNetworkImageView;

/**
 * Created by USER on 08/01/2018.
 */

public class m2Adapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private static LayoutInflater inflater;
    private List<m2List> m2List;

    public m2Adapter(Activity activity, List<m2List> m2List) {
        this.activity = activity;
        this.m2List = m2List;
    }

    @Override
    public int getCount() {
        return m2List.size();
    }

    @Override
    public Object getItem(int i) {
        return m2List.get(i);
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

        TextView fi1 = (TextView) convertView.findViewById(R.id.fi1);
        TextView fu1 = (TextView) convertView.findViewById(R.id.fu1);
        TextView ti1 = (TextView)convertView.findViewById(R.id.ti1);
        TextView to1 = (TextView)convertView.findViewById(R.id.to1);
        TextView saldo1 = (TextView)convertView.findViewById(R.id.saldo1);

        TextView fi2 = (TextView) convertView.findViewById(R.id.fi2);
        TextView fu2 = (TextView) convertView.findViewById(R.id.fu2);
        TextView ti2 = (TextView)convertView.findViewById(R.id.ti2);
        TextView to2 = (TextView)convertView.findViewById(R.id.to2);
        TextView saldo2 = (TextView)convertView.findViewById(R.id.saldo2);

        TextView fi3 = (TextView) convertView.findViewById(R.id.fi3);
        TextView fu3 = (TextView) convertView.findViewById(R.id.fu3);
        TextView ti3 = (TextView)convertView.findViewById(R.id.ti3);
        TextView to3 = (TextView)convertView.findViewById(R.id.to3);
        TextView saldo3 = (TextView)convertView.findViewById(R.id.saldo3);

        TextView fi4 = (TextView) convertView.findViewById(R.id.fi4);
        TextView fu4 = (TextView) convertView.findViewById(R.id.fu4);
        TextView ti4 = (TextView)convertView.findViewById(R.id.ti4);
        TextView to4 = (TextView)convertView.findViewById(R.id.to4);
        TextView saldo4 = (TextView)convertView.findViewById(R.id.saldo4);

        m2List m = m2List.get(position);

        zfmnm.setText(m.getZfmnm());
        zfmid.setText(m.getZfmid().substring(5,8));
        lsabw.setText(m.getLsabw() + " kg");
        lsstk.setText(m.getLsstk()+"/"+m.getDocin()+" ekor");
        runnm.setText(m.getRunnm());
        tag.setText(m.getTag());
        last_time.setText(m.getZdate());
        fcr.setText("FCR: "+m.getFcr());

        fi1.setText(m.getFi1());
        fu1.setText(m.getFu1());
        ti1.setText(m.getTi1());
        to1.setText(m.getTo1());
        saldo1.setText(m.getSaldo1());

        fi2.setText(m.getFi2());
        fu2.setText(m.getFu2());
        ti2.setText(m.getTi2());
        to2.setText(m.getTo2());
        saldo2.setText(m.getSaldo2());

        fi3.setText(m.getFi3());
        fu3.setText(m.getFu3());
        ti3.setText(m.getTi3());
        to3.setText(m.getTo3());
        saldo3.setText(m.getSaldo3());

        fi4.setText(m.getFi4());
        fu4.setText(m.getFu4());
        ti4.setText(m.getTi4());
        to4.setText(m.getTo4());
        saldo4.setText(m.getSaldo4());
        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}