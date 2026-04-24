package com.example.kigaliride.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.example.kigaliride.data.model.CustomerInfo
import com.example.kigaliride.data.model.DriverInfo
import com.example.kigaliride.data.repository.ApiResult
import com.example.kigaliride.data.repository.KigaliRideRepository
import com.example.kigaliride.utils.DevicePrefs
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

    private fun normalizeRwandaPhoneNumber(input: String): String? {
        val cleaned = input.replace("\\s".toRegex(), "")

        val isValid = cleaned.matches(Regex("^(078|079|072|073)\\d{7}$"))
        if (!isValid) return null

        return "+250${cleaned.drop(1)}"
    }

    fun showRideSearchLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    fun hideRideSearchLoading() {
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun validateCustomerPhoneNumber(): String? {
        return normalizeRwandaPhoneNumber(uiState.value.customerPhoneNumber)
    }

    private fun validateDriverPhoneNumber(): String? {
        return normalizeRwandaPhoneNumber(uiState.value.driverPhoneNumber)
    }

    fun loginCustomer(context: Context, onSuccess: () -> Unit) {
        val normalizedPhone = validateCustomerPhoneNumber()

        if (normalizedPhone == null) {
            showSnackbar("Enter a valid MTN or Airtel phone number")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val savedPhone = DevicePrefs.getCustomerPhone(context)

            if (savedPhone == normalizedPhone) {
                // Trusted device
                when (val result = repository.loginCustomer(normalizedPhone)) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, loggedInCustomer = result.data)
                        }
                        onSuccess()
                    }
                    is ApiResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        showAppMessage(result.message)
                    }
                }
            } else {
                // Not trusted → check DB
                val exists = repository.checkCustomerPhone(normalizedPhone)

                if (exists) {
                    _uiState.update { it.copy(isLoading = false) }
                    showAppMessage("This phone number is already linked to another device.")
                } else {
                    // New user → register + save device
                    when (val registerResult = repository.registerCustomer(normalizedPhone)) {
                        is ApiResult.Success -> {
                            DevicePrefs.saveCustomerPhone(context, normalizedPhone)
                            showOtpField()
                            _uiState.update { it.copy(isLoading = false) }
                            showAppMessage("OTP sent to your phone")
                        }
                        is ApiResult.Error -> {
                            _uiState.update { it.copy(isLoading = false) }
                            showAppMessage(registerResult.message)
                        }
                    }
                }
            }
        }
    }

    fun verifyOtp(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val phone = normalizeRwandaPhoneNumber(uiState.value.customerPhoneNumber)
            val otp = uiState.value.customerOtp

            if (phone == null) {
                _uiState.update { it.copy(isLoading = false) }
                showAppMessage("Enter a valid MTN or Airtel phone number")
                return@launch
            }

            when (val result = repository.verifyOtp(phone, otp)) {
                is ApiResult.Success -> {
                    hideOtpField()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loggedInCustomer = result.data
                        )
                    }
                    onSuccess()
                }

                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    showAppMessage("Invalid phone number OR OTP")
                }
            }
        }
    }

    fun loginDriver(context: Context, onSuccess: () -> Unit) {
        val phone = validateDriverPhoneNumber()
        val plate = uiState.value.driverPlateNumber.trim()

        if (phone == null) {
            showSnackbar("Enter a valid MTN or Airtel phone number")
            return
        }

        if (plate.isBlank()) {
            showSnackbar("Please enter plate number")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val savedPhone = DevicePrefs.getDriverPhone(context)
            val savedPlate = DevicePrefs.getDriverPlate(context)

            if (savedPhone == phone && savedPlate == plate) {
                // Trusted device
                when (val result = repository.loginDriver(plate, phone)) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, loggedInDriver = result.data)
                        }
                        onSuccess()
                    }
                    is ApiResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        showAppMessage(result.message)
                    }
                }
            } else {
                val exists = repository.checkDriverAccount(phone, plate)

                if (exists) {
                    _uiState.update { it.copy(isLoading = false) }
                    showAppMessage("This driver account is already linked to another device.")
                } else {
                    // New driver → allow + save device
                    DevicePrefs.saveDriver(context, phone, plate)

                    when (val result = repository.loginDriver(plate, phone)) {
                        is ApiResult.Success -> {
                            _uiState.update {
                                it.copy(isLoading = false, loggedInDriver = result.data)
                            }
                            onSuccess()
                        }
                        is ApiResult.Error -> {
                            _uiState.update { it.copy(isLoading = false) }
                            showAppMessage(result.message)
                        }
                    }
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
