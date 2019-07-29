package id.co.cp.mdc.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.net.ContentHandler;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.MainActivity;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.interfaces.IBaseFragment;

public abstract class BaseFragment extends Fragment {

    SQLiteHandler db;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public void setTitle(String title){
        ((MainActivity) getActivity()).setActionBarTitle(title);
    }



    public void sessionTimeOut(){
        db=new SQLiteHandler(getActivity());
        db.deleteTabel();
        Toast.makeText(getActivity(),"Session Time Out. Harap login kembali",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void serverMaintenance(){

        Toast.makeText(getActivity(),"Server sedang dalam perbaikan.",Toast.LENGTH_LONG).show();
        Intent intent2 = new Intent(getActivity(), MainActivity.class);
        startActivity(intent2);
    }

}
