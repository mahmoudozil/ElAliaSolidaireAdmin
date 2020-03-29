package com.jendouba.corona

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.ndroid.admin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //supportActionBar?.hide()

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
