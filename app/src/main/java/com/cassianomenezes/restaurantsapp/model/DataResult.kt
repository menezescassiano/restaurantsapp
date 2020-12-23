package com.cassianomenezes.restaurantsapp.model

import com.cassianomenezes.restaurantsapp.internal.RequestStatus

class DataResult<T>(
    val status: RequestStatus,
    val data: T? = null
)