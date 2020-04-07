package com.jendouba.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_services_list.*
import java.util.*
import java.util.stream.Collector
import java.util.stream.Collectors

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

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.children.count() == 0) {
                    progressBar.visibility = View.GONE
                    servicesList.visibility = View.GONE
                    tvNoService.visibility = View.VISIBLE
                }
            }
        })
        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) { // Failed to read value

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if (dataSnapshot.children.count() == 0) {
                    progressBar.visibility = View.GONE
                    servicesList.visibility = View.GONE
                    tvNoService.visibility = View.VISIBLE
                } else {
                    val userKey = dataSnapshot.key
                    Log.d("children", "e")
                    var nb = 0
                    dataSnapshot.child("services").children.forEach {
                        val value = it.getValue(Service::class.java)!!
                        Log.d("value", value.adresse)
                        progressBar.visibility = View.GONE
                        tvNoService.visibility = View.GONE
                        servicesList.visibility = View.VISIBLE
                        // -1 canceled
                        if(value.etat != -1) {
                            nb = nb +1
                            servicesData.add(
                                0,
                                Service(
                                    cin = value.cin,
                                    user = value.user,
                                    adresse = value.adresse,
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
                    if(nb == 0) {
                        progressBar.visibility = View.GONE
                        servicesList.visibility = View.GONE
                        tvNoService.visibility = View.VISIBLE
                    }

                    servicesAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                for (i in 0 until servicesData.size) {
                    if (servicesData[i].databaseKey == dataSnapshot.key)
                        servicesData.removeAt(i)
                }
                servicesAdapter.notifyDataSetChanged()
                if(servicesData.size==0){
                    servicesList.visibility = View.GONE
                    tvNoService.visibility = View.VISIBLE
                }

            }
        })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onContextItemSelected(item)
    }
}
