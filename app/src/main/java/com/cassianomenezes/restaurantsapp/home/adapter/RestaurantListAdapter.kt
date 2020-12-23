package com.cassianomenezes.restaurantsapp.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.cassianomenezes.restaurantsapp.R
import com.cassianomenezes.restaurantsapp.databinding.LayoutRestaurantListItemBinding
import com.cassianomenezes.restaurantsapp.model.Restaurant

class RestaurantListAdapter(var list: List<Restaurant>) : RecyclerView.Adapter<RestaurantViewHolder>() {

    lateinit var binding: LayoutRestaurantListItemBinding
    val selectedRestaurant: MutableLiveData<Restaurant> = MutableLiveData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false) as LayoutRestaurantListItemBinding
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = list[position]

        holder.apply {
            bind(item)
            itemView.setOnClickListener {
                selectedRestaurant.postValue(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.layout_restaurant_list_item
    }
}