package com.example.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding
    private val database = Firebase.database
    private val myRef = database.getReference("messages").child("Kostya")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        sendMessage()
        readMassages(myRef)
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

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}
