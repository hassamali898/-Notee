package com.example.firebaseimplimentationkotlinproject

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseimplimentationkotlinproject.databinding.ActivityHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {
    //ViewBindding
    private lateinit var binding: ActivityHomeBinding;
    //RecyclerView
    private lateinit var recyclerView: RecyclerView;
    //array of profiles
    private lateinit var profileArrayList: ArrayList<Profile>
    //create adapter
    private lateinit var myAdapter: MyAdapter;
    //Firestore init
    private lateinit var db: FirebaseFirestore;
    //current user
    private lateinit var currentUser:FirebaseUser;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater);
        setContentView(binding.root)
        binding.recyclerView.adapter
        currentUser = FirebaseAuth.getInstance().currentUser!!
        binding.header.logoutBTN.setOnClickListener {
            signOut();
        }
        binding.fab.setOnClickListener {
            startActivity(Intent(this,AddNoteActivity::class.java))
        }
        recyclerView = binding.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true);
        profileArrayList = arrayListOf();
        myAdapter = MyAdapter(this,profileArrayList);
        recyclerView.adapter = myAdapter;
        getData(this);
    }
    private fun signOut(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out your account!")
            .setPositiveButton("Yes"
            ) { p0, p1 ->
                FirebaseAuth.getInstance().signOut();
                startActivity(Intent(this,LoginActivity::class.java));
                finish();
            }
            .setNegativeButton("No"
            ) { p0, p1 ->
                Toast.makeText(this,"SignOut operation canceled.",Toast.LENGTH_SHORT).show();
            }.show()
    }
    //get data from firestore
    private fun getData(context: Context) {
        var currentUser = FirebaseAuth.getInstance().currentUser;
        db = FirebaseFirestore.getInstance();
        db.collection("notes").whereEqualTo("uid",currentUser!!.uid).addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error!=null){
                    Log.e("FireStore Error ",error.message.toString());
                    return
                }
                for(dc:DocumentChange in value?.documentChanges!!){
                    if(dc.type==DocumentChange.Type.ADDED){
                        profileArrayList.add(dc.document.toObject(Profile::class.java));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        })
    }
}