package com.example.sky.snapcopy

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*


class newSnapActivity : AppCompatActivity() {
    var newSnapImage:ImageView?=null
    var messageText: EditText?=null
    val imageName= UUID.randomUUID().toString()+".jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_snap)
        newSnapImage=findViewById(R.id.imageView)
        messageText=findViewById(R.id.message)
    }

    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }
    fun chooseImage(view: View){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getPhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selected = data!!.data
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selected)

                newSnapImage!!.setImageBitmap(bitmap)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun nextClicked(view:View){
        newSnapImage?.setDrawingCacheEnabled(true)
        newSnapImage?.buildDrawingCache()
        val bitmap = newSnapImage?.getDrawingCache()
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        FirebaseStorage.getInstance().getReference().child("images").child(imageName)

        val uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data)
        uploadTask.addOnFailureListener(OnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this,"Upload Failed",Toast.LENGTH_LONG).show()
        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

            val downloadUrl = taskSnapshot.downloadUrl
            val intent=Intent(this,SendToActivity::class.java)
            intent.putExtra("imageURL",downloadUrl.toString())
            intent.putExtra("imageName",imageName)
            intent.putExtra("message",messageText?.text.toString())
            startActivity(intent)
        })
    }
}
