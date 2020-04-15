package com.jendouba.volunteer

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.collections.ArrayList

class ServiceAdapter(var context: Context) : BaseAdapter() {

    var servicesList: ArrayList<Service> = ArrayList()

    fun setData(servicesList:ArrayList<Service> ) {
        this.servicesList = servicesList
    }


    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        var convertView = LayoutInflater.from(parent?.context).inflate(R.layout.service_item, parent, false)
        var tvDate : TextView = convertView.findViewById(R.id.tvDate)
        var tvUser : TextView = convertView.findViewById(R.id.tvUser)
        var serviceState : TextView = convertView.findViewById(R.id.serviceState)

        val selectedService = this.servicesList[position]
        tvDate.text = selectedService.dateDemande
        tvUser.text = selectedService.user
        serviceState.text = getState(selectedService.etat)

        convertView.setOnClickListener {
            val intent = Intent(context, ServiceDetailsActivity::class.java)
            intent.putExtra("service_data", selectedService)
            context.startActivity(intent)
        }


        return convertView
    }

    fun getState(state: Int): String {
        if (state == 0) {
            return "في إنتظار التأكيد"
        } else if (state == 1) {
            return "مؤكد"
        } else if (state == -1) {
            return "ملغي"
        } else {
            return "تم الإيصال"
        }
    }



    override fun getItem(position: Int): Any {
        return this.servicesList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return this.servicesList.count()
    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

}
