package com.cassianomenezes.restaurantsapp.repository

import android.content.Context
import android.util.Log
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.utils.JsonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataRepository(private val context: Context) {

    fun getRestaurants(): OverallData = getJson()

    private fun getJson(): OverallData {
        val jsonFileString = JsonUtil.getJsonDataFromAsset(context, "sample_android.json")
        jsonFileString?.let { Log.i("data", it) }

        val data = object : TypeToken<OverallData>(){}.type

        return Gson().fromJson(jsonFileString, data)
    }

}