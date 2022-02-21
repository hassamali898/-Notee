package com.example.firebaseimplimentationkotlinproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.firebaseimplimentationkotlinproject.databinding.ActivityAddNoteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.security.Permission
import java.util.*

@Suppress("DEPRECATION")
class AddNoteActivity : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivityAddNoteBinding;
    private lateinit var ImageUri:Uri;
    private lateinit var firestore: FirebaseFirestore;
    private lateinit var currentUser: FirebaseUser
    companion object{
        var IMAGE_REQUEST_CODE =100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater);
        setContentView(binding.root);
        firestore= FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().currentUser!!
        binding.header.saveBTN.setOnClickListener {
            onSave();
        }
        binding.header.discartBTN.setOnClickListener {
            onDiscard();
        }
        binding.addImage.setOnClickListener{
           requestPermission();
        }
    }

    override fun onBackPressed() {
        onDiscard();
    }
    //select Image from gallery
    private fun selectImage() {
        var intent = Intent(Intent.ACTION_GET_CONTENT);
        intent.type = "images/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }
    //request permission if required
    private fun requestPermission(){
        //if the sdk version is greater then marshmallow
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                var permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions,1001)
            }
            else{
                selectImage();
            }
        }
        else{
            selectImage();
        }
    }

    //save note into firebase
    private fun onSave() {
        var title = binding.titleET.text.toString().trim();
        var caption = binding.addCaption.text.toString().trim();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this,"title is required",Toast.LENGTH_SHORT).show();
            return;
        }

        var date = Calendar.getInstance().time;
        var user = Profile(currentUser.uid,title,caption,"",date);
        var db = firestore.collection("notes");
        db.document().set(user).addOnSuccessListener {
            Toast.makeText(this,"Document added successfully",Toast.LENGTH_SHORT).show();
            finish();
        }.addOnFailureListener{e->
            Toast.makeText(this,"Something went wrong to add a note please check you internet connection...",Toast.LENGTH_SHORT).show();
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== IMAGE_REQUEST_CODE && resultCode== RESULT_OK){
            ImageUri = data?.data!!;
            binding.selectedImage.setImageURI(ImageUri);
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1001->{
                if(grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    selectImage();
                }
                else{
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //if user don't want to save note
    private fun onDiscard(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Discard")
            .setMessage("Are you sure you don't want to add this note")
            .setPositiveButton("Yes"
            ) { p0, p1 ->
                finish();
            }
            .setNegativeButton("No"
            ) { p0, p1 ->
                Toast.makeText(this,"Discart operation canceled.", Toast.LENGTH_SHORT).show();
            }.show()
    }
}