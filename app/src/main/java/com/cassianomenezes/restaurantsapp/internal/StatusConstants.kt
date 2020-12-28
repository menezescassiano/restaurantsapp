package com.cassianomenezes.restaurantsapp.internal

enum class StatusConstants(val status: String) {
    INITIAL("initial"),
    OPEN("open"),
    CLOSED("closed"),
    ORDER_AHEAD("order ahead"),
    BEST_MATCH("best match"),
    NEWEST("newest"),
    RATING_AVERAGE("rating average"),
    DISTANCE("distance"),
    POPULARITY("popularity"),
    AVERAGE_PRICE("average price"),
    DELIVERY_COSTS("delivery costs"),
    MIN_COSTS("min costs")
}