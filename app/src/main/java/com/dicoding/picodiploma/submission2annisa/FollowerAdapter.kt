package com.dicoding.picodiploma.submission2annisa

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.submission2annisa.databinding.ItemRowAvatarBinding
import de.hdodenhof.circleimageview.CircleImageView

var listfollower = ArrayList<AvatarData>()
class FollowerAdapter(listfollowers: ArrayList<AvatarData>): RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>(){
    lateinit var ccontext : Context

    init {
        listfollower = listfollowers
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    inner class FollowerViewHolder(private var binding: ItemRowAvatarBinding) : RecyclerView.ViewHolder(binding.root) {

        var imgPhotoAvatar : CircleImageView =binding.imgPhotoAvatar
        var textUsername: TextView = binding.textUsername
        var textName: TextView = binding.textNameavatar
        var textLocation: TextView = binding.textLocation
        var textCompany: TextView = binding.textCompany
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): FollowerAdapter.FollowerViewHolder {
        val binding =
                ItemRowAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ccontext = parent.context
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerAdapter.FollowerViewHolder, position: Int) {
        val dataa = listfollower[position]
        Glide.with(holder.itemView.context)
                .load(dataa.photoavatar)
                .apply(RequestOptions().override(50, 50))
                .into(holder.imgPhotoAvatar)
        holder.textUsername.text = dataa.username
        holder.textName.text = dataa.nameavatar
        holder.textLocation.text = dataa.location
        holder.textCompany.text = dataa.company


    }

    override fun getItemCount(): Int = listfollower.size
    interface OnItemClickCallback {
        fun onItemClicked(data: AvatarData)
    }


}


