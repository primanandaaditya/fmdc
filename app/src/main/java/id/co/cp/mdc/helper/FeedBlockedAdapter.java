package id.co.cp.mdc.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import id.co.cp.mdc.R;

public class FeedBlockedAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private static LayoutInflater inflater;
    private List<FeedBlockList> FeedBlock;

    String tanda;

    public FeedBlockedAdapter(Activity activity, List<FeedBlockList> FeedBlock) {
        this.activity = activity;
        this.FeedBlock = FeedBlock;
    }

    @Override
    public int getCount() {
        return FeedBlock.size();
    }

    @Override
    public Object getItem(int i) {
        return FeedBlock.get(i);
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
            convertView = inflater.inflate(R.layout.list_feedblocked, null);

        TextView feed = convertView.findViewById(R.id.feed);
        EditText jumlahfeed = convertView.findViewById(R.id.jumlahfeed);

        FeedBlockList m = FeedBlock.get(position);

        feed.setText(m.getTipe());
        jumlahfeed.setText(m.getJumlah());

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}