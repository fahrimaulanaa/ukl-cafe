package com.example.uklcafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var btn_transaksi: Button
    private lateinit var btn_user: Button
    private lateinit var btn_menu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        if(user == null){
            val redirectLogin = Intent(this, LoginActivity::class.java)
            startActivity(redirectLogin)
        }else{

        }

        btn_transaksi = findViewById(R.id.btn_transaksi)
        btn_user = findViewById(R.id.btn_user)
        btn_menu = findViewById(R.id.btn_menu)
        btn_transaksi.setOnClickListener(this)
        btn_user.setOnClickListener(this)
        btn_menu.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_transaksi -> {
                val intent = Intent(this, TransaksiActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_user -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_menu -> {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
        }
    }
}