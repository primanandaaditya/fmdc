package id.co.cp.mdc.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.cp.mdc.ApproveListActivity;
import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.MainActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.adapter.ApprovalCountAdapter;
import id.co.cp.mdc.adapter.UserListAdapter;
import id.co.cp.mdc.controller.ApproveCountController;
import id.co.cp.mdc.controller.UserListController;
import id.co.cp.mdc.interfaces.IApproveCount;
import id.co.cp.mdc.interfaces.IBaseFragment;
import id.co.cp.mdc.interfaces.IUserList;
import id.co.cp.mdc.model.UserListModel;
import id.co.cp.mdc.model.approvecount.ApproveCountModel;
import id.co.cp.mdc.model.approvecount.Zlist;
import id.co.cp.mdc.model.approvecount.Sysrc;
import id.co.cp.mdc.other.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends BaseFragment implements IBaseFragment, IApproveCount {

    ListView lv;
    SearchView sv;
    ApproveCountController approveCountController;
    ApprovalCountAdapter approvalCountAdapter;
    Boolean resume=false;
    private int status = 0;

    SwipeRefreshLayout sr;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setTitle("P4 Approval");
        findID();
        getData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void findID() {

        sv=(SearchView)getActivity().findViewById(R.id.sv);
        sv.clearFocus();

        lv=(ListView)getActivity().findViewById(R.id.lv);
        sr=(SwipeRefreshLayout)getActivity().findViewById(R.id.sr);

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        sv.setQueryHint("Search");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                try {
                    approvalCountAdapter.filter(s);
                }
                catch(Exception e) {
                    //  Block of code to handle errors
                }

                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Zlist zlist = (Zlist)parent.getAdapter().getItem(position);
                String jmlAppr=zlist.getJmlappr();
                if (jmlAppr.equals("0")){
                    Toast.makeText(getActivity(), "Tidak ada approval", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getActivity(), ApproveListActivity.class);
                    intent.putExtra("id", zlist.getZtsid());
                    intent.putExtra("user", zlist.getNamaTS());
                    intent.putExtra("status", status);
                    startActivity(intent);

                }

            }
        });
    }

    @Override
    public void getData() {
        approveCountController=new ApproveCountController(getActivity(), this);
        approveCountController.getData();
        sr.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (resume=false){
            resume=true;
        }else{
            getData();
        }
    }

    @Override
    public void onApproveCountError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApproveCountSuccess(ApproveCountModel approveCountModel) {

        Integer jmlAppr;
        String str_jmlAppr;
        List<Zlist> zlistList;
        List<Zlist> zlists;

        status= approveCountModel.getSysrc().getStatus();
        switch (approveCountModel.getSysrc().getStatus()){
            case 0:
//                jmlAppr=0;
//                str_jmlAppr="";

                 zlistList = approveCountModel.getZlist();
                 zlists=new ArrayList<>();
                for (Zlist zlist:zlistList){
                    str_jmlAppr=zlist.getJmlappr();
                    jmlAppr=Integer.parseInt(str_jmlAppr);

                    if (jmlAppr <= 0){

                    }else{
                        zlists.add(zlist);
                    }
                }
                approvalCountAdapter=new ApprovalCountAdapter(getActivity(),zlists);
                lv.setAdapter(approvalCountAdapter);
                break;

            case 1:
                this.sessionTimeOut();
                break;

            case 2:
//                jmlAppr=0;
//                str_jmlAppr="";

                 zlistList = approveCountModel.getZlist();

                 zlists=new ArrayList<>();
                for (Zlist zlist:zlistList){
                    str_jmlAppr=zlist.getJmlappr();
                    jmlAppr=Integer.parseInt(str_jmlAppr);

                    if (jmlAppr <= 0){

                    }else{
                        zlists.add(zlist);
                    }
                }
                approvalCountAdapter=new ApprovalCountAdapter(getActivity(),zlists);
                lv.setAdapter(approvalCountAdapter);
                break;

        }
    }
}
