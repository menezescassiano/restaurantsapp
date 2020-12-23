package com.cassianomenezes.restaurantsapp.home.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cassianomenezes.restaurantsapp.BR
import com.cassianomenezes.restaurantsapp.BaseApplication
import com.cassianomenezes.restaurantsapp.R
import com.cassianomenezes.restaurantsapp.extension.bindingContentView
import com.cassianomenezes.restaurantsapp.extension.observe
import com.cassianomenezes.restaurantsapp.home.adapter.RestaurantListAdapter
import com.cassianomenezes.restaurantsapp.home.view.viewmodel.MainViewModel
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.model.Restaurant
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel{ parametersOf((application as BaseApplication).restaurantRepositoryImpl) }
    lateinit var restaurantsData: OverallData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        bindingContentView(R.layout.activity_main).apply {
            setVariable(BR.viewModel, viewModel)
            //setVariable(BR.onTryAgainClick, View.OnClickListener { onTryAgainClick() })
        }

        viewModel.apply {
            getData()
            observe(restaurantData) {
                it?.let {
                    restaurantsData = it
                    handleList(it.restaurants)
                }
            }
            observe(listData) {
                it?.let {
                    setRecyclerView()
                }
            }
        }
    }


    private fun setRecyclerView() {
        val allList = arrayListOf<Restaurant>()
        restaurantsData.restaurants.run {
            val openList = this.filter { it.status == "open" }
            val closedList = this.filter { it.status == "closed" }
            val orderAhead = this.filter { it.status == "order ahead" }
            allList.run {
                addAll(openList)
                addAll(orderAhead)
                addAll(closedList)
            }
        }
        val listAdapter = RestaurantListAdapter(allList)

        recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        listAdapter.selectedRestaurant.observe(this@MainActivity, {
            viewModel.favRestaurant(it.name, it.added)
            allList[allList.indexOf(it)].added = !it.added
            listAdapter.notifyDataSetChanged()
        })
    }
}