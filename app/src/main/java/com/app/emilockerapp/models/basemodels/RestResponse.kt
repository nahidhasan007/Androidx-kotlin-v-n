package com.app.emilockerapp.models.basemodels

import com.squareup.moshi.Json

data class RestResponse<T> (
    @Json(name = "success")
    var success: Boolean,

    @Json(name = "message")
    var message: String,

    @Json(name = "data")
    var data: T?= null,

    @Json(name = "statusCode")
    var code: String,
)