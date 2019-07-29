package id.co.cp.mdc

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import id.co.cp.mdc.app.AppConfig
import id.co.cp.mdc.fragments.BlockedFragment
import id.co.cp.mdc.fragments.FullFragment
import id.co.cp.mdc.fragments.PartialFragment
import id.co.cp.mdc.helper.SQLiteHandler
import kotlinx.android.synthetic.main.activity_mass_input.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MassInputActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var db: SQLiteHandler
    var partialCount: Int = 0
    var fullCount: Int = 0
    var blockedCount: Int = 0
    var testArray2: JSONArray? = null
    lateinit var pDialog: ProgressDialog

    class ArrayMassData(){
        companion object data {
            var ArrayBlocked: JSONArray? = null
            var ArrayFull: JSONArray? = null
            var ArrayPartial: JSONArray? = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mass_input)

        pDialog = ProgressDialog(this)
        pDialog.setMessage("Please wait...")
        pDialog.setCancelable(false)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Mass Input")

        db = SQLiteHandler(this)

//        PR: refresh saat input, benerin list view untuk blocked

        tabs.getTabAt(0)?.text = "Blank (0)"
        tabs.getTabAt(1)?.text = "Partial (0)"
        tabs.getTabAt(2)?.text = "Blocked (0)"
        getAllMassData("first")
//        val tab = tabs.getTabAt(1)
//        tab!!.select()


        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
//
//        // Set up the ViewPager with the sections adapter.
//        container.setCurrentItem(1)
//        container.adapter = mSectionsPagerAdapter
//        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
//        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        return true
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            var fragment: Fragment? = null
            when (position) {
                0 -> fragment = FullFragment()
                1 -> fragment = PartialFragment()
                2 -> fragment = BlockedFragment()
                else -> fragment = FullFragment()
            }
            return fragment
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    fun getPartialCount(){
//        val user = db.userDetails
//        val email = user["email"]
//        val token = user["pass"]
//
//        val url = AppConfig.urlMassInput
//        val jsonObj = JSONObject()
//        jsonObj.put("clien", AppConfig.clien)
//        jsonObj.put("zuser",email)
//        jsonObj.put("tocod",email)
//        jsonObj.put("type","get_partial_mass_input")
//        jsonObj.put("token",token)
//
//        val que = Volley.newRequestQueue(this)
//        val req = JsonObjectRequest(Request.Method.POST,url,jsonObj,
//                Response.Listener {
//                    Log.e("inputan",jsonObj.toString())
//                    val data = it.getJSONArray("data")
//                    partialCount = data.length()
//                    tabs.getTabAt(0)?.text = "Partial ("+ partialCount.toString() +")"
//                    Log.e("partialCount",partialCount.toString())
//                }, Response.ErrorListener {
//            partialCount = 0
//        })
//        que.add(req)
        tabs.getTabAt(1)?.text = "Partial ("+ AppConfig.partialjum.toString() +")"
    }

    fun getFullCount(){
//        val user = db.userDetails
//        val email = user["email"]
//        val token = user["pass"]
//
//        val url = AppConfig.urlMassInput
//        val jsonObj = JSONObject()
//        jsonObj.put("clien", AppConfig.clien)
//        jsonObj.put("zuser",email)
//        jsonObj.put("tocod",email)
//        jsonObj.put("type","get_mass_input")
//        jsonObj.put("token",token)
//
//        val que = Volley.newRequestQueue(this)
//        val req = JsonObjectRequest(Request.Method.POST,url,jsonObj,
//                Response.Listener {
//                    Log.e("inputan",jsonObj.toString())
//                    val data = it.getJSONArray("data")
//                    fullCount = data.length()
//                    tabs.getTabAt(1)?.text = "Full ("+ fullCount.toString() +")"
//                    Log.e("fullCount",fullCount.toString())
//                }, Response.ErrorListener {
//            fullCount = 0
//        })
//        que.add(req)
                    tabs.getTabAt(0)?.text = "Blank ("+ AppConfig.fulljum.toString() +")"
    }

    fun getAllMassData(a:String){
        pDialog.show()
        val pemakai=db.userDetails
        val user = db.userDetails
        val email = user["email"]
        val token = user["pass"]

        val url = AppConfig.urlMassInput
        val jsonObj = JSONObject()
        jsonObj.put("clien", AppConfig.clien)
        jsonObj.put("zuser",email)
        jsonObj.put("tocod",email)
        jsonObj.put("type","get_all_mass_input")
        jsonObj.put("token",token)

        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(Request.Method.POST,url,jsonObj,
                Response.Listener {
                    if(it.getString("stat").equals("0")){
                        try {
                            ArrayMassData.data.ArrayBlocked = it.getJSONArray("data_blocked")
                            AppConfig.blockedjum = it.getJSONArray("data_blocked").length().toString()
                        }
                        catch(e:JSONException){
                            ArrayMassData.data.ArrayBlocked = JSONArray(ArrayList<String>())
                            AppConfig.blockedjum = "0"
                        }
                        try {
                            ArrayMassData.data.ArrayFull = it.getJSONArray("data_full")
                            AppConfig.fulljum = it.getJSONArray("data_full").length().toString()
                        }
                        catch (e:JSONException){
                            ArrayMassData.data.ArrayFull = JSONArray(ArrayList<String>())
                            AppConfig.fulljum = "0"
                        }
                        try {
                            ArrayMassData.data.ArrayPartial = it.getJSONArray("data_partial")
                            AppConfig.partialjum = it.getJSONArray("data_partial").length().toString()
                        }
                        catch(e:JSONException){
                            ArrayMassData.data.ArrayPartial = JSONArray(ArrayList<String>())
                            AppConfig.partialjum = "0"
                        }
                        getPartialCount()
                        getFullCount()
                        getBlockedCount()
                        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

                        // Set up the ViewPager with the sections adapter.
                        pDialog.hide()
                        if(a.equals("first")) {
                            container.setCurrentItem(1)
                            container.adapter = mSectionsPagerAdapter
                            container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
                            tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
                        }
                        else if(a.equals("second")){
                            if(supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.container+":0") != null) {
                                val fragmentFull = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.container + ":0") as FullFragment
                                fragmentFull.getData()
                            }
                            if(supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.container+":1") != null) {
                                val fragmentPartial = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.container + ":1") as PartialFragment
                                fragmentPartial.getData()
                            }
                            if(supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.container+":2") != null){
                                val fragmentBlocked = supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.container+":2") as BlockedFragment
                                fragmentBlocked.getData()
                            }
                        }
                    }
                    else{
                        Toast.makeText(this,it.getString("message").toString(),Toast.LENGTH_LONG).show()
                        finish()
                    }
                }, Response.ErrorListener {
            blockedCount = 0
            fullCount = 0
            partialCount = 0
            // BEGIN add function pdialog
            pDialog.hide()
        })
        // begin Penambahan retry policy 20190319
        req.retryPolicy = DefaultRetryPolicy(
                100000,
                0,
                0F)
        // end Penambahan retry policy 20190319
        que.add(req)
//        tabs.getTabAt(2)?.text = "Blocked ("+ AppConfig.blockedjum.toString() +")"
    }

    fun getBlockedCount(){
//        Log.e("isi_blocked_count",AppConfig.blockedjum.toString())
        tabs.getTabAt(2)?.text = "Blocked ("+ AppConfig.blockedjum.toString() +")"
//        tabs.getTabAt(2)?.text = "Blocked (bae)"
    }

    fun refreshFull(){
        val fragment = supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.container+":0") as FullFragment
        fragment.getData()
    }

    fun refreshPartial(){
        val fragment = supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.container+":1") as PartialFragment
        fragment.getData()
    }
}
