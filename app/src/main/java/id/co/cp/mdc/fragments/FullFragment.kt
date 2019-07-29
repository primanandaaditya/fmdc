package id.co.cp.mdc.fragments


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import id.co.cp.mdc.LoginActivity
import id.co.cp.mdc.MassInputActivity
import id.co.cp.mdc.R
import id.co.cp.mdc.app.AppConfig
import id.co.cp.mdc.helper.MInputAdapter
import id.co.cp.mdc.helper.MInputList
import id.co.cp.mdc.helper.SQLiteHandler
import kotlinx.android.synthetic.main.activity_minput.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FullFragment : Fragment() {
    var input = ArrayList<MInputList>()
    var name: String = ""
    var tanggal: String = ""
    lateinit var adapter:MInputAdapter
    lateinit var db: SQLiteHandler
    lateinit var pDialog: ProgressDialog

    class MInputClass {
        companion object Factory {
            var latit:String = ""
            var longi:String = ""
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_full, container, false)

        pDialog = ProgressDialog(activity)
        pDialog.setMessage("Please wait...")
        pDialog.setCancelable(false)

        db = SQLiteHandler(activity)


        val listinput = rootView.findViewById(R.id.listinput) as RecyclerView
        listinput.layoutManager = LinearLayoutManager(activity)

        val searchname = rootView.findViewById(R.id.searchname) as EditText
        val search = rootView.findViewById(R.id.search) as ImageView
        search.setOnClickListener(View.OnClickListener {
            name = searchname.text.toString()
            searchName()
        })

        val date = rootView.findViewById(R.id.date) as FloatingActionButton
        date.setOnClickListener(View.OnClickListener {
            getDate()
        })

        adapter = MInputAdapter(input)
        listinput.adapter = adapter

        getData()
        getLocation()

        return rootView
    }

    private fun getLocation() {
        if (activity?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED && activity?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 101) }
        } else {
            val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            val locationGPS = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val locationNetwork = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (locationGPS != null) {
                FullFragment.MInputClass.longi = locationGPS.longitude.toString()
                FullFragment.MInputClass.latit= locationGPS.latitude.toString()
            } else {
                if (locationNetwork == null) {
                    Toast.makeText(activity, "Mohon aktifkan GPS", Toast.LENGTH_SHORT).show()
                } else {
                    FullFragment.MInputClass.longi = locationNetwork.longitude.toString()
                    FullFragment.MInputClass.latit = locationNetwork.latitude.toString()
                }
            }
        }
    }

    fun getDate(){
        val c = Calendar.getInstance()
        var setyear = c.get(Calendar.YEAR)
        var setmonth = c.get(Calendar.MONTH)
        var setday = c.get(Calendar.DAY_OF_MONTH)
        var tgl= ""
        var bln = ""

        val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            if(dayOfMonth<=9){
                tgl = "0"+dayOfMonth.toString()
            }else{
                tgl = dayOfMonth.toString()
            }
            if(monthOfYear+1<=9){
                bln = "0"+(monthOfYear+1).toString()
            }else{
                bln = (monthOfYear+1).toString()
            }
            val thn = year.toString()

            tanggal = thn+"-"+bln+"-"+tgl
            searchDate()
        }, setyear, setmonth, setday)
        dpd.show()
    }

    fun getData_bu(){
        pDialog.show()
        input.clear()

        val user = db.userDetails
        val email = user["email"]
        val token = user["pass"]

        val url = AppConfig.urlMassInput
        val jsonObj = JSONObject()
        jsonObj.put("clien", AppConfig.clien)
        jsonObj.put("zuser",email)
        jsonObj.put("tocod",email)
        jsonObj.put("type","get_mass_input")
        jsonObj.put("token",token)

        val que = Volley.newRequestQueue(activity)
        val req = JsonObjectRequest(Request.Method.POST,url,jsonObj,
                Response.Listener {
                    pDialog.hide()
//                    Log.e("inputan",jsonObj.toString())
//                    Log.e("response",it.toString())
                    val stat = it.getString("stat")
                    val message = ""
                    if(stat.equals("0")){
                        try {
                            val data = it.getJSONArray("data")
                            AppConfig.fulljum = data.length().toString()
                            (activity as MassInputActivity).getFullCount()
                            for (i in 0..data.length() - 1) {
                                val jobj = data.getJSONObject(i)
                                val name = jobj.getString("name")
                                val address = jobj.getString("address")
                                val zfmid = jobj.getString("zfmid")
                                val farmer_id = jobj.getString("farmer_id")
                                val batch = jobj.getString("batch")
                                val recdt = jobj.getString("recdt")
                                val opened = jobj.getString("opened")
                                val age = jobj.getString("age")
                                input.add(MInputList(name, address, zfmid, farmer_id, batch, recdt, opened, age))
                            }

                            Log.e("isi_array_fragment_old",input.toString())
                            val adapter = MInputAdapter(input)
                            listinput.adapter = adapter
                        }
                        catch (e:JSONException){
                            AppConfig.fulljum = "0"
                        }
                        finally {
//                            AppConfig.fulljum = "0"
                            (activity as MassInputActivity).getFullCount()
                            val adapter = MInputAdapter(input)
                            listinput.adapter = adapter
                        }
                    }
                    else if(stat.equals("1")){
                        val message = it.getString("message")
                        Toast.makeText(activity,message.toString(), Toast.LENGTH_LONG).show()
                    }
                    else if(stat.equals("2")){
                        val message = it.getString("message")
                        Toast.makeText(activity,message.toString(), Toast.LENGTH_LONG).show()
                        db.deleteTabel()
                        val intent = Intent(activity, LoginActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                }, Response.ErrorListener {
            pDialog.hide()
            Toast.makeText(activity,"Tidak terhubung dengan server", Toast.LENGTH_LONG).show()
            Log.e("error",it.toString())
        })
        req.retryPolicy = DefaultRetryPolicy(
                100000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        que.add(req)
    }
    fun convertNull(param:String):String{
        if(param.equals("null")){
            return ""
        }
        else{
            return param
        }
    }
    fun getData(){
//        pDialog.show()
        input.clear()
        val data = MassInputActivity.ArrayMassData.data.ArrayFull
        if (data != null) {
            AppConfig.fulljum = data.length().toString()
            (activity as MassInputActivity).getFullCount()
            for (i in 0..data.length() - 1) {
                val jobj = data.getJSONObject(i)
                val name = jobj.getString("name")
                val address = jobj.getString("address")
                val zfmid = jobj.getString("zfmid")
                val farmer_id = jobj.getString("farmer_id")
                val batch = jobj.getString("batch")
                val recdt = jobj.getString("recdt")
                val opened = jobj.getString("opened")
                val age = jobj.getString("age")
                input.add(MInputList(name, address, zfmid, farmer_id, batch, recdt, opened, age))
            }
        }
        adapter.notifyDataSetChanged();
    }

    fun searchName() {
        input.clear()
        val data = MassInputActivity.ArrayMassData.data.ArrayFull
        if (data != null) {
            for (i in 0..data.length() - 1) {
                val jobj = data.getJSONObject(i)
                if (jobj.getString("name").contains(name) || jobj.getString("name").contains(name.toUpperCase())) {
                    val jobj = data.getJSONObject(i)
                    val name = jobj.getString("name")
                    val address = jobj.getString("address")
                    val zfmid = jobj.getString("zfmid")
                    val farmer_id = jobj.getString("farmer_id")
                    val batch = jobj.getString("batch")
                    val recdt = jobj.getString("recdt")
                    val opened = jobj.getString("opened")
                    val age = convertNull(jobj.getString("age"))
                    val s00 = convertNull(jobj.getString("s00"))
                    val s10 = convertNull(jobj.getString("s10"))
                    val s11 = convertNull(jobj.getString("s11"))
                    val s12 = convertNull(jobj.getString("s12"))
                    val morta = convertNull(jobj.getString("morta"))
                    val bw = convertNull(jobj.getString("avgbw"))
                    input.add(MInputList(name, address, zfmid, farmer_id, batch, recdt, opened, age))
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun searchDate() {
//        pDialog.show()
        input.clear()
        val data = MassInputActivity.ArrayMassData.data.ArrayFull
        if (data != null) {
            for (i in 0..data.length() - 1) {
                val jobj = data.getJSONObject(i)
                if (tanggal.substring(0, 4).equals(jobj.getString("recdt").substring(0, 4)) &&
                        tanggal.substring(5, 7).equals(jobj.getString("recdt").substring(5, 7)) &&
                        tanggal.substring(8, 10).equals(jobj.getString("recdt").substring(8, 10))) {
                    val name = jobj.getString("name")
                    val address = jobj.getString("address")
                    val zfmid = jobj.getString("zfmid")
                    val farmer_id = jobj.getString("farmer_id")
                    val batch = jobj.getString("batch")
                    val recdt = jobj.getString("recdt")
                    val opened = jobj.getString("opened")
                    val age = convertNull(jobj.getString("age"))
                    val s00 = convertNull(jobj.getString("s00"))
                    val s10 = convertNull(jobj.getString("s10"))
                    val s11 = convertNull(jobj.getString("s11"))
                    val s12 = convertNull(jobj.getString("s12"))
                    val morta = convertNull(jobj.getString("morta"))
                    val bw = convertNull(jobj.getString("avgbw"))
                    input.add(MInputList(name, address, zfmid, farmer_id, batch, recdt, opened, age))
                }
            }
        }
        adapter.notifyDataSetChanged()
    }
}
