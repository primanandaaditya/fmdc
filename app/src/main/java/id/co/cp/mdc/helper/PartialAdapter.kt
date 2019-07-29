package id.co.cp.mdc.helper

import android.app.ProgressDialog
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import id.co.cp.mdc.MassInputActivity
import id.co.cp.mdc.R
import id.co.cp.mdc.app.AppConfig
import id.co.cp.mdc.app.AppController
import id.co.cp.mdc.fragments.PartialFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PartialAdapter (val partialList: ArrayList<PartialList>) : RecyclerView.Adapter<PartialAdapter.ViewHolder>() {
    //this method is returning the view for each item in the list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartialAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_partial, parent, false)

        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: PartialAdapter.ViewHolder, position: Int) {
        holder.bindItems(partialList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return partialList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context = itemView.context
        var inputtgl:String = ""
        var inputbatch:String = ""
        var inputmorta:String = ""
        var inputbw:String = ""
        var inputfeed1:String = ""
        var inputfeed2:String = ""
        var inputfeed3:String = ""
        var inputfeed4:String = ""
        var totaljenisfeed: Int = 0
        val inputjenisfeed = mutableListOf<String>()
        var inputtotalfeed = mutableListOf<String>()
        var feed1:String = ""
        var feed2:String = ""
        var feed3:String = ""
        var feed4:String = ""

        fun bindItems(user: PartialList) {

            val farmer = itemView.findViewById(R.id.farmer) as TextView
            val alamat = itemView.findViewById(R.id.alamatfarmer) as TextView
            val tanggal = itemView.findViewById(R.id.tanggal) as TextView
            val jumlahmort = itemView.findViewById(R.id.jumlahmort) as EditText
            val jumlahbw = itemView.findViewById(R.id.jumlahbw) as EditText
            val jumlahfeed1 = itemView.findViewById(R.id.jumlahfeed1) as EditText
            val jumlahfeed2 = itemView.findViewById(R.id.jumlahfeed2) as EditText
            val jumlahfeed3 = itemView.findViewById(R.id.jumlahfeed3) as EditText
            val jumlahfeed4 = itemView.findViewById(R.id.jumlahfeed4) as EditText
            val save = itemView.findViewById(R.id.save) as Button


            inputtgl = user.recdt
            if(Integer.parseInt(user.age)==7||Integer.parseInt(user.age)==14||Integer.parseInt(user.age)==21||Integer.parseInt(user.age)>=25) {
                tanggal.setTextColor(Color.RED)
            }
            else{
                tanggal.setTextColor(Color.BLACK)
            }
            inputbatch = user.batch

            save.setOnClickListener(View.OnClickListener {
                inputjenisfeed.clear()
                inputtotalfeed.clear()
                inputmorta = jumlahmort.text.toString()
                inputbw = jumlahbw.text.toString()
                inputfeed1 = jumlahfeed1.text.toString()
                inputfeed2 = jumlahfeed2.text.toString()
                inputfeed3 = jumlahfeed3.text.toString()
                inputfeed4 = jumlahfeed4.text.toString()
                inputtotalfeed.add(jumlahfeed1.text.toString())
                inputjenisfeed.add("s00")
                inputtotalfeed.add(jumlahfeed2.text.toString())
                inputjenisfeed.add("s10")
                inputtotalfeed.add(jumlahfeed3.text.toString())
                inputjenisfeed.add("s11")
                inputtotalfeed.add(jumlahfeed4.text.toString())
                inputjenisfeed.add("s12")
                saveinput()
            })

            farmer.setText(user.name+" ("+user.farmer_id+")")
            alamat.setText(user.address)
            tanggal.setText(inputtgl+" ("+user.age+")")
            jumlahmort.setText(user.morta)
            jumlahbw.setText(user.avgbw)
            jumlahfeed1.setText(user.s00)
            jumlahfeed2.setText(user.s10)
            jumlahfeed3.setText(user.s11)
            jumlahfeed4.setText(user.s12)

            feed1 = jumlahfeed1.text.toString()
            feed2 = jumlahfeed2.text.toString()
            feed3 = jumlahfeed3.text.toString()
            feed4 = jumlahfeed4.text.toString()
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

            val JObjFeed1 = JSONObject()
            JObjFeed1.put("jfeed","s00")
            JObjFeed1.put("jumla", inputfeed1)
            JFeed.put(JObjFeed1)
            val JObjFeed2 = JSONObject()
            JObjFeed2.put("jfeed","s10")
            JObjFeed2.put("jumla", inputfeed2)
            JFeed.put(JObjFeed2)
            val JObjFeed3 = JSONObject()
            JObjFeed3.put("jfeed","s11")
            JObjFeed3.put("jumla",inputfeed3)
            JFeed.put(JObjFeed3)
            val JObjFeed4 = JSONObject()
            JObjFeed4.put("jfeed","s12")
            JObjFeed4.put("jumla",inputfeed4)
            JFeed.put(JObjFeed4)

            try {
                JObj.put("type","send_mass_input")
                JObj.put("token", token)
                JObj.put("zuser", email)
                JObj.put("clien", AppConfig.clien)
                JObj.put("tnggl", inputtgl)
                JObj.put("longi", PartialFragment.MInputClass.longi)
                JObj.put("latit", PartialFragment.MInputClass.latit)
                JObj.put("batch", inputbatch)
                JObj.put("jmlmo", inputmorta)
                JObj.put("jmlbw", inputbw)
                JObj.put("ifeed", JFeed)
            }
            catch (e: JSONException) {
                e.printStackTrace()
            }


            val jsObjRequest = JsonObjectRequest(Request.Method.POST, AppConfig.urlMassInput2, JObj,
                    Response.Listener { response ->
                        pDialog.hide()
                        val message = response.getString("message")
                        Toast.makeText(context,message.toString(), Toast.LENGTH_LONG).show()
                        (context as MassInputActivity).getAllMassData("second")
//                        (context as MassInputActivity).refreshPartial()
//                        (context as MassInputActivity).refreshFull()
//                        (context as MassInputActivity).getPartialCount()
//                        (context as MassInputActivity).getFullCount()
//                        (context as MassInputActivity).getBlockedCount()
                    },
                    Response.ErrorListener { error ->
                        pDialog.hide()
                        Toast.makeText(context,"Tidak terhubung dengan server", Toast.LENGTH_LONG).show()
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
