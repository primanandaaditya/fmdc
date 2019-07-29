package id.co.cp.mdc;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import id.co.cp.mdc.interfaces.IBaseFragment;

public class DetailLokasiActivity extends FragmentActivity implements IBaseFragment, OnMapReadyCallback {

    private GoogleMap mMap;
    String longi, latit;
    Double dlongi, dlatit;
    String famnm, address, area, branch;
    Button btnClose;
    TextView tvfarmname, tvaddress, tvarea, tvbranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lokasi);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getData();
        findID();



        // Add a marker in Sydney and move the camera
        LatLng lokasi = new LatLng( dlatit, dlongi);
        mMap.addMarker(new MarkerOptions().position(lokasi).title(famnm)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));

        CameraUpdate center= CameraUpdateFactory.newLatLng(lokasi);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);


    }

    @Override
    public void findID() {
        tvfarmname=(TextView)findViewById(R.id.tvfarmname);
        tvfarmname.setText(famnm);

        tvaddress=(TextView)findViewById(R.id.tvaddress);
        tvaddress.setText(address);

        tvarea=(TextView)findViewById(R.id.tvarea);
        tvarea.setText(area);

        tvbranch=(TextView)findViewById(R.id.tvbranch);
        tvbranch.setText(branch);

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
        longi=intent.getStringExtra("longi");
        latit=intent.getStringExtra("latit");

        dlongi=Double.parseDouble(longi);
        dlatit=Double.parseDouble(latit);

        famnm=intent.getStringExtra("famnm");
        address=intent.getStringExtra("fmadr");
        area=intent.getStringExtra("arenm");
        branch=intent.getStringExtra("brcnm");

    }
}
