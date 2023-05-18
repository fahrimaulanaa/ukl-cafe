package com.example.uklcafe

data class Transaksi(
    val id_transaksi: String = "",
    val nama_customer: String = "",
    val menu: String = "",
    val harga: String = "",
    val tanggal: String = "",
    val metode_pembayaran: String = ""
)
