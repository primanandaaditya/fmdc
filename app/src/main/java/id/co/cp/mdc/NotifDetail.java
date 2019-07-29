package id.co.cp.mdc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import id.co.cp.mdc.app.AppConfig;

public class NotifDetail extends AppCompatActivity {
    private TextView SelLgMsg;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConfig.SelectedNotif.getShmsg());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SelLgMsg = (TextView) findViewById(R.id.SelLgMsg);
        SelLgMsg.setText(AppConfig.SelectedNotif.getLgmsg());
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(NotifDetail.this, NotificationActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotifDetail.this, NotificationActivity.class);
        startActivity(intent);
        finish();
    }
}
