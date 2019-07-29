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
 * Created by USER on 17/11/2017.
 */

public class FeedDtlAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private static LayoutInflater inflater;
    private List<FeedDtlList> FeedDtlList;

    public FeedDtlAdapter(Activity activity, List<FeedDtlList> FeedDtlList) {
        this.activity = activity;
        this.FeedDtlList = FeedDtlList;
    }



    @Override
    public int getCount() {
        return FeedDtlList.size();
    }

    @Override
    public Object getItem(int i) {
        return FeedDtlList.get(i);
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
            convertView = inflater.inflate(R.layout.list_feed_detail, null);


        TextView fedcd = (TextView) convertView.findViewById(R.id.fedcd);
        TextView quant = (TextView) convertView.findViewById(R.id.quant);

        FeedDtlList m = FeedDtlList.get(position);

        // title
        fedcd.setText(m.getfedcd());
        quant.setText(m.getshkzg()+m.getquant()+" karung");

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}