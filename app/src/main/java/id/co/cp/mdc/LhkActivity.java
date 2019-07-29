package id.co.cp.mdc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.fragments.AyamFragment;
import id.co.cp.mdc.fragments.CheckInFragment;
import id.co.cp.mdc.fragments.FourFragment;
import id.co.cp.mdc.fragments.OneFragment;
import id.co.cp.mdc.fragments.ThreeFragment;
import id.co.cp.mdc.fragments.TwoFragment;

public class LhkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText ztxtMortality;
    private EditText ztxtWeight;
    private String in;
    private Integer i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // remove title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lhk);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        //Prima
        //jika activity awal tidak ada intent dg nama intent
        //maka fokus ke tab 0
        //jika ada, maka fokus ke tab tersebut
        //karena di fragment Photo ada modal/dialog
        if(getIntent().getStringExtra("intent")==null){
            viewPager.setCurrentItem(0);
        }else{
            in = getIntent().getStringExtra("intent");
            i = Integer.parseInt(in);
            viewPager.setCurrentItem(i);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        ztxtMortality = (EditText) findViewById(R.id.ztxtMortality);
//        ztxtMortality.setText("");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        String in = getIntent().getStringExtra("close");
        if(in==null){
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
        }else{
            finish();
        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CheckInFragment(), "Absensi");
        adapter.addFragment(new FourFragment(), "Cycle");
        adapter.addFragment(new OneFragment(), "LPAB");
        adapter.addFragment(new TwoFragment(), "Doc In");
        adapter.addFragment(new ThreeFragment(), "Transfer");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
