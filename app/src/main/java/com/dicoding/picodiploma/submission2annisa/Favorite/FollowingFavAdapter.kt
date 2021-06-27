package com.dicoding.picodiploma.submission2annisa.Favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.submission2annisa.databinding.ItemRowAvatarBinding
import de.hdodenhof.circleimageview.CircleImageView

var listfavfollowing = ArrayList<FollowersData>()
class FollowingFavAdapter (listfollowing: ArrayList<FollowersData>): RecyclerView.Adapter<FollowingFavAdapter.FollowerViewHolder>(){
    lateinit var ccontext : Context

    init {
        listfavfollowing= listfollowing
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    inner class FollowerViewHolder(private var binding: ItemRowAvatarBinding) : RecyclerView.ViewHolder(binding.root) {

        var imgPhotoAvatar : CircleImageView =binding.imgPhotoAvatar
        var textName: TextView = binding.textNameavatar
        var textLocation: TextView = binding.textLocation
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowingFavAdapter.FollowerViewHolder {
        val binding = ItemRowAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ccontext = parent.context
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingFavAdapter.FollowerViewHolder, position: Int) {
        val dataa = listfavfollowing[position]
        Glide.with(holder.itemView.context)
            .load(dataa.avatar)
            .apply(RequestOptions().override(50, 50))
            .into(holder.imgPhotoAvatar)
        holder.textName.text = dataa.login
        holder.textLocation.text = dataa.location


    }

    override fun getItemCount(): Int = listfavfollowing.size
    interface OnItemClickCallback {
        fun onItemClicked(data: FollowersData)
    }


}