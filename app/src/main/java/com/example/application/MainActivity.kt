package com.example.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application.databinding.ActivityMainBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


private lateinit var bindingClass: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        main()
    }

    private fun main() {
        val database = Firebase.database
        val myRef = database.getReference("messages").child("Kostya")

        myRef.setValue("Hello, World!")
    }
}
