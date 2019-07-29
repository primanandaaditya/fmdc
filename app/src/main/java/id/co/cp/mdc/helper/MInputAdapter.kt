package id.co.cp.mdc.helper

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import id.co.cp.mdc.MassInputActivity
import id.co.cp.mdc.R
import id.co.cp.mdc.app.AppConfig
import id.co.cp.mdc.app.AppController
import id.co.cp.mdc.fragments.FullFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject









class MInputAdapter (val minputList: ArrayList<MInputList>) : RecyclerView.Adapter<MInputAdapter.ViewHolder>() {
    //this method is returning the view for each item in the list

//    val CallBack mCallBack
    public interface CallBack{
        fun MyMethod()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MInputAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_messinput, parent, false)

        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: MInputAdapter.ViewHolder, position: Int) {
        holder.bindItems(minputList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return minputList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context = itemView.context
        var inputtgl:String = ""
        var inputbatch:String = ""
        var inputmorta:String = ""
        var inputbw:String = ""
        var totaljenisfeed: Int = 0
        val inputjenisfeed = mutableListOf<String>()
        var inputtotalfeed = mutableListOf<String>()

        fun bindItems(user: MInputList) {

//            Log.e("adapter_check",user.toString())

            val farmer = itemView.findViewById(R.id.farmer) as TextView
            val alamat = itemView.findViewById(R.id.alamatfarmer) as TextView
            val tanggal = itemView.findViewById(R.id.tanggal) as TextView
            val jumlahmort = itemView.findViewById(R.id.jumlahmort) as EditText
            val jumlahbw = itemView.findViewById(R.id.jumlahbw) as EditText
            val feed = itemView.findViewById(R.id.feed) as SearchableSpinner
            val pfeed = itemView.findViewById(R.id.pfeed) as ImageView
            val mfeed = itemView.findViewById(R.id.mfeed) as ImageView
            val inputfeed = itemView.findViewById(R.id.inputfeed) as LinearLayout
            val jumlahfeed = itemView.findViewById(R.id.jumlahfeed) as EditText
            val save = itemView.findViewById(R.id.save) as Button
            inputtgl = user.recdt
            if(Integer.parseInt(user.age)==7||Integer.parseInt(user.age)==14||Integer.parseInt(user.age)==21||Integer.parseInt(user.age)>=25) {
                tanggal.setTextColor(Color.RED)
            }
            else{
                tanggal.setTextColor(Color.BLACK)
            }
            inputbatch = user.batch
            inputmorta=""
            inputbw=""

            save.setOnClickListener(View.OnClickListener {
                inputjenisfeed.clear()
                inputtotalfeed.clear()
                inputmorta = jumlahmort.text.toString()
                inputbw = jumlahbw.text.toString()
                totaljenisfeed = inputfeed.childCount
                for (i in 0 until inputfeed.getChildCount()) {
                    val rlP1 = inputfeed.getChildAt(i)
                    val feed = rlP1.findViewById(R.id.feed) as SearchableSpinner
                    val jumlahfeed = rlP1.findViewById(R.id.jumlahfeed) as EditText
                    inputjenisfeed.add(feed.getSelectedItem().toString())
                    inputtotalfeed.add(jumlahfeed.text.toString())
                }
                Log.e("kirim_data",jumlahmort.text.toString())
                saveinput()
            })

            jumlahmort.setText("")
            jumlahbw.setText("")
            jumlahfeed.setText("")
            farmer.setText(user.name+" ("+user.farmer_id+")")
            alamat.setText(user.address)
            tanggal.setText(inputtgl+" ("+user.age+")")

            val spinfeed = java.util.ArrayList<String>()
            spinfeed.add("s00")
            spinfeed.add("s10")
            spinfeed.add("s11")
            spinfeed.add("s12")
            val dataFeed = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinfeed)
            dataFeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            feed.setAdapter(dataFeed)

            pfeed.setOnClickListener{
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                val rowView = inflater!!.inflate(R.layout.list_feedmessinput, null)
                // Add the new row before the add field button.
                if(inputfeed.childCount<4){
                    inputfeed.addView(rowView, inputfeed.getChildCount())
                }
                val feed = rowView.findViewById(R.id.feed) as SearchableSpinner
                val dataFeed = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinfeed)
                dataFeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                feed.setAdapter(dataFeed)
            }

            mfeed.setOnClickListener{
                if(inputfeed.childCount>1){
                    inputfeed.removeViewAt(inputfeed.childCount-1)
                }
            }
        }

        fun saveinput(){
            val pDialog = ProgressDialog(context)
            pDialog.setMessage("Please wait...")
            pDialog.setCancelable(false)
            pDialog.show()

            val db = SQLiteHandler(context)
            val user = db.userDetails
            val email = user["email"]
            val token = user["pass"]

            val JObj = JSONObject()
            val JFeed = JSONArray()
            for(i in 0..totaljenisfeed-1){
                val JObjFeed = JSONObject()
                JObjFeed.put("jfeed",inputjenisfeed.get(i))
                JObjFeed.put("jumla",inputtotalfeed.get(i))
                JFeed.put(JObjFeed)
            }

            try {
                JObj.put("type","send_mass_input")
                JObj.put("token", token)
                JObj.put("zuser", email)
                JObj.put("clien", AppConfig.clien)
                JObj.put("tnggl", inputtgl)
                JObj.put("longi", FullFragment.MInputClass.longi)
                JObj.put("latit", FullFragment.MInputClass.latit)
                JObj.put("batch", inputbatch)
                JObj.put("jmlmo", inputmorta)
                JObj.put("jmlbw", inputbw)
                JObj.put("ifeed", JFeed)
                Log.e("json_obj_kirim",JObj.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val jsObjRequest = JsonObjectRequest(Request.Method.POST, AppConfig.urlMassInput2, JObj,
                    Response.Listener { response ->
                        pDialog.hide()
                        val message = response.getString("message")
                        Toast.makeText(context,message.toString(),Toast.LENGTH_LONG).show()
                        (context as MassInputActivity).getAllMassData("second")

//                        (context as MassInputActivity).refreshFull()
//                        (context as MassInputActivity).refreshPartial()
//                        (context as MassInputActivity).getPartialCount()
//                        (context as MassInputActivity).getFullCount()
//                        (context as MassInputActivity).getBlockedCount()
                    },
                    Response.ErrorListener { error ->
                        pDialog.hide()
                        Toast.makeText(context,"Tidak terhubung dengan server",Toast.LENGTH_LONG).show()
                    })
            jsObjRequest.retryPolicy = DefaultRetryPolicy(
                    50000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsObjRequest)
        }
    }
}
