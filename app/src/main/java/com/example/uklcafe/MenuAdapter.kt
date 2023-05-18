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

class MenuAdapter(private var menuList: List<Menu>) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgMenu: ImageView = itemView.findViewById(R.id.img_menu)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama)
        val tvDeskripsi: TextView = itemView.findViewById(R.id.tv_deskripsi)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
        val btnUbah: Button = itemView.findViewById(R.id.btn_ubah)
        val btnHapus: Button = itemView.findViewById(R.id.btn_hapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item_row, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentMenu = menuList[position]

        Glide.with(holder.itemView)
            .load(currentMenu.gambar)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imgMenu)

        holder.tvNama.text = currentMenu.nama.toString()
        holder.tvDeskripsi.text = currentMenu.deskripsi
        holder.tvHarga.text = currentMenu.harga

        holder.btnUbah.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UpdateMenuActivity::class.java)
            intent.putExtra("id", currentMenu.id)
            intent.putExtra("gambar", currentMenu.gambar)
            intent.putExtra("nama", currentMenu.nama)
            intent.putExtra("harga", currentMenu.harga)
            intent.putExtra("deskripsi", currentMenu.deskripsi)
            context.startActivity(intent)
        }

        holder.btnHapus.setOnClickListener{
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("menu").document(currentMenu.id)
            val img = menuList[position].nama
            docRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Berhasil Menghapus Menu", Toast.LENGTH_SHORT).show()
                    val storageRef = Firebase.storage.reference
                    val imgRef = storageRef.child("images/$img.jpg")
                    imgRef.delete()
                        .addOnSuccessListener {
                            Toast.makeText(holder.itemView.context, "Berhasil Menghapus Gambar", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(holder.itemView.context, "Gagal Menghapus Gambar", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "Gagal Menghapus Menu", Toast.LENGTH_SHORT).show()
                }
        }
    }


    override fun getItemCount(): Int {
        return menuList.size
    }

    fun setMenuList(menuList: List<Menu>) {
        this.menuList = menuList
        notifyDataSetChanged()
    }
}
