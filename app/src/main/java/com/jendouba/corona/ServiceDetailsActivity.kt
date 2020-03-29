package com.jendouba.corona

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.jendouba.corona.R
import com.jendouba.corona.Service
import kotlinx.android.synthetic.main.activity_service_details.*
import kotlinx.android.synthetic.main.activity_service_details.tvServices
import kotlinx.android.synthetic.main.service_item.*

class ServiceDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val serviceData = intent.getSerializableExtra("service_data") as Service
        tvUserName.text = serviceData.user
        tvAdresse.text = serviceData.adresse
        tvTel.text = serviceData.tel
        tvServices.text = serviceData.service
        val volunteer = getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).getString("volunteer_email", "").toString()

        Log.e("etat: ", "${serviceData.etat}")

        if (serviceData.etat == 0) {
            tvState.text = "في إنتظار التأكيد"
            // pas encore confirmé
            btnDone.visibility = View.GONE
        } else if (serviceData.etat == 1) {
            // confirmé
            tvState.text = "مؤكد"
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            if(!serviceData.volunteer.equals(volunteer)) {
                btnDone.visibility = View.GONE
            } else {
                btnDone.visibility = View.VISIBLE
            }
        } else if (serviceData.etat == -1) {
            tvState.text = "ملغي"
            tvState.setTextColor(Color.parseColor("#FF0000"))
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnDone.visibility = View.GONE
        } else if (serviceData.etat == 2) {
            tvState.text = "تم الإيصال"
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnDone.visibility = View.GONE
        }

        val database = FirebaseDatabase.getInstance().reference.child(serviceData.userKey).child("services").child(serviceData.databaseKey)
        btnConfirm.setOnClickListener {
            tvState.text = "مؤكد"
            database.child("etat").setValue(1)
            database.child("volunteer").setValue(volunteer)
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            finish()
            startActivity(Intent(this, ServicesListActivity::class.java))
        }

        btnDone.setOnClickListener {
            tvState.text = "تم الإيصال"
            database.child("etat").setValue(2)
            btnConfirm.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnDone.visibility = View.GONE
            finish()
            startActivity(Intent(this, ServicesListActivity::class.java))
        }

        btnCancel.setOnClickListener {
            tvState.text = "ملغي"
            tvState.setTextColor(Color.parseColor("#FF0000"))
            database.child("etat").setValue(-1)
            finish()
            startActivity(Intent(this, ServicesListActivity::class.java))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onContextItemSelected(item)
    }

}
