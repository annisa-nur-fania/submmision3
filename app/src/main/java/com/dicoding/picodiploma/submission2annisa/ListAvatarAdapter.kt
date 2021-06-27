package com.dicoding.picodiploma.submission2annisa

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.submission2annisa.databinding.ItemRowAvatarBinding
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList


var filterlist = ArrayList<AvatarData>()
class ListAvatarAdapter (private var listavatar: ArrayList<AvatarData>): RecyclerView.Adapter<ListAvatarAdapter.ListViewHolder>(), Filterable {
    init {
        filterlist = listavatar

    }
    lateinit var ccontext: Context

    fun setData(items: ArrayList<AvatarData>) {
        listavatar.clear()
        listavatar.addAll(items)
    }

    private var onItemClickCallback: OnItemClickCallback? = null


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=onItemClickCallback
    }

    inner class ListViewHolder(private val binding: ItemRowAvatarBinding) : RecyclerView.ViewHolder(binding.root) {
        var imgPhotoAvatar :CircleImageView=binding.imgPhotoAvatar
        var textUsername: TextView= binding.textUsername
        var textName: TextView= binding.textNameavatar
        var textLocation: TextView= binding.textLocation
        var textCompany: TextView= binding.textCompany


    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AvatarData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ccontext = parent.context
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var dataavt = filterlist[position]
           Glide.with(holder.itemView.context)
                        .load(dataavt.photoavatar)
                        .apply(RequestOptions().override(50, 50))
                        .into(holder.imgPhotoAvatar)
                holder.textUsername.text = dataavt.username
                holder.textName.text = dataavt.nameavatar
                holder.textLocation.text = dataavt.location
                holder.textCompany.text = dataavt.company

                holder.itemView.setOnClickListener {
                    val data = AvatarData(
                            dataavt.photoavatar,
                            dataavt.username,
                            dataavt.nameavatar,
                            dataavt.location,
                            dataavt.company,
                            dataavt.repository,
                            dataavt.follower,
                            dataavt.following
                    )
                    val intentd = Intent(ccontext, AvatarDetail::class.java)
                    intentd.putExtra(AvatarDetail.EXTRA_DATA, data)
                    ccontext.startActivity(intentd)
                }
    }

    override fun getItemCount(): Int = listavatar.size
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val Searchuser = constraint.toString()
                filterlist = if (Searchuser.isEmpty()) {
                    listavatar
                } else {
                    val resultList = ArrayList<AvatarData>()
                    for (item in filterlist) {
                        if ((item.username.toString().toLowerCase(Locale.ROOT).contains(Searchuser.toLowerCase(Locale.ROOT)))
                        ) {
                            resultList.add(
                                    AvatarData(
                                            item.photoavatar,
                                            item.username,
                                            item.nameavatar,
                                            item.location,
                                            item.company,
                                            item.follower,
                                            item.following,
                                            item.repository
                                    )
                            )
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterlist
                return filterResults
            }
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filterlist = results.values as ArrayList<AvatarData>
                notifyDataSetChanged()
            }
        }

    }
}

