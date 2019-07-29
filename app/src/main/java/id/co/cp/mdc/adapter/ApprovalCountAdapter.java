package id.co.cp.mdc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.model.UserListModel;
import id.co.cp.mdc.model.approvecount.Zlist;
import id.co.cp.mdc.other.BaseActivity;
import id.co.cp.mdc.other.CircularNetworkImageView;

public class ApprovalCountAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Zlist> zlists;
    private ArrayList<Zlist> arrayList;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();



    public ApprovalCountAdapter(Context context, List<Zlist> zlists){
        this.context=context;
        this.zlists=zlists;

        this.arrayList = new ArrayList<Zlist>();
        this.arrayList.addAll(zlists);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        zlists.clear();

        if (charText.length() == 0) {
            zlists.addAll(arrayList);
        }
        else
        {
            for (Zlist zlist : arrayList)
            {
                if (zlist.getNamaTS().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    zlists.add(zlist);
                }
            }
        }
        notifyDataSetChanged();

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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.pre_approval, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView username = (TextView) convertView.findViewById(R.id.usernm);
        TextView waiting = (TextView) convertView.findViewById(R.id.waiting);
        TextView url = (TextView)convertView.findViewById(R.id.url);
        TextView runnm = (TextView)convertView.findViewById(R.id.runnm);

//        UserListModel.Datum m = datumList.get(position);
        Zlist m = zlists.get(position);

        // thumbnail get image
        thumbNail.setImageUrl(AppConfig.server_ip+m.getUrl(), imageLoader);

        // title
        username.setText(m.getNamaTS());
        waiting.setText("Waiting for approval : (" + m.getJmlappr() + ")");
//        url.setText("URL:" + m.getUrl());
//        time.setText(m.getEmailts());
//        runnm.setText(m.getRemain());

        return convertView;
    }


}
