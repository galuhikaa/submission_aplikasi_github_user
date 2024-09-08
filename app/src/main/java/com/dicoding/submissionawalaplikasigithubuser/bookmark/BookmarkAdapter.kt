package com.dicoding.submissionawalaplikasigithubuser.bookmark

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionawalaplikasigithubuser.DetailActivity
import com.dicoding.submissionawalaplikasigithubuser.R

class BookmarkAdapter : RecyclerView.Adapter<BookmarkAdapter.UserViewHolder>(){
    private var data = mutableListOf<EntityUser>()

    class UserViewHolder(ItemView : View) : RecyclerView.ViewHolder(ItemView){
        val tvphoto: ImageView = ItemView.findViewById(R.id.img_item_photo)
        val tvnama: TextView = ItemView.findViewById(R.id.tv_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itewview = layoutInflater.inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itewview)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = data[position]

        Glide.with(holder.itemView)
            .load(user.avatar_url)
            .into(holder.tvphoto)
        holder.tvnama.text = user.login

        holder.itemView.setOnClickListener{
            val detail = Intent(holder.itemView.context, DetailActivity::class.java)
            detail.putExtra("key_nama", user.login)
            detail.putExtra("id", user.id)
            holder.itemView.context.startActivity(detail)
        }
    }

    fun setData(list: List<EntityUser>){
        data.apply {
            clear()
            addAll(list)
        }
    }
}