package id.co.cp.mdc.other;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.MainActivity;
import id.co.cp.mdc.helper.SQLiteHandler;

public abstract class BaseActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SQLiteHandler db;

    public void toast(String teks){
        Toast.makeText(this, teks, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(String title, String message){
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        progressDialog.show();
    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    public void sessionTimeOut(){
        db=new SQLiteHandler(this);
        db.deleteTabel();

        Toast.makeText(this,"Session Time Out. Harap login kembali",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void serverMaintenance(){
        Toast.makeText(this,"Server sedang dalam perbaikan.",Toast.LENGTH_LONG).show();

    }

    public void serverMaintenanceToMainActivity(){
        Toast.makeText(this,"Server sedang dalam perbaikan.",Toast.LENGTH_LONG).show();
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }
}
