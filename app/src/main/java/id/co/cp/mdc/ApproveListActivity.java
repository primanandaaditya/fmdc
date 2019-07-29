package id.co.cp.mdc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.cp.mdc.adapter.ApprovalListAdapter;
import id.co.cp.mdc.controller.ApproveListController;
import id.co.cp.mdc.controller.ProcessApproveController;
import id.co.cp.mdc.interfaces.IApprove;
import id.co.cp.mdc.interfaces.IProcessApprove;
import id.co.cp.mdc.model.approve.P4Model;
import id.co.cp.mdc.model.approve.ZList;
import id.co.cp.mdc.model.approvecount.Zlist;
import id.co.cp.mdc.model.processapprove.ProcessApproveModel;
import id.co.cp.mdc.other.BaseActivity;


public class ApproveListActivity extends BaseActivity implements IApprove, IProcessApprove {


    ListView lv;
    TextView tv_farmer;
    Switch cekAll;
    List<ZList> zlists;
    ZList zlist;
    Button btnApprove;
    Boolean cek;
    String batch;
    SearchView sv;


    ApprovalListAdapter approvalListAdapter;
    ApproveListController approveListController;
    ProcessApproveController processApproveController;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search, menu);
//        MenuItem mSearch = menu.findItem(R.id.action_search);
//        SearchView mSearchView = (SearchView) mSearch.getActionView();
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (zlists==null){
//
//                }else{
//                    approvalListAdapter.filter(newText);
//                }
//                return true;
//            }
//        });
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        coba();

    }

    void coba(){
        Intent intent=getIntent();
        String id = intent.getStringExtra("id");

        approveListController = new ApproveListController(ApproveListActivity.this, id, this);
        approveListController.getApproveList();

    }

    @Override
    public void onApproveError(String error) {
        Toast.makeText(ApproveListActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApproveSuccess(P4Model p4Model) {
        lv=(ListView)findViewById(R.id.lv);
        lv.requestFocus();


        if (p4Model.getZList()==null){

        }else{

            zlists = p4Model.getZList();
            int jml = zlists.size();

            Intent intent=getIntent();
            String nama_farmer=intent.getStringExtra("user");
            String id = intent.getStringExtra("id");
            setTitle(id + " - " + nama_farmer + " (" + String.valueOf(jml) + ")");

            approvalListAdapter=new ApprovalListAdapter(ApproveListActivity.this, zlists);
            lv.setAdapter(approvalListAdapter);

        }

        cekAll=(Switch)findViewById(R.id.cekAll);
        cekAll.setEnabled(true);
        cekAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true){
                    approvalListAdapter.cekAll();
                }else{
                    approvalListAdapter.uncekAll();
                }
            }
        });

        btnApprove=(Button)findViewById(R.id.btnApprove);
        Intent intent=getIntent();
        Integer status = intent.getIntExtra("status",0);


        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                AlertDialog.Builder builder = new AlertDialog.Builder(ApproveListActivity.this);
                builder.setMessage("Apakah Anda yakin akan melakukan approve?").setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();

//
            }
        });

        tv_farmer=(TextView)findViewById(R.id.tv_farmer);
        tv_farmer.setText("");

        sv=(SearchView)findViewById(R.id.sv);
        sv.clearFocus();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                approvalListAdapter.filter(s);
                return false;
            }
        });


        //jika yang login adalah TS
        //maka sembunyikan tombol Approve
        if (status==2){
            btnApprove.setEnabled(false);
            btnApprove.setVisibility(View.GONE);
            cekAll.setVisibility(View.GONE);
        }else{
            btnApprove.setEnabled(true);
            btnApprove.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onProcessError(String error) {
        Toast.makeText(ApproveListActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProcessSuccess(ProcessApproveModel processApproveModel) {
//
    }



    @Override
    public void doProcess() {

        try {
            int jml=jmlApprove();
            if (jml==0){
                Toast.makeText(ApproveListActivity.this, "Mohon pilih minimal satu", Toast.LENGTH_SHORT).show();
            }else{

                this.showProgressDialog("Loading", "Processing...");
                for (ZList z: zlists){
                    cek=z.getCek();
                    batch=z.getBatch();
                    if (cek==true){
                        processApproveController=new ProcessApproveController(ApproveListActivity.this, batch, this);
                        processApproveController.doProcess();
                    }
                }
                this.dismissProgressDialog();
                coba();
                this.toast("Approval telah diupdate");
            }
        }
        catch(Exception e) {
            //  Block of code to handle errors
        }



    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    doProcess();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };


    public int jmlApprove(){
        int counter = 0;
        for (ZList z: zlists){
            if (z.getCek()==true){
                counter=counter + 1;
            }
        }

        return counter;
    }


}
