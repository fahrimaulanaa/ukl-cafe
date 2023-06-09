package com.example.uklcafe

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class TambahMenu : AppCompatActivity() {

    private lateinit var iv_menu: ImageView
    private lateinit var et_nama_menu: EditText
    private lateinit var et_harga_menu: EditText
    private lateinit var et_deskripsi_menu: EditText
    private lateinit var btn_kirim: Button
    private lateinit var storage: FirebaseStorage

    // Firebase Firestore
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_menu)
        storage = Firebase.storage

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance()

        iv_menu = findViewById(R.id.iv_menu)
        et_nama_menu = findViewById(R.id.et_nama_menu)
        et_harga_menu = findViewById(R.id.et_harga_menu)
        et_deskripsi_menu = findViewById(R.id.et_deskripsi_menu)
        btn_kirim = findViewById(R.id.btn_kirim)

        iv_menu.setOnClickListener {
            selectImage()
        }

        btn_kirim.setOnClickListener {
            uploadImage()
        }
    }

    private fun selectImage() {
        val item = arrayOf<CharSequence>("Kamera", "Galeri")
        AlertDialog.Builder(this)
            .setTitle("Ambil Gambar Dari")
            .setItems(item) { dialog, which ->
                when (which) {
                    0 -> {
                        // Open camera
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, 100)
                    }
                    1 -> {
                        // Open gallery
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, 200)
                    }
                }
            }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Get image from camera
            val image = data?.extras?.get("data") as Bitmap
            iv_menu.setImageBitmap(image)
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            // Get image from gallery
            val uri = data?.data
            iv_menu.setImageURI(uri)
        }
    }

    private fun uploadImage() {
        // Upload image to Firebase Storage
        iv_menu.isDrawingCacheEnabled = true
        iv_menu.buildDrawingCache()
        val bitmap = (iv_menu.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${et_nama_menu.text}.jpg") // Use "nama" as the image file name
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this, "Upload Gagal", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // Get the download URL of the uploaded image
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageURL = uri.toString()

                // Save menu data to Firestore along with the image URL
                saveData(imageURL)
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal mendapatkan URL gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData(imageURL: String) {
        val menu = hashMapOf(
            "nama" to et_nama_menu.text.toString(),
            "harga" to et_harga_menu.text.toString(),
            "deskripsi" to et_deskripsi_menu.text.toString(),
            "gambar" to imageURL
        )

        db.collection("menu")
            .add(menu)
            .addOnSuccessListener { documentReference ->
                val menuId = documentReference.id // Get the AUTO-ID generated by Firestore
                // Add the AUTO-ID to the menu document
                db.collection("menu")
                    .document(menuId)
                    .update("id", menuId)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Data Gagal Disimpan", Toast.LENGTH_SHORT).show()
            }
    }


}
