package com.example.firebaseimplimentationkotlinproject

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(private var context: Context,private val profileList:ArrayList<Profile>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false);
        return MyViewHolder(itemView);
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val profile:Profile = profileList[position];
        var cal = Calendar.getInstance();
        //Months
        var months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul","Aug", "Sep", "Oct", "Nov", "Dec");
        //set time
        cal.time = profile.creationTime;
        var hour = cal.get(Calendar.HOUR)%12;
        if(hour==0){
            hour=12
        }
        var AM_PM = arrayOf("am","pm");
        holder.title.text = profile.title
        holder.caption.text = profile.caption

        holder.date.text = cal.get(Calendar.DAY_OF_MONTH).toString() + " " + months[cal.get(Calendar.MONTH)];
        holder.time.text ="at "+ hour + ":" + cal.get(Calendar.MINUTE) + AM_PM[cal.get(Calendar.AM_PM)];
        if(TextUtils.isEmpty(profile.photo)){
            holder.image.setImageResource(R.drawable.ic_note_icon)
        }
        else {
            Picasso.get().load(profile.photo).into(holder.image);
        }
        holder.itemView.setOnClickListener{
            var intent = Intent(context,NoteExtendActivity::class.java);
            intent.putExtra("title",profile.title);
            intent.putExtra("photo",profile.photo);
            intent.putExtra("caption",profile.caption);
            context.startActivity(intent);
        }
    }

    override fun getItemCount(): Int {
        return profileList.size
    }
    public class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var title: TextView = itemView.findViewById((R.id.listTitle));
        var caption: TextView = itemView.findViewById(R.id.listDetail);
        var image: ImageView = itemView.findViewById(R.id.listImage);
        var time:TextView = itemView.findViewById(R.id.listTime);
        var date:TextView = itemView.findViewById(R.id.listDate);
    }
}