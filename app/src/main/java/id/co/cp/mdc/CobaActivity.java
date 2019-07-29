package id.co.cp.mdc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.cp.mdc.adapter.CobaAdapter;
import id.co.cp.mdc.model.CobaModel;

public class CobaActivity extends AppCompatActivity {


    ListView lv;
    CobaModel cobaModel;
    List<CobaModel> cobaModels;
    CobaAdapter cobaAdapter;
    Button btn, btnCekSemua, btnunCek, btnReverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);

        loadData();

        lv=(ListView)findViewById(R.id.lv);
        btn=(Button)findViewById(R.id.btn);
        btnunCek=(Button)findViewById(R.id.btnunCek);

        btnCekSemua=(Button)findViewById(R.id.btnCekSemua);
        btnReverse=(Button)findViewById(R.id.btnreverse);

        cobaAdapter=new CobaAdapter(this, cobaModels);
        lv.setAdapter(cobaAdapter);

        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cobaAdapter.reverseCek();
            }
        });

        btnCekSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cobaAdapter.cekAll();
            }
        });

        btnunCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cobaAdapter.uncekAll();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesan();
            }
        });

    }

    void pesan(){
        for (CobaModel cm: cobaModels){
            Toast.makeText(CobaActivity.this, cm.getNama() + " " + String.valueOf(cm.getFlag()), Toast.LENGTH_LONG).show();
        }
    }

    void loadData(){
        cobaModels=new ArrayList<>();

        cobaModel=new CobaModel();
        cobaModel.setNama("Devara");
        cobaModel.setFlag(true);
        cobaModels.add(cobaModel);

        cobaModel=new CobaModel();
        cobaModel.setNama("Sheira");
        cobaModel.setFlag(true);
        cobaModels.add(cobaModel);

        cobaModel=new CobaModel();
        cobaModel.setNama("Putri");
        cobaModel.setFlag(false);
        cobaModels.add(cobaModel);


    }
}
