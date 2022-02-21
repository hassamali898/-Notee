package com.example.firebaseimplimentationkotlinproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.firebaseimplimentationkotlinproject.databinding.ActivityNoteExtendBinding
import com.squareup.picasso.Picasso

class NoteExtendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteExtendBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteExtendBinding.inflate(layoutInflater);
        setContentView(binding.root);
        var bundle: Bundle?= intent.extras;
        var title = bundle!!.getString("title");
        var caption = bundle!!.getString("caption");
        var photo = bundle!!.getString("photo")
        binding.titleText.text = title;
        binding.captionText.text = caption;
        if(TextUtils.isEmpty((photo))){
            binding.imageView.setImageResource(R.drawable.ic_note_icon);
        }
        else{
            Picasso.get().load(photo).into(binding.imageView);
        }
        binding.backBTN.setOnClickListener{
            finish();
        }
    }

}