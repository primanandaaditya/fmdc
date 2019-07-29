package id.co.cp.mdc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.app.AppController;

import id.co.cp.mdc.model.UserListModel;
import id.co.cp.mdc.model.UserListModel.UserList;
import id.co.cp.mdc.model.UserListModel.Datum;
import id.co.cp.mdc.other.CircularNetworkImageView;

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private static LayoutInflater inflater;
    private List<Datum> datumList;
    private ArrayList<Datum> datumArrayList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public UserListAdapter(Context context, List<Datum> datumList){
        this.context=context;
        this.datumList=datumList;

        this.datumArrayList=new ArrayList<>();
        this.datumArrayList.addAll(datumList);

    }

    @Override
    public int getCount() {
        return datumList.size();
    }

    @Override
    public Object getItem(int position) {
        return datumList.get(position);
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
        TextView tag = (TextView) convertView.findViewById(R.id.tag);
        TextView time = (TextView)convertView.findViewById(R.id.last_time);
        TextView runnm = (TextView)convertView.findViewById(R.id.runnm);

        Datum m = datumList.get(position);

        // thumbnail get image
       thumbNail.setImageUrl(AppConfig.server_ip+m.getUrl(), imageLoader);

        // title
        username.setText(m.getZuser());
        tag.setText("");
        time.setText(m.getLttme());
        runnm.setText(m.getRemain());

        return convertView;
    }

    public void filter(String charText) {
        if (datumList==null){
            charText = charText.toLowerCase(Locale.getDefault());
            datumList.clear();
            if (charText.length() == 0) {
                datumList.addAll(datumArrayList);
            }
            else
            {
                for (Datum datum : datumArrayList)
                {
                    if (datum.getZuser().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        datumList.add(datum);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }

}
