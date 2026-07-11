package com.examples.kigaliride.data.model

import com.google.gson.annotations.SerializedName

// This Kotlin data class is based on the user's C# DriverInfo model.
// The C# model maps JSON keys such as car_plate, car_make, car_model, car_capacity,
// service_type, latitude, longitude, phone_number, and is_available.
data class DriverInfo(
    val id: Int? = null,
    @SerializedName("car_plate")
    val carPlate: String,
    @SerializedName("car_make")
    val carMake: String? = null,
    @SerializedName("car_model")
    val carModel: String? = null,
    @SerializedName("car_color")
    val carColor: String? = null,
    @SerializedName("car_capacity")
    val carCapacity: Int? = null,
    @SerializedName("service_type")
    val serviceType: String? = null,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("is_available")
    val isAvailable: Boolean? = null,
    @SerializedName("distance_km")
    val distanceKm: Double? = null,
    // The backend also returns payment fields, so we keep them here for safety.
    @SerializedName("payment_date")
    val paymentDate: String? = null,
    @SerializedName("paid")
    val paid: Boolean? = null,
    @SerializedName("image")
    val carImage: String?,
    @SerializedName("body")
    val carBody: String?
)
