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
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private static LayoutInflater inflater;
    private List<Notif> NotifList;

    public CustomAdapter(Activity activity, List<Notif> NotifList) {
        this.activity = activity;
        this.NotifList = NotifList;
    }

    static class ViewHolder {
        protected TextView notcd;
        protected TextView shmsg;
        protected TextView zdate;
    }

    @Override
    public int getCount() {
        return NotifList.size();
    }

    @Override
    public Object getItem(int i) {
        return NotifList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view==null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_notification, null);
            viewHolder = new ViewHolder();
            viewHolder.notcd = (TextView) view.findViewById(R.id.notif_id);
            viewHolder.shmsg = (TextView) view.findViewById(R.id.notif_short_msg);
            viewHolder.zdate = (TextView) view.findViewById(R.id.notif_date);
            view.setTag(viewHolder);
            view.setTag(R.id.notif_id, viewHolder.notcd);
            view.setTag(R.id.notif_date, viewHolder.zdate);
            view.setTag(R.id.notif_short_msg, viewHolder.shmsg);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.zdate.setText(NotifList.get(i).getZdate());
        viewHolder.notcd.setText(NotifList.get(i).getNotcd());
        viewHolder.shmsg.setText(NotifList.get(i).getShmsg());
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
