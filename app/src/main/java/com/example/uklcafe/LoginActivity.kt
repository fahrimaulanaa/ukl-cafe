package com.example.uklcafe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBarLogin)

        btnLogin.setOnClickListener(this)

        if (auth.currentUser != null) {
            // Pengguna sudah login sebelumnya
            navigateToMainScreen()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
                } else {
                    progressBar.visibility = View.VISIBLE
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            progressBar.visibility = View.GONE
                            if (task.isSuccessful) {
                                // Login berhasil
                                navigateToMainScreen()
                            } else {
                                // Login gagal
                                progressBar.visibility = View.GONE
                                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity()
    }

}
