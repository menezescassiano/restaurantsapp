package com.cassianomenezes.restaurantsapp.home.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cassianomenezes.restaurantsapp.R
import com.cassianomenezes.restaurantsapp.extension.bindingContentView
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.utils.JsonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        bindingContentView(R.layout.activity_main).apply {
            /*setVariable(BR.viewModel, viewModel)
            setVariable(BR.onTryAgainClick, View.OnClickListener { onTryAgainClick() })*/
        }
    }

    private fun getJson() {
        val jsonFileString = JsonUtil.getJsonDataFromAsset(applicationContext, "sample_android.json")
        jsonFileString?.let { Log.i("data", it) }

        val gson = Gson()
        val data = object : TypeToken<OverallData>(){}.type

        var restaurantsData: OverallData = gson.fromJson(jsonFileString, data)
        println(restaurantsData.restaurants.toString())
    }
}