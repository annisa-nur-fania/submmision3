package com.dicoding.picodiploma.consummerapp.Favorite

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.consummerapp.CustomeOnItemClickListener
import com.dicoding.picodiploma.consummerapp.databinding.ItemRowAvatarBinding


class FavoriteAdapter (private val activity: Activity) :
        RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

    var item = ArrayList<FavoriteData>()
        set(itemfav) {
            if (itemfav.size > 0) {
                this.item.clear()
            }
            this.item.addAll(itemfav)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val binding = ItemRowAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bind(item[position])
    }

    inner class FavoriteHolder(itemView: ItemRowAvatarBinding) :
            RecyclerView.ViewHolder(itemView.root) {
        private val binding = ItemRowAvatarBinding.bind(itemView.root)
        fun bind(data: FavoriteData) {
            with(itemView) {
                Glide.with(context)
                        .load(data.avatar)
                        .apply(RequestOptions().override(50, 50))
                        .into(binding.imgPhotoAvatar)

                binding.textLocation.text = data.location.toString()
                binding.textUsername.text = data.username.toString()
                binding.layout.setOnClickListener(
                    CustomeOnItemClickListener(adapterPosition,
                                object : CustomeOnItemClickListener.OnItemClickCallback{
                                    override fun onItemClicked(view: View, position: Int) {
                                        val intent = Intent(context, FavDetailActivity::class.java)
                                        intent.putExtra(FavDetailActivity.EXTRA_POSITION, position)
                                        intent.putExtra(FavDetailActivity.EXTRA_DATA, data)
                                        activity.startActivity(intent)
                                    }
                                })
                )
            }
        }
    }
    fun removeItem(position: Int) {
        this.item.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.item.size)
    }
}