package id.co.cp.mdc.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppController;
import id.co.cp.mdc.other.CircularNetworkImageView;

/**
 * Created by user on 31/10/2016.
 */
public class MonitoringAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private static LayoutInflater inflater;
    private List<UserList> MonitorList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MonitoringAdapter(Activity activity, List<UserList> MonitorList) {
        this.activity = activity;
        this.MonitorList = MonitorList;
    }


    @Override
    public int getCount() {
        return MonitorList.size();
    }

    @Override
    public Object getItem(int i) {
        return MonitorList.get(i);
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
            convertView = inflater.inflate(R.layout.list_user, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView username = (TextView) convertView.findViewById(R.id.usernm);
        TextView tag = (TextView) convertView.findViewById(R.id.tag);
        TextView time = (TextView)convertView.findViewById(R.id.last_time);
        TextView runnm = (TextView)convertView.findViewById(R.id.runnm);

        UserList m = MonitorList.get(position);

        // thumbnail get image
        thumbNail.setImageUrl(m.getProfile(), imageLoader);

        // title
        username.setText(m.getUsername());
        tag.setText(m.getTag());
        time.setText(m.getTime());
        runnm.setText(m.getRunnm());

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
