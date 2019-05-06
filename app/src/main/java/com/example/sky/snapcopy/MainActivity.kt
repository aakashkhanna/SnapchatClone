package com.example.sky.snapcopy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    var emailEdit:EditText? =null
    var passcodeEdit: EditText?=null
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEdit=findViewById(R.id.emailText)
        passcodeEdit=findViewById(R.id.passcodeText)
        if(mAuth.currentUser!=null){
            logIn()
        }

    }
    fun submitClicked(view : View) {
        mAuth.signInWithEmailAndPassword(emailEdit?.text.toString(), passcodeEdit?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        logIn()

                    } else {
                        // If sign in fails, display a message to the user.
                        mAuth.createUserWithEmailAndPassword(emailEdit?.text.toString(), passcodeEdit?.text.toString())
                                .addOnCompleteListener(this) { task1 ->
                                    if (task1.isSuccessful) {
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task1.result.user.uid).child("email").setValue(emailEdit?.text.toString())
                                        logIn()
                                    } else {
                                        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                    }


                }
    }
     fun logIn(){
         Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
         val intent=Intent(this,SnapsActivity::class.java)
         startActivity(intent)
    }
}
