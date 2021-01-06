package com.cassianomenezes.restaurantsapp.home.view.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cassianomenezes.restaurantsapp.BR
import com.cassianomenezes.restaurantsapp.BaseApplication
import com.cassianomenezes.restaurantsapp.R
import com.cassianomenezes.restaurantsapp.extension.bindingContentView
import com.cassianomenezes.restaurantsapp.extension.observe
import com.cassianomenezes.restaurantsapp.home.adapter.RestaurantListAdapter
import com.cassianomenezes.restaurantsapp.home.view.viewmodel.MainViewModel
import com.cassianomenezes.restaurantsapp.internal.StatusConstants
import com.cassianomenezes.restaurantsapp.internal.StatusConstants.*
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.model.Restaurant
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel{ parametersOf((application as BaseApplication).restaurantRepositoryImpl) }
    lateinit var restaurantsData: OverallData
    lateinit var listAdapter: RestaurantListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setSwipeListener()
    }

    private fun setupBinding() {
        bindingContentView(R.layout.activity_main).apply {
            setVariable(BR.viewModel, viewModel)
            setVariable(BR.onSearchClick, View.OnClickListener { onSearchClick() })
        }

        viewModel.apply {
            observe(restaurantData) {
                it?.let {
                    restaurantsData = it
                    handleList(it.restaurants)
                }
            }
            observe(listData) {
                it?.let {
                    setRecyclerView(it)
                    setSpinner()
                }
            }
            lifecycle.addObserver(this)
        }
    }

    private fun onSearchClick() {
        updateListAdapter(WORDS)
    }

    private fun setSwipeListener() {
        swipeLayout.run {
            setOnRefreshListener {
                updateListAdapter(INITIAL)
                isRefreshing = false
                viewModel.inputTextSearch.set("")
                spinner.setSelection(0)
            }
        }
    }

    private fun setRecyclerView(list: List<Restaurant>) {
        listAdapter = RestaurantListAdapter(list)

        recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        listAdapter.selectedRestaurant.observe(this@MainActivity, {
            viewModel.favRestaurant(it.name, it.added)
            list[list.indexOf(it)].added = !it.added
            listAdapter.notifyDataSetChanged()
        })
    }

    private fun setSpinner() {
        val orderingOptions = resources.getStringArray(R.array.ordering)
        spinner?.let {
            it.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, orderingOptions)
            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    when (position) {
                        1 -> updateListAdapter(OPEN)
                        2 -> updateListAdapter(CLOSED)
                        3 -> updateListAdapter(ORDER_AHEAD)
                        4 -> updateListAdapter(BEST_MATCH)
                        5 -> updateListAdapter(NEWEST)
                        6 -> updateListAdapter(RATING_AVERAGE)
                        7 -> updateListAdapter(DISTANCE)
                        8 -> updateListAdapter(POPULARITY)
                        9 -> updateListAdapter(AVERAGE_PRICE)
                        10 -> updateListAdapter(DELIVERY_COSTS)
                        11 -> updateListAdapter(MIN_COSTS)
                        else -> updateListAdapter(INITIAL)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // does nothing
                }
            }
        }
    }

    private fun updateListAdapter(status: StatusConstants) {
        recyclerView.apply {
            listAdapter.run {
                list = viewModel.getDesiredOrder(viewModel.inputTextSearch.get(), restaurantsData.restaurants, status)
                notifyDataSetChanged()
            }
        }
    }
}