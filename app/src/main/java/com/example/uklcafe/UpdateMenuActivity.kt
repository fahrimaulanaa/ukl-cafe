package com.example.uklcafe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class UpdateMenuActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etNama: EditText
    private lateinit var etHarga: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnUpdate: Button
    private lateinit var imgMenu: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private var menuId: String? = null
    private var oldImageName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_menu)

        db = FirebaseFirestore.getInstance()

        etNama = findViewById(R.id.editTextNama)
        etHarga = findViewById(R.id.editTextHarga)
        etDeskripsi = findViewById(R.id.editTextDeskripsi)
        btnUpdate = findViewById(R.id.buttonUpdate)
        imgMenu = findViewById(R.id.imageViewMenu)

        menuId = intent.getStringExtra("id")
        oldImageName = intent.getStringExtra("gambar")
        val menuName = intent.getStringExtra("nama")
        val menuPrice = intent.getStringExtra("harga")
        val menuDescription = intent.getStringExtra("deskripsi")

        // Mengisi nilai awal pada EditText dengan data menu yang dikirimkan
        etNama.setText(menuName)
        etHarga.setText(menuPrice)
        etDeskripsi.setText(menuDescription)

        // Menampilkan gambar menu
        Glide.with(this)
            .load(oldImageName)
            .into(imgMenu)

        imgMenu.setOnClickListener {
            // Memilih gambar dari galeri
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnUpdate.setOnClickListener {
            // Mendapatkan data dari EditText
            val namaMenuBaru = etNama.text.toString().trim()
            val harga = etHarga.text.toString().trim()
            val deskripsi = etDeskripsi.text.toString().trim()

            if (menuId != null) {
                // Memperbarui data menu di Firebase Firestore
                db.collection("menu")
                    .document(menuId!!)
                    .update(
                        mapOf(
                            "nama" to namaMenuBaru,
                            "harga" to harga,
                            "deskripsi" to deskripsi
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "Menu berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Menu gagal diperbarui", Toast.LENGTH_SHORT).show()
                    }

                // Mengganti gambar menu di Firebase Storage
                val selectedImage: Uri? = getImageUri()
                if (selectedImage != null) {
                    updateMenuImageInStorage(selectedImage)
                }

                // Menutup activity dan kembali ke halaman sebelumnya
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            // Menampilkan gambar terpilih pada ImageView
            Glide.with(this)
                .load(selectedImage)
                .into(imgMenu)
        }
    }

    private fun getImageUri(): Uri? {
        return try {
            val drawable = imgMenu.drawable
            val bitmap = (drawable as BitmapDrawable).bitmap
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String =
                MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
            Uri.parse(path)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateMenuImageInStorage(imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("menu_images/$menuId.jpg") // Menggunakan ID menu sebagai nama gambar

        // Upload the new image to Firebase Storage
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            // Continue with the task to get the download URL
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                // Update the menu document in Firestore with the new image URL
                if (menuId != null) {
                    db.collection("menu")
                        .document(menuId!!)
                        .update("gambar", downloadUri.toString())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Gambar menu berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            // Hapus gambar lama setelah diperbarui
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal memperbarui gambar menu", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                // Handle failures
                Toast.makeText(this, "Gagal mengupload gambar menu", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
