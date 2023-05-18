package com.example.uklcafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class TransaksiActivity : AppCompatActivity(), View.OnClickListener {

    var btn_tambah_transaksi: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        btn_tambah_transaksi = findViewById(R.id.btn_tambahTransaksi)
        btn_tambah_transaksi?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_tambahTransaksi -> {
                val intent = Intent(this, TambahTransaksiActivity::class.java)
                startActivity(intent)
            }
        }
    }

}