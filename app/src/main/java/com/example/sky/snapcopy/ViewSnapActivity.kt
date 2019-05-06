package com.example.sky.snapcopy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class ViewSnapActivity : AppCompatActivity() {
    var messageView:TextView?=null
    var snapImageView: ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)
        messageView=findViewById(R.id.messageView)
        snapImageView=findViewById(R.id.snapView)

    }
}
