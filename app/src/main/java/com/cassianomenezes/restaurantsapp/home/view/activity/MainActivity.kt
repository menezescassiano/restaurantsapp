package com.cassianomenezes.restaurantsapp.home.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cassianomenezes.restaurantsapp.R
import com.cassianomenezes.restaurantsapp.extension.bindingContentView
import com.cassianomenezes.restaurantsapp.extension.showToast
import com.cassianomenezes.restaurantsapp.home.adapter.RestaurantListAdapter
import com.cassianomenezes.restaurantsapp.home.view.viewmodel.MainViewModel
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.model.Restaurant
import com.cassianomenezes.restaurantsapp.utils.JsonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    lateinit var restaurantsData: OverallData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        bindingContentView(R.layout.activity_main).apply {
            /*setVariable(BR.viewModel, viewModel)
            setVariable(BR.onTryAgainClick, View.OnClickListener { onTryAgainClick() })*/
        }
        getJson()
        setRecyclerView()
    }

    private fun getJson() {
        val jsonFileString = JsonUtil.getJsonDataFromAsset(applicationContext, "sample_android.json")
        jsonFileString?.let { Log.i("data", it) }

        val gson = Gson()
        val data = object : TypeToken<OverallData>(){}.type

        restaurantsData = gson.fromJson(jsonFileString, data)
        println(restaurantsData.restaurants.toString())
    }

    private fun setRecyclerView() {
        val listAdapter = RestaurantListAdapter(restaurantsData.restaurants as ArrayList<Restaurant>)

        recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }

        listAdapter.selectedRestaurant.observe(this@MainActivity, {
            showToast(it.status)
            /*Intent(this, RecipeDetailActivity::class.java).apply {
                this.putExtra(BUNDLE_RECIPE, it)
                startActivity(this)
            }*/
        })
    }
}