package com.jendouba.volunteer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sharedPreferences = getSharedPreferences("volunteer_data", Context.MODE_PRIVATE)
        //supportActionBar?.hide()
        val spinner: Spinner = findViewById(R.id.cities)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.cities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        var nb = 0
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                var cityPos = sharedPreferences.getString("cityPos", "0").toString()
                parent?.setSelection(Integer.valueOf(cityPos))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                nb += 1

                if(nb > 1) {
                    val editor =  sharedPreferences.edit()
                    editor.putString("city", parent?.getItemAtPosition(pos).toString())
                    editor.putString("cityPos", pos.toString())
                    editor.apply()

                }
                var cityPos = sharedPreferences.getString("cityPos", "0").toString()
                parent?.setSelection(Integer.valueOf(cityPos))
            }
        }

        val email = getSharedPreferences(
            "volunteer_data",
            Context.MODE_PRIVATE
        ).getString("volunteer_email", "").toString()

        welcome.setText(" Welcome " + email.subSequence(0,email.lastIndexOf("@")))

        btnMyServices.setOnClickListener {
            startActivity(Intent(this, ServicesListActivity::class.java))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.itemLogout) {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            getSharedPreferences("volunteer_data", Context.MODE_PRIVATE).edit().clear().apply()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }


}
