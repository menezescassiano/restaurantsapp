package com.cassianomenezes.restaurantsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class RestaurantObject(@PrimaryKey val id: String, @ColumnInfo(name = "title") val title: String)
