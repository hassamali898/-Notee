package com.example.firebaseimplimentationkotlinproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.firebaseimplimentationkotlinproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityLoginBinding
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth;

    private var email = "";
    private var password = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance();
        checkAuth()
        //handle click --> open Home Activity
        binding.loginBTN.setOnClickListener {
            validateAndLogin()
        }
        //handle click --> open SignUp Activity
        binding.noAccountBTN.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
    //check if user already logged in
    private fun checkAuth(){
        var firebaseUser = firebaseAuth.currentUser;
        if(firebaseUser!=null){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
    private fun validateAndLogin(){
        email = binding.emailET.text.toString().trim();
        password = binding.passwordET.text.toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailET.error= "invalid Email"
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordET.error = "password is required"
        }
        else{
            login()
        }
    }

    private fun login() {
        //disabling login button and show indicator
        binding.loginBTN.isEnabled = false;
        binding.noAccountBTN.isEnabled=false;
        binding.loginBTN.text = "";
        binding.loadingIndecator.visibility =View.VISIBLE;
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            //successfully login
            binding.loadingIndecator.visibility =View.INVISIBLE;
            startActivity(Intent(this, HomeActivity::class.java))
            finish();
        }.addOnFailureListener { e->
            //enabling login button and hide indicator
            binding.loginBTN.isEnabled = true;
            binding.noAccountBTN.isEnabled = true;
            binding.loginBTN.text = "Login";
            binding.loadingIndecator.visibility =View.INVISIBLE;

            Toast.makeText(this,"Login Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
        }

    }
}