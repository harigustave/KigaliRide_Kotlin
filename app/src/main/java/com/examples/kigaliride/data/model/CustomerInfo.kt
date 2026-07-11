package com.examples.kigaliride.data.model

import com.google.gson.annotations.SerializedName

// This Kotlin data class is based on the user's C# CustomerInfo model.
// The C# model uses phone_number, latitude, longitude, paid, and payment_date JSON keys.
data class CustomerInfo(
    val id: Int? = null,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
    @SerializedName("paid")
    val paid: Boolean? = null,
    @SerializedName("payment_date")
    val paymentDate: String? = null,
    @SerializedName("otp")
    val userOTP: String? = null
)
