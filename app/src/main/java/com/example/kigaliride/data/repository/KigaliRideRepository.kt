package com.example.kigaliride.data.repository

import com.example.kigaliride.data.model.CustomerInfo
import com.example.kigaliride.data.model.DriverInfo
import com.example.kigaliride.data.network.ClosestDriversRequest
import com.example.kigaliride.data.network.CustomerLocationRequest
import com.example.kigaliride.data.network.CustomerLoginRequest
import com.example.kigaliride.data.network.DriverLocationRequest
import com.example.kigaliride.data.network.DriverLoginRequest
import com.example.kigaliride.data.network.RetrofitClient
import com.example.kigaliride.data.network.SetAvailabilityRequest
import retrofit2.Response
import com.example.kigaliride.data.network.VerifyOtpRequest

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

class KigaliRideRepository {

    private val api = RetrofitClient.apiService

    suspend fun loginCustomer(phoneNumber: String): ApiResult<CustomerInfo> {
        return handleResponse(api.customerLogin(CustomerLoginRequest(phoneNumber)))
    }

    suspend fun updateCustomerLocation(phoneNumber: String, latitude: Double, longitude: Double): ApiResult<CustomerInfo> {
        return handleResponse(api.updateCustomerLocation(CustomerLocationRequest(phoneNumber, latitude, longitude)))
    }

    suspend fun getClosestDrivers(
        latitude: Double,
        longitude: Double,
        phoneNumber: String,
        serviceType: String
    ): ApiResult<List<DriverInfo>> {
        return handleResponse(
            api.getClosestDrivers(
                ClosestDriversRequest(
                    latitude = latitude,
                    longitude = longitude,
                    phoneNumber = phoneNumber,
                    serviceType = serviceType
                )
            )
        )
    }

    suspend fun loginDriver(carPlate: String, phoneNumber: String): ApiResult<DriverInfo> {
        return handleResponse(api.driverLogin(DriverLoginRequest(carPlate, phoneNumber)))
    }

    suspend fun updateDriverLocation(carPlate: String, latitude: Double, longitude: Double): ApiResult<DriverInfo> {
        return handleResponse(api.updateDriverLocation(DriverLocationRequest(carPlate, latitude, longitude)))
    }

    suspend fun setDriverAvailability(carPlate: String, isAvailable: Boolean): ApiResult<DriverInfo> {
        return handleResponse(api.setDriverAvailability(SetAvailabilityRequest(carPlate, isAvailable)))
    }

    suspend fun registerCustomer(phoneNumber: String): ApiResult<CustomerInfo> {
        return handleResponse(api.registerCustomer(CustomerLoginRequest(phoneNumber)))
    }

    suspend fun verifyOtp(phoneNumber: String, otp: String): ApiResult<CustomerInfo> {
        return handleResponse(api.verifyOtp(VerifyOtpRequest(phoneNumber, otp)))
    }

    suspend fun checkCustomerPhone(phoneNumber: String): Boolean {
        val response = api.checkCustomerPhone(CustomerLoginRequest(phoneNumber))
        return response.body()?.get("exists") == true
    }

    suspend fun checkDriverAccount(phone: String, plate: String): Boolean {
        val response = api.checkDriverAccount(DriverLoginRequest(plate, phone))
        return response.body()?.get("exists") == true
    }

    private fun <T> handleResponse(response: Response<T>): ApiResult<T> {
        return if (response.isSuccessful && response.body() != null) {
            ApiResult.Success(response.body()!!)
        } else {
            val message = when (response.code()) {
                403 -> "Your subscription has expired. Please renew to proceed"
                404 -> "Phone number or driver details not registered"
                else -> response.errorBody()?.string() ?: "Something went wrong"
            }
            ApiResult.Error(message)
        }
    }
}
