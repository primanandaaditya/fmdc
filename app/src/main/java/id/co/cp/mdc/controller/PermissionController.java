package id.co.cp.mdc.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionController  {


    public int PERMISSION_ALL = 1;
    public String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};
    static AppCompatActivity appCompatActivity;

    public PermissionController(AppCompatActivity appCompatActivity){
        this.appCompatActivity=appCompatActivity;
    }

    public void doPermissions(){
        if(!hasPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(appCompatActivity, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions( String... permissions) {
        if ( appCompatActivity != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(appCompatActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
