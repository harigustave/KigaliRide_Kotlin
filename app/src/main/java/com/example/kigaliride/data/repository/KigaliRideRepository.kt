package com.example.kigaliride.data.repository

import com.example.kigaliride.data.model.CustomerInfo
import com.example.kigaliride.data.model.DriverInfo
import com.example.kigaliride.data.network.ClosestDriversRequest
//import com.example.kigaliride.data.network.CustomerLocationRequest
import com.example.kigaliride.data.network.CustomerLoginRequest
import com.example.kigaliride.data.network.DriverLocationRequest
import com.example.kigaliride.data.network.DriverLoginRequest
import com.example.kigaliride.data.network.RetrofitClient
import com.example.kigaliride.data.network.SetAvailabilityRequest
import retrofit2.Response
import com.example.kigaliride.data.network.PasscodeRequest

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

class KigaliRideRepository {

    private val api = RetrofitClient.apiService

    suspend fun loginCustomer(phoneNumber: String): ApiResult<CustomerInfo> {
        return handleResponse(api.customerLogin(CustomerLoginRequest(phoneNumber)))
    }

//    suspend fun updateCustomerLocation(phoneNumber: String, latitude: Double, longitude: Double): ApiResult<CustomerInfo> {
//        return handleResponse(api.updateCustomerLocation(CustomerLocationRequest(phoneNumber, latitude, longitude)))
//    }

    suspend fun getClosestDrivers(
        latitude: Double,
        longitude: Double,
        serviceType: String
    ): ApiResult<List<DriverInfo>> {
        return handleResponse(
            api.getClosestDrivers(
                ClosestDriversRequest(
                    latitude = latitude,
                    longitude = longitude,
                    serviceType = serviceType
                )
            )
        )
    }

    suspend fun loginDriver(
        carPlate: String,
        phoneNumber: String,
        deviceId: String
    ): ApiResult<DriverInfo> {
        return handleResponse(
            api.driverLogin(
                DriverLoginRequest(carPlate, phoneNumber, deviceId)
            )
        )
    }

    suspend fun updateDriverLocation(carPlate: String, latitude: Double, longitude: Double): ApiResult<DriverInfo> {
        return handleResponse(api.updateDriverLocation(DriverLocationRequest(carPlate, latitude, longitude)))
    }

    suspend fun setDriverAvailability(carPlate: String, isAvailable: Boolean): ApiResult<DriverInfo> {
        return handleResponse(api.setDriverAvailability(SetAvailabilityRequest(carPlate, isAvailable)))
    }

    suspend fun registerCustomer(phoneNumber: String, passcode: String): ApiResult<CustomerInfo> {
        return handleResponse(api.registerCustomer(PasscodeRequest(phoneNumber, passcode)))
    }

    suspend fun verifyPasscode(phone: String, passcode: String): ApiResult<CustomerInfo> {
        return handleResponse(api.verifyPasscode(PasscodeRequest(phone, passcode)))
    }
    suspend fun checkCustomerPhone(phoneNumber: String): Boolean {
        val response = api.checkCustomerPhone(CustomerLoginRequest(phoneNumber))
        return response.body()?.get("exists") == true
    }

//    suspend fun checkDriverAccount(phone: String, plate: String): Boolean {
//        val response = api.checkDriverAccount(DriverLoginRequest(plate, phone))
//        return response.body()?.get("exists") == true
//    }

    private fun <T> handleResponse(response: Response<T>): ApiResult<T> {
        return if (response.isSuccessful && response.body() != null) {
            ApiResult.Success(response.body()!!)
        } else {
            val message = try {
                val errorBody = response.errorBody()?.string()
                if (!errorBody.isNullOrEmpty()) {
                    val json = org.json.JSONObject(errorBody)
                    json.optString("message", "Something went wrong")
                } else {
                    "Something went wrong"
                }
            } catch (e: Exception) {
                "Something went wrong"
            }

            ApiResult.Error(message)
        }
    }
}
