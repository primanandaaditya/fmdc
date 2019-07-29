package id.co.cp.mdc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import id.co.cp.mdc.helper.zGeneralFunction;

import id.co.cp.mdc.interfaces.IBaseFragment;
import id.co.cp.mdc.model.approve.ZList;

public class DetailApproveActivity extends AppCompatActivity implements IBaseFragment {


    TextView tv_farmer, tvP4Date, tvP4No;
    TextView tvDocIn, tvP4FM, tv_LPAB;
    TextView tvAdj;

    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_approve);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findID();
        getData();
    }

    @Override
    public void findID(){
        tv_farmer=(TextView)findViewById(R.id.tv_farmer);
        tvP4Date=(TextView)findViewById(R.id.tv_P4_Date);
        tvP4No=(TextView)findViewById(R.id.tv_P4_Number);
        tvDocIn=(TextView)findViewById(R.id.tvDocIn);
        tvP4FM=(TextView)findViewById(R.id.tvP4MF);
        tv_LPAB=(TextView)findViewById(R.id.tv_LPAB);
        tvAdj=(TextView)findViewById(R.id.tv_adj);

        btnClose=(Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getData() {

        Intent intent=getIntent();
        ZList zList = intent.getParcelableExtra("zList");
        tv_farmer.setText(zList.getZfmid() + " - " + zList.getFamnm());

        String p4Date=zList.getP4_date();
        tvP4Date.setText(p4Date.substring(8,10) + "-" + p4Date.substring(5,7) + "-" + p4Date.substring(0,4));
        tvP4No.setText(zList.getP4_no());
        tvDocIn.setText(zList.getRecdt());
        String docIn=zList.getRecdt();
        tvDocIn.setText(docIn.substring(8,10) + "-" + docIn.substring(5,7) + "-" + docIn.substring(0,4));

        tvP4FM.setText( zGeneralFunction.pemisah_ribuan(zList.getMortaPims()) + " / " + zGeneralFunction.pemisah_ribuan(zList.getFeedusePims()));
        tv_LPAB.setText( zGeneralFunction.pemisah_ribuan(zList.getMortaFmdc()) + " / " + zGeneralFunction.pemisah_ribuan(zList.getFeeduseFmdc()));
        tvAdj.setText(zGeneralFunction.pemisah_ribuan(zList.getSelisihMorta()) + " / " + zGeneralFunction.pemisah_ribuan(zList.getSelisihFeed()));
    }
}
