package com.jendouba.volunteer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_services_list.*
import java.lang.Thread.sleep

class ServicesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var servicesData = arrayListOf<Service>()

        val servicesAdapter = ServiceAdapter(this)
        servicesAdapter.setData(servicesData)
        servicesList.adapter = servicesAdapter
        val database = FirebaseDatabase.getInstance().reference

        progressBar.visibility = View.VISIBLE
        servicesList.visibility = View.GONE
        tvNoService.visibility = View.VISIBLE
        tvNoService.setText("لا يوجد أي طلب \n\nفي معتمدية\n\n"+ getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).getString("city", "جندوبة").toString())


        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) { // Failed to read value

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("hey changed", "change")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                progressBar.visibility = View.GONE
                if (dataSnapshot.children.count() != 0 && !containsOnlyCancelledOrders(dataSnapshot)) {
                    progressBar.visibility = View.GONE
                    tvNoService.visibility = View.GONE
                    servicesList.visibility = View.VISIBLE
                    val userKey = dataSnapshot.key
                    dataSnapshot.child("services").children.forEach {
                        val value = it.getValue(Service::class.java)!!
                        Log.d("city: ", value.city)
                        Log.d("city shared : ", getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).getString("city", "جندوبة").toString())
                        val x = (value.city == getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).getString("city", "جندوبة").toString())
                        Log.d("yet3ada? :", x.toString())
                        Log.d("etat? :", value.etat.toString())
                        // -1 canceled
                        if(value.etat != -1 && value.city == getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).getString("city", "جندوبة").toString()) {
                            Log.d("hello" ,"inside")

                            servicesData.add(
                                0,
                                Service(
                                    cin = value.cin,
                                    user = value.user,
                                    adresse = value.adresse,
                                    city = value.city,
                                    service = value.service,
                                    tel = value.tel,
                                    dateDemande = value.dateDemande,
                                    databaseKey = it.key!!,
                                    etat = value.etat,
                                    userKey = userKey!!,
                                    volunteer = value.volunteer
                                )
                            )
                        }

                    }
                    servicesAdapter.notifyDataSetChanged()

                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                for (i in 0 until servicesData.size) {
                    if (servicesData[i].databaseKey == dataSnapshot.key)
                        servicesData.removeAt(i)
                }
                if(servicesData.size==0){
                    servicesList.visibility = View.GONE
                    tvNoService.visibility = View.VISIBLE
                    tvNoService.setText("لا يوجد أي طلب \n\nفي معتمدية\n\n"+ getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).getString("city", "جندوبة").toString())
                }
                servicesAdapter.notifyDataSetChanged()

            }
        })

    }

    private fun containsOnlyCancelledOrders(dataSnapshot: DataSnapshot): Boolean {
        dataSnapshot.child("services").children.forEach {
            val value = it.getValue(Service::class.java)!!
            if(value.etat != -1 && value.city == getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).getString("city", "جندوبة").toString()) {
                return false;
            }
        }
        return true;
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onContextItemSelected(item)
    }
}
