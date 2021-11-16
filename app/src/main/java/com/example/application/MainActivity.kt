package com.example.application

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.application.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database
    private val myRef = database.getReference("messages").child("Kostya")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        auth = Firebase.auth
        sendMessage()
        readMassages(myRef)
        setUpActionBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.singOutMenuButton) {
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendMessage() {
        bindingClass.bSend.setOnClickListener {
            myRef.setValue(bindingClass.message.text.toString())
        }
    }

    private fun readMassages(dRef: DatabaseReference) {

        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bindingClass.apply {
                    chat.append("${snapshot.value.toString()} \n")
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setUpActionBar() {
        val actionBar = supportActionBar
        CoroutineScope(Dispatchers.Main).launch {

            val bMap = async(Dispatchers.IO) {
                Picasso.get().load(auth.currentUser?.photoUrl).get()
            }
            val icon = BitmapDrawable(resources, bMap.await())
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.setHomeAsUpIndicator(icon)
            actionBar?.title = auth.currentUser?.displayName
        }
    }
}
