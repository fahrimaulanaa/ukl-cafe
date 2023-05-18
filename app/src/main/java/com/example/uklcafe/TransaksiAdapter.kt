package com.example.uklcafe

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class TransaksiAdapter(var transaksiList: List<Transaksi>) :
    RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    inner class TransaksiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id_transaksi: TextView = itemView.findViewById(R.id.tv_idTransaksiFilled)
        var tanggal: TextView = itemView.findViewById(R.id.tv_tanggalTransaksiFilled)
        var namaCustomer: TextView = itemView.findViewById(R.id.tv_namaCustomerFilled)
        var menu: TextView = itemView.findViewById(R.id.tv_menuFilled)
        var harga: TextView = itemView.findViewById(R.id.tv_hargaFilled)
        var metodePembayaran: TextView = itemView.findViewById(R.id.tv_metodePembayaranFilled)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaksi_item_row, parent, false)
        return TransaksiViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val transaksi = transaksiList[position]
        holder.id_transaksi.text = transaksi.id_transaksi
        holder.tanggal.text = transaksi.nama_customer
        holder.namaCustomer.text = transaksi.menu
        holder.menu.text = transaksi.harga
        holder.harga.text = transaksi.tanggal
        holder.metodePembayaran.text = transaksi.metode_pembayaran
    }


    override fun getItemCount(): Int {
        return transaksiList.size
    }

}