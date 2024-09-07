package com.dicoding.submissionawalaplikasigithubuser.ui

import android.content.Context
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
import com.dicoding.submissionawalaplikasigithubuser.data.response.ApiResponse

class GithubAdapter(val context: Context, val datalist: ArrayList<ApiResponse>): RecyclerView.Adapter<GithubAdapter.MyViewHolder>() {
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tvphoto: ImageView = view.findViewById(R.id.img_item_photo)
        val tvnama: TextView = view.findViewById(R.id.tv_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itewview = layoutInflater.inflate(R.layout.item_user, parent, false)
        return MyViewHolder(itewview)
    }

    override fun getItemCount(): Int = datalist.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = datalist[position]
        Glide.with(holder.view)
            .load(result.avatar_url)
            .into(holder.tvphoto)
        holder.tvnama.text = result.login
        holder.view.setOnClickListener{
            val detail = Intent(holder.view.context, DetailActivity::class.java)
            detail.putExtra("key_nama", result.login)
            detail.putExtra("id", result.id)
            holder.view.context.startActivity(detail)
        }
    }

    fun setData(data: List<ApiResponse>){
        this.datalist.clear()
        this.datalist.addAll(data)
        notifyDataSetChanged()
    }
}


