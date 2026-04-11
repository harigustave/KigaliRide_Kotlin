package com.example.kigaliride.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kigaliride.data.model.CustomerInfo
import com.example.kigaliride.data.model.DriverInfo
import com.example.kigaliride.data.repository.ApiResult
import com.example.kigaliride.data.repository.KigaliRideRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppUiState(
    val isLoading: Boolean = false,
    val customerPhoneNumber: String = "",
    val driverPhoneNumber: String = "",
    val driverPlateNumber: String = "",
    val loggedInCustomer: CustomerInfo? = null,
    val loggedInDriver: DriverInfo? = null,
    val selectedDriver: DriverInfo? = null,
    val customerLatitude: Double? = null,
    val customerLongitude: Double? = null,
    val driverLatitude: Double? = null,
    val driverLongitude: Double? = null,
    val customerOtp: String = "",
    val showOtpField: Boolean = false,
    val isOtpEnabled: Boolean = false,
    val snackbarMessage: String? = null,
    val selectedServiceType: String = "",
    val nearbyDrivers: List<DriverInfo> = emptyList(),
)

class AppViewModel(
    private val repository: KigaliRideRepository = KigaliRideRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun onCustomerPhoneChanged(value: String) {
        _uiState.update { it.copy(customerPhoneNumber = value) }
    }

    fun onDriverPhoneChanged(value: String) {
        _uiState.update { it.copy(driverPhoneNumber = value) }
    }

    fun onDriverPlateChanged(value: String) {
        _uiState.update { it.copy(driverPlateNumber = value.uppercase()) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun showAppMessage(message: String) {
        showSnackbar(message)
    }

    fun chooseServiceType(serviceType: String) {
        _uiState.update { it.copy(selectedServiceType = serviceType) }
    }

    fun selectDriver(driver: DriverInfo) {
        _uiState.update { it.copy(selectedDriver = driver) }
    }

    fun dismissSelectedDriver() {
        _uiState.update { it.copy(selectedDriver = null) }
    }

    fun loginCustomer(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.loginCustomer(_uiState.value.customerPhoneNumber)) {
                is ApiResult.Success -> {
                    hideOtpField()

                    val phone = _uiState.value.customerPhoneNumber

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loggedInCustomer = CustomerInfo(
                                phoneNumber = phone
                            )
                        )
                    }

                    onSuccess()
                }

                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }

                    if (result.message.contains("not registered", ignoreCase = true)) {
                        showOtpField()
                        showAppMessage("Phone number not registered. Enter OTP to continue.")
                    } else {
                        showAppMessage(result.message)
                    }
                }
            }
        }
    }

    fun loginDriver(onSuccess: () -> Unit) {
        val phone = uiState.value.driverPhoneNumber.trim()
        val plate = uiState.value.driverPlateNumber.trim()

        if (phone.isBlank() || plate.isBlank()) {
            showSnackbar("Please enter phone number and plate number")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.loginDriver(plate, phone)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loggedInDriver = result.data,
                            driverPhoneNumber = phone,
                            driverPlateNumber = plate
                        )
                    }
                    onSuccess()
                }
                is ApiResult.Error -> {
                    val message = if (result.message.contains("Phone number or driver details")) {
                        "Driver not found"
                    } else {
                        result.message
                    }
                    _uiState.update { it.copy(isLoading = false, snackbarMessage = message) }
                }
            }
        }
    }

    fun updateCustomerLocationAndFetchDrivers(
        latitude: Double,
        longitude: Double,
        onSuccess: () -> Unit
    ) {
        val phone = uiState.value.loggedInCustomer?.phoneNumber ?: return
        val service = uiState.value.selectedServiceType
        if (service.isBlank()) {
            showSnackbar("Please choose a ride service first")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val locationResult = repository.updateCustomerLocation(phone, latitude, longitude)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            customerLatitude = latitude,
                            customerLongitude = longitude,
                            loggedInCustomer = locationResult.data
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, snackbarMessage = locationResult.message) }
                    return@launch
                }
            }

            when (val driversResult = repository.getClosestDrivers(latitude, longitude, phone, service)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nearbyDrivers = driversResult.data
                        )
                    }
                    onSuccess()
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nearbyDrivers = emptyList(),
                            snackbarMessage = if (driversResult.message.contains("Phone number or driver details")) {
                                "No drivers found in this location"
                            } else driversResult.message
                        )
                    }
                }
            }
        }
    }

    fun updateDriverLocation(latitude: Double, longitude: Double) {
        val plate = uiState.value.loggedInDriver?.carPlate ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.updateDriverLocation(plate, latitude, longitude)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            driverLatitude = latitude,
                            driverLongitude = longitude,
                            loggedInDriver = result.data,
                            snackbarMessage = "Location updated successfully"
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, snackbarMessage = result.message) }
                }
            }
        }
    }

    fun onCustomerOtpChanged(otp: String) {
        _uiState.update { it.copy(customerOtp = otp) }
    }

    fun hideOtpField() {
        _uiState.update {
            it.copy(
                showOtpField = false,
                isOtpEnabled = false,
                customerOtp = ""
            )
        }
    }

    fun showOtpField() {
        _uiState.update {
            it.copy(
                showOtpField = true,
                isOtpEnabled = true
            )
        }
    }

    fun setDriverAvailability(isAvailable: Boolean) {
        val plate = uiState.value.loggedInDriver?.carPlate ?: return

        viewModelScope.launch {
            when (val result = repository.setDriverAvailability(plate, isAvailable)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(loggedInDriver = result.data) }
                }
                is ApiResult.Error -> showSnackbar(result.message)
            }
        }
    }

    private fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }
}
