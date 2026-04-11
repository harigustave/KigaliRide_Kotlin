package com.example.kigaliride.data.network

import com.google.gson.annotations.SerializedName

data class CustomerLoginRequest(
    @SerializedName("phone_number")
    val phoneNumber: String
)

data class DriverLoginRequest(
    @SerializedName("car_plate")
    val carPlate: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)

data class CustomerLocationRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)

data class DriverLocationRequest(
    @SerializedName("car_plate")
    val carPlate: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)

data class ClosestDriversRequest(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("service_type")
    val serviceType: String
)

data class SetAvailabilityRequest(
    @SerializedName("car_plate")
    val carPlate: String,
    @SerializedName("is_available")
    val isAvailable: Boolean
)
