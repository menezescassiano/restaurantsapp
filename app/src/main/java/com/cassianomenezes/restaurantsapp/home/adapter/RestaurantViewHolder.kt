package com.cassianomenezes.restaurantsapp.home.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cassianomenezes.restaurantsapp.BR
import com.cassianomenezes.restaurantsapp.R
import com.cassianomenezes.restaurantsapp.model.Restaurant
import com.cassianomenezes.restaurantsapp.utils.DrawableUtils

class RestaurantViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    val context: Context = binding.root.context

    fun bind(item: Restaurant) {

        this.binding.apply {
            item.run {
                setVariable(BR.title, name)
                setVariable(BR.status, status)
                setIcon(item, item.added)
            }
            executePendingBindings()
        }
    }

    private fun setIcon(item: Restaurant, contains: Boolean = false) {
        this.binding.apply {
            item.run {
                when {
                    contains -> setVariable(BR.favIcon, DrawableUtils.getDrawable(context, R.drawable.ic_fav))
                    else -> setVariable(BR.favIcon, DrawableUtils.getDrawable(context, R.drawable.ic_unfav))
                }
            }
        }
    }
}