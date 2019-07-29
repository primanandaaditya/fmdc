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
 * Created by USER on 16/11/2017.
 */

public class FeedAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private static LayoutInflater inflater;
    private List<FeedList> FeedList;

    String tanda;

    public FeedAdapter(Activity activity, List<FeedList> FeedList) {
        this.activity = activity;
        this.FeedList = FeedList;
    }

    @Override
    public int getCount() {
        return FeedList.size();
    }

    @Override
    public Object getItem(int i) {
        return FeedList.get(i);
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
            convertView = inflater.inflate(R.layout.list_feed, null);

        TextView num = (TextView) convertView.findViewById(R.id.num);
        TextView trtyp = (TextView) convertView.findViewById(R.id.trtyp);
        TextView trxcd = (TextView) convertView.findViewById(R.id.trxcd);
        TextView qtyto = (TextView) convertView.findViewById(R.id.qtyto);
        TextView tgl  = (TextView) convertView.findViewById(R.id.tgl);

        FeedList m = FeedList.get(position);

        num.setText("" + m.getnum());
        trtyp.setText(m.gettrtyp());
        trxcd.setText(m.gettrxcd());
        qtyto.setText(m.getshkzg()+m.getqtyto()+" karung");
        tgl.setText(m.getrecdt());

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
