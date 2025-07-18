package com.app.emilockerapp.models.basemodels

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class GenericError (
    @field:Json(name = "success") val success: Boolean?,
    @field:Json(name = "statusCode") val status: String?,
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "errorMessages") val errors: List<String>?,
)