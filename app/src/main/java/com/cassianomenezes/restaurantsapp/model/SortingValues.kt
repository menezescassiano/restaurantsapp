package com.cassianomenezes.restaurantsapp.model

import java.math.BigDecimal

data class SortingValues(
    val bestMatch: Float,
    val newest: Float,
    val ratingAverage: Float,
    val distance: Int,
    val popularity: Float,
    val averageProductPrice: BigDecimal,
    val deliveryCosts: BigDecimal,
    val minCost: BigDecimal
)
