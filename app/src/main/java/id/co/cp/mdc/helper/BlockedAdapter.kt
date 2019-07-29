package id.co.cp.mdc.helper

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import id.co.cp.mdc.R

class BlockedAdapter (val blockedList: ArrayList<BlockedList>) : RecyclerView.Adapter<BlockedAdapter.ViewHolder>() {
    //this method is returning the view for each item in the list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_blocked, parent, false)

        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: BlockedAdapter.ViewHolder, position: Int) {
        holder.bindItems(blockedList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return blockedList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context = itemView.context
        var inputtgl:String = ""
        var inputbatch:String = ""
        var inputmorta:String = ""
        var inputbw:String = ""
        var inputs00:String=""
        var inputs10:String=""
        var inputs11:String=""
        var inputs12:String=""
        var totaljenisfeed: Int = 0
        val inputjenisfeed = mutableListOf<String>()
        var inputtotalfeed = mutableListOf<String>()
        var FeedBlockList = ArrayList<FeedBlockList>()

        fun bindItems(user: BlockedList) {

            val farmer = itemView.findViewById(R.id.farmer) as TextView
            val alamat = itemView.findViewById(R.id.alamatfarmer) as TextView
            val tanggal = itemView.findViewById(R.id.tanggal) as TextView
            val jumlahmort = itemView.findViewById(R.id.jumlahmort) as TextView
            val jumlahbw = itemView.findViewById(R.id.jumlahbw) as TextView
            val jumlahs00 = itemView.findViewById(R.id.jumlahfeed1) as TextView
            val jumlahs10 = itemView.findViewById(R.id.jumlahfeed2) as TextView
            val jumlahs11 = itemView.findViewById(R.id.jumlahfeed3) as TextView
            val jumlahs12 = itemView.findViewById(R.id.jumlahfeed4) as TextView

            val listfeed = itemView.findViewById(R.id.listfeed) as NestedListView
            val adapter = FeedBlockedAdapter(context as Activity?, FeedBlockList)
            listfeed.setAdapter(adapter)
//            getListFeedBlocked()
            inputtgl = user.recdt
            if(Integer.parseInt(user.age)==7||Integer.parseInt(user.age)==14||Integer.parseInt(user.age)==21||Integer.parseInt(user.age)>=25) {
                tanggal.setTextColor(Color.RED)
            }
            else{
                tanggal.setTextColor(Color.BLACK)
            }
            inputbatch = user.batch

            farmer.setText(user.name+" ("+user.farmer_id+")")
            alamat.setText(user.address)
            tanggal.setText(inputtgl+" ("+user.age+")")
            jumlahmort.setText(user.morta)
            jumlahbw.setText(user.avgbw)
            jumlahs00.setText(user.s00)
            jumlahs10.setText(user.s10)
            jumlahs11.setText(user.s11)
            jumlahs12.setText(user.s12)
        }

//        fun getListFeedBlocked(){
//            val user = db.userDetails
//            val email = user["email"]
//            val token = user["pass"]
//
//            val url = AppConfig.urlMassInput
//            val jsonObj = JSONObject()
//            jsonObj.put("clien", AppConfig.clien)
//            jsonObj.put("zuser",email)
//            jsonObj.put("tocod",email)
//            jsonObj.put("type","get_mass_input")
//            jsonObj.put("token",token)
//
//            val que = Volley.newRequestQueue(activity)
//            val req = JsonObjectRequest(Request.Method.POST,url,jsonObj,
//                    Response.Listener {
//                        pDialog.hide()
//                        Log.e("inputan",jsonObj.toString())
//                        Log.e("response",it.toString())
//                        val stat = it.getString("stat")
//                        val message = ""
//                        if(stat.equals("0")){
//                            val data = it.getJSONArray("data")
//                            for (i in 0..data.length()-1){
//                                val jobj = data.getJSONObject(i)
//                                val name = jobj.getString("name")
//                                val address = jobj.getString("address")
//                                val zfmid = jobj.getString("zfmid")
//                                val farmer_id = jobj.getString("farmer_id")
//                                val batch = jobj.getString("batch")
//                                val recdt = jobj.getString("recdt")
//                                val opened = jobj.getString("opened")
//                                val age = jobj.getString("age")
//                                input.add(BlockedList(name,address,zfmid,farmer_id,batch,recdt,opened,age))
//                            }
//                            val adapter = BlockedAdapter(input)
//                            listinput.adapter = adapter
//
//                        }
//                        else if(stat.equals("1")){
//                            val message = it.getString("message")
//                            Toast.makeText(activity,message.toString(), Toast.LENGTH_LONG).show()
//                        }
//                        else if(stat.equals("2")){
//                            val message = it.getString("message")
//                            Toast.makeText(activity,message.toString(), Toast.LENGTH_LONG).show()
//                            db.deleteTabel()
//                            val intent = Intent(activity, LoginActivity::class.java)
//                            startActivity(intent)
//                            activity.finish()
//                        }
//
//                    }, Response.ErrorListener {
//                pDialog.hide()
//                Toast.makeText(activity,"Tidak terhubung dengan server", Toast.LENGTH_LONG).show()
//                Log.e("error",it.toString())
//            })
//            que.add(req)
//        }
    }
}
