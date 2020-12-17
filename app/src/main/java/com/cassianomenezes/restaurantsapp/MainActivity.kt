package com.cassianomenezes.restaurantsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.utils.JsonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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