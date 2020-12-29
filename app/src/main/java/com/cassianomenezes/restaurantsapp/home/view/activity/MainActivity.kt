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
    }

    private fun setupBinding() {
        bindingContentView(R.layout.activity_main).apply {
            setVariable(BR.viewModel, viewModel)
            setVariable(BR.onSearchClick, View.OnClickListener { onSearchClick() })
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
                    setRecyclerView(it)
                    setSpinner()
                }
            }
        }
    }

    private fun onSearchClick() {
        setAdapter(viewModel.inputTextSearch.get(), WORDS)
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
                        1 -> setAdapter(viewModel.inputTextSearch.get(), OPEN)
                        2 -> setAdapter(viewModel.inputTextSearch.get(), CLOSED)
                        3 -> setAdapter(viewModel.inputTextSearch.get(), ORDER_AHEAD)
                        4 -> setAdapter(viewModel.inputTextSearch.get(), BEST_MATCH)
                        5 -> setAdapter(viewModel.inputTextSearch.get(), NEWEST)
                        6 -> setAdapter(viewModel.inputTextSearch.get(), RATING_AVERAGE)
                        7 -> setAdapter(viewModel.inputTextSearch.get(), DISTANCE)
                        8 -> setAdapter(viewModel.inputTextSearch.get(), POPULARITY)
                        9 -> setAdapter(viewModel.inputTextSearch.get(), AVERAGE_PRICE)
                        10 -> setAdapter(viewModel.inputTextSearch.get(), DELIVERY_COSTS)
                        11 -> setAdapter(viewModel.inputTextSearch.get(), MIN_COSTS)
                        else -> setAdapter(viewModel.inputTextSearch.get(), INITIAL)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // does nothing
                }
            }
        }
    }

    private fun setAdapter(word: String? = "", status: StatusConstants) {
        recyclerView.apply {
            listAdapter.run {
                list = viewModel.getDesiredOrder(word, restaurantsData.restaurants, status)
                notifyDataSetChanged()
            }
        }
    }
}