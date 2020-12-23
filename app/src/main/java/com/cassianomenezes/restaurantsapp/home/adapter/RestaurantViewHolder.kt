package com.cassianomenezes.restaurantsapp.home.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cassianomenezes.restaurantsapp.model.Restaurant
import com.cassianomenezes.restaurantsapp.BR

class RestaurantViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Restaurant) {

        this.binding.apply {
            val context = this.root.context
            item.run {
                setVariable(BR.title, name)
                setVariable(BR.status, status)
            }
            executePendingBindings()
        }
    }
}