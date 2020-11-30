package com.ansel.githubdicoding.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ansel.githubdicoding.CustomOnItemClickListener
import com.ansel.githubdicoding.DetailActivity
import com.ansel.githubdicoding.R
import com.ansel.githubdicoding.entity.Favorite
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_github_user.view.*
import java.util.ArrayList

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.NoteViewHolder>() {
    var listFavorite = ArrayList<Favorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_github_user, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fav: Favorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(fav.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(img_item_photo)
                tv_item_name.text = fav.name
                tv_item_company.text = fav.company.toString()
                tv_item_location.text = fav.location.toString()
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.PositionList, position)
                                intent.putExtra(DetailActivity.NoteList, fav)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )
            }
        }
    }
}