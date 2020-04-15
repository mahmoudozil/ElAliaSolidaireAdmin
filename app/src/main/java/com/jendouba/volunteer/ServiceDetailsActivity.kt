package com.jendouba.volunteer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_service_details.*
import kotlinx.android.synthetic.main.activity_service_details.tvServices

class ServiceDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val serviceData = intent.getSerializableExtra("service_data") as Service
        tvUserName.text = serviceData.user
        tvCin.text = serviceData.cin
        tvAdresse.text = serviceData.adresse
        tvCity.text = serviceData.city
        tvTel.text = serviceData.tel
        tvServices.text = serviceData.service
        val volunteer = getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).getString("volunteer_email", "").toString()

        Log.e("etat: ", "${serviceData.etat}")

        if (serviceData.etat == 0) {
            tvState.text = "في إنتظار التأكيد"
            btnDone.visibility = View.GONE
            volunteer_name.text = "هذا الطلب شاغر ! يمكنك إنجازه"
        } else if (serviceData.etat == 1) {
            tvState.text = "مؤكد"
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            notice.visibility = View.GONE
            if(!serviceData.volunteer.equals(volunteer)) {
                btnDone.visibility = View.GONE
            } else {
                btnDone.visibility = View.VISIBLE
            }
            volunteer_name.text = serviceData.volunteer.subSequence(0,serviceData.volunteer.lastIndexOf("@"))
        } else if (serviceData.etat == -1) {
            tvState.text = "ملغي"
            tvState.setTextColor(Color.parseColor("#FF0000"))
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnDone.visibility = View.GONE
            notice.visibility = View.GONE
        } else if (serviceData.etat == 2) {
            tvState.text = "تم الإيصال"
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnDone.visibility = View.GONE
            notice.visibility = View.GONE
            volunteer_name.text = serviceData.volunteer.subSequence(0,serviceData.volunteer.lastIndexOf("@"))
        }

        val database = FirebaseDatabase.getInstance().reference.child(serviceData.userKey).child("services").child(serviceData.databaseKey)
        btnConfirm.setOnClickListener {
            tvState.text = "مؤكد"
            database.child("etat").setValue(1)
            database.child("volunteer").setValue(volunteer)
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            notice.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            finish()
            startActivity(Intent(this, ServicesListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        btnDone.setOnClickListener {
            tvState.text = "تم الإيصال"
            database.child("etat").setValue(2)
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnDone.visibility = View.GONE
            notice.visibility = View.GONE
            finish()
            startActivity(Intent(this, ServicesListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        btnCancel.setOnClickListener {
            tvState.text = "ملغي"
            tvState.setTextColor(Color.parseColor("#FF0000"))
            database.child("etat").setValue(-1)
            finish()
            startActivity(Intent(this, ServicesListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onContextItemSelected(item)
    }

}