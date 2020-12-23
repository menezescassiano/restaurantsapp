package com.cassianomenezes.restaurantsapp.model

data class Restaurant(val name: String, val status: String, val sortingValues: SortingValues, var added: Boolean)