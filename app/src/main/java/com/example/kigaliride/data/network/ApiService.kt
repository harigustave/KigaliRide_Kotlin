package com.example.kigaliride.data.network

import com.example.kigaliride.data.model.CustomerInfo
import com.example.kigaliride.data.model.DriverInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("api/customers/login")
    suspend fun customerLogin(@Body request: CustomerLoginRequest): Response<CustomerInfo>

    @PUT("api/customers/update-location")
    suspend fun updateCustomerLocation(@Body request: CustomerLocationRequest): Response<CustomerInfo>

    @POST("api/customers/closest-drivers")
    suspend fun getClosestDrivers(@Body request: ClosestDriversRequest): Response<List<DriverInfo>>

    @POST("api/drivers/login")
    suspend fun driverLogin(@Body request: DriverLoginRequest): Response<DriverInfo>

    @PUT("api/drivers/update-location")
    suspend fun updateDriverLocation(@Body request: DriverLocationRequest): Response<DriverInfo>

    @PUT("api/drivers/set-availability")
    suspend fun setDriverAvailability(@Body request: SetAvailabilityRequest): Response<DriverInfo>
}
