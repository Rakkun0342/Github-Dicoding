package com.ansel.githubdicoding.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ansel.githubdicoding.DetailActivity
import com.ansel.githubdicoding.R
import com.ansel.githubdicoding.entity.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_row_github_user.view.*
import java.util.*
import kotlin.collections.ArrayList

var userFilterList = ArrayList<User>()
lateinit var mcontext: Context

class ListUserAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(), Filterable {
    init {
        userFilterList = listUser
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_github_user, viewGroup, false)
        val sch = ListViewHolder(view)
        mcontext = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = userFilterList[position]
        Glide.with(holder.itemView.context)
                .load(data.avatar)
                .apply(RequestOptions().override(250, 250))
                .into(holder.imgAvatar)
        //holder.txtUsername.text = data.username
        holder.txtName.text = data.name
        holder.txtCompany.text = data.company
        holder.txtLocation.text = data.location
        holder.itemView.setOnClickListener {
            val dataUser = User(
                    data.username,
                    data.name,
                    data.avatar,
                    data.company,
                    data.location,
                    data.repository,
                    data.followers,
                    data.following,
                    data.isFav
            )
            val intentDetail = Intent(mcontext, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.UserList, dataUser)
            intentDetail.putExtra(DetailActivity.FavoriteList, dataUser)
            mcontext.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: User)
    }

    override fun getItemCount(): Int = userFilterList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: CircleImageView = itemView.img_item_photo
        //var txtUsername: TextView = itemView.im
        var txtName: TextView = itemView.tv_item_name
        var txtCompany: TextView = itemView.tv_item_company
        var txtLocation: TextView = itemView.tv_item_location
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()
                userFilterList = if (charSearch.isEmpty()) {
                    listUser
                } else {
                    val resultList = ArrayList<User>()
                    for (row in userFilterList) {
                        if ((row.username.toString().toLowerCase(Locale.ROOT)
                                        .contains(charSearch.toLowerCase(Locale.ROOT)))
                        ) {
                            resultList.add(
                                    User(
                                            row.username,
                                            row.name,
                                            row.avatar,
                                            row.company,
                                            row.location,
                                            row.repository,
                                            row.followers,
                                            row.following
                                    )
                            )
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = userFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                userFilterList = results.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }
}