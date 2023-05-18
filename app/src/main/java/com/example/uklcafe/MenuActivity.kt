package com.example.uklcafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MenuActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var btn_tambah_menu: Button
    private lateinit var btn_daftar_menu: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        if(user == null){
            val redirectLogin = Intent(this, LoginActivity::class.java)
            startActivity(redirectLogin)
        }else{
            Toast.makeText(this, "User Belum Login", Toast.LENGTH_SHORT).show()
        }

        btn_tambah_menu = findViewById(R.id.btn_tambahMenu)
        btn_daftar_menu = findViewById(R.id.btn_listMenu)
        btn_tambah_menu.setOnClickListener(this)
        btn_daftar_menu.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_tambahMenu -> {
                val intent = Intent(this, TambahMenu::class.java)
                startActivity(intent)
            }
            R.id.btn_listMenu -> {
                val intent = Intent(this, DaftarMenu::class.java)
                startActivity(intent)
            }
        }
    }
}