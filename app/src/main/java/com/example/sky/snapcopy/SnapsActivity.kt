package com.example.sky.snapcopy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class SnapsActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    var snapsListView:ListView?=null
    var emails:ArrayList<String> = ArrayList()
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.snap){
            val intent= Intent(this,newSnapActivity::class.java)
            startActivity(intent)
        }
        else if(item?.itemId==R.id.logout){
            mAuth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        snapsListView=findViewById(R.id.snapsView)

        val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        snapsListView?.adapter=adapter
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid).child("snaps").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                emails.add(p0?.child("from")?.value as String)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {}
        })
        snapsListView?.setOnItemClickListener { adapterView, view, i, l ->

        }
    }


}
