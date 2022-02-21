package com.example.firebaseimplimentationkotlinproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.firebaseimplimentationkotlinproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivitySignUpBinding;
    //Firebase auth init
    private lateinit var firebaseAuth: FirebaseAuth;

    private var username="";
    private var email = "";
    private var password = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater);
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.signUpBTN.setOnClickListener {
            validateAndSignUp()
        }
        binding.haveAccount.setOnClickListener {
            finish();
        }
    }

    private fun validateAndSignUp() {
        username = binding.usernameET.text.toString().trim();
        email = binding.emailET.text.toString().trim();
        password = binding.passwordET.text.toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailET.error = "Bad Email Format"
        }
        else if(TextUtils.isEmpty(username)){
            binding.usernameET.error = "Username is required"
        }
        else if(password.length<8){
            binding.passwordET.error = "password must be atleast 8 characters"
        }
        else{
            signUp();
        }
    }

    private fun signUp() {
        //disabling SignUp button and show indicator
        binding.signUpBTN.isEnabled = false;
        binding.haveAccount.isEnabled=false;
        binding.signUpBTN.text = "";
        binding.loadingIndecator.visibility = View.VISIBLE;
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            binding.loadingIndecator.visibility = View.INVISIBLE;
            startActivity(Intent(this, HomeActivity::class.java))
            finish();
        }.addOnFailureListener{ e->
            //enabling SignUp button and hide indicator
            binding.signUpBTN.isEnabled = true;
            binding.haveAccount.isEnabled = true;
            binding.signUpBTN.text = "Login";
            binding.loadingIndecator.visibility =View.INVISIBLE;

            Toast.makeText(this,"SignUp Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}