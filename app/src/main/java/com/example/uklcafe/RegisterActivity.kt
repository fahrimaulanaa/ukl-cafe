package com.example.uklcafe

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    var et_email: EditText? = null
    var et_password: EditText? = null
    var progres_bar: ProgressBar? = null
    var setLogin: TextView ? = null
    lateinit var btn_register: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        et_email = findViewById(R.id.et_email_registrasi)
        et_password = findViewById(R.id.et_password_registrasi)
        btn_register = findViewById(R.id.btn_register)
        progres_bar = findViewById(R.id.progressBar)
        setLogin = findViewById(R.id.setLogin)
        btn_register.setOnClickListener(this)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val redirectLogin = Intent(this, LoginActivity::class.java)
            startActivity(redirectLogin)
        } else {
            Toast.makeText(this, "User Belum Login", Toast.LENGTH_SHORT).show()
        }

        setLogin?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun reload() {
        Toast.makeText(this, "User Sudah Login", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        progres_bar?.visibility = View.VISIBLE
        var email = et_email?.text.toString()
        var password = et_password?.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        progres_bar?.visibility = View.GONE
                        Log.d(TAG, "Account Created Successfully.")
                        val user = auth.currentUser
                    } else {
                        progres_bar?.visibility = View.GONE
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Account Creation Failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}