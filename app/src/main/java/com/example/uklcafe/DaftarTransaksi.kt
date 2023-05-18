package com.example.uklcafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore

class DaftarTransaksi : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    lateinit var transaksiAdapter: TransaksiAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_transaksi)

        recyclerView = findViewById(R.id.rv_transaksi)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)

        recyclerView.layoutManager = LinearLayoutManager(this)
        transaksiAdapter = TransaksiAdapter(ArrayList()) // Menggunakan ArrayList kosong awal
        recyclerView.adapter = transaksiAdapter

        getDataFromFirestore() // Memanggil fungsi untuk mengambil data transaksi dari Firestore

        swipeRefreshLayout.setOnRefreshListener {
            getDataFromFirestore() // Memanggil fungsi kembali saat melakukan refresh dengan menggesek layar ke bawah
        }
    }

    private fun getDataFromFirestore() {
        // Mengambil data transaksi dari Firestore
        db.collection("transaksi")
            .get()
            .addOnSuccessListener { result ->
                val transaksiList = ArrayList<Transaksi>()
                for (document in result) {
                    val idTransaksi = document.getString("id_transaksi") ?: ""
                    val tanggal = document.getString("tanggal") ?: ""
                    val namaCustomer = document.getString("nama_customer") ?: ""
                    val menu = document.getString("menu") ?: ""
                    val harga = document.getString("harga") ?: ""
                    val metodePembayaran = document.getString("metode_pembayaran") ?: ""

                    val transaksi = Transaksi(
                        idTransaksi,
                        tanggal,
                        namaCustomer,
                        menu,
                        harga,
                        metodePembayaran
                    )
                    transaksiList.add(transaksi)
                }

                transaksiAdapter.transaksiList = transaksiList // Mengupdate data transaksi pada adapter
                transaksiAdapter.notifyDataSetChanged() // Memberi tahu adapter bahwa data telah berubah

                swipeRefreshLayout.isRefreshing = false // Menghentikan animasi refresh
            }
            .addOnFailureListener { exception ->
                // Menangani kesalahan saat mengambil data dari Firestore
                // Anda dapat menambahkan tindakan yang sesuai, seperti menampilkan pesan kesalahan
                swipeRefreshLayout.isRefreshing = false // Menghentikan animasi refresh
            }
    }
}
