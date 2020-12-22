package com.cassianomenezes.restaurantsapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.cassianomenezes.restaurantsapp.database.AppDatabase
import com.cassianomenezes.restaurantsapp.database.RestaurantRepository
import com.cassianomenezes.restaurantsapp.database.RestaurantRepositoryImpl
import com.cassianomenezes.restaurantsapp.di.loadKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    lateinit var restaurantRepositoryImpl: RestaurantRepository
    lateinit var db: AppDatabase

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate() {
        super.onCreate()

        setupKoin()
        initDB()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@BaseApplication)
            androidLogger()
            loadKoinModules()
        }
    }

    private fun initDB() {
        db = AppDatabase.invoke(this)
        restaurantRepositoryImpl = RestaurantRepositoryImpl(db.restaurantDao())
    }
}