package com.example.kigaliride

import com.example.kigaliride.ui.viewmodel.AppViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AppViewModelTest {

    private lateinit var viewModel: AppViewModel

    @Before
    fun setup() {
        viewModel = AppViewModel()
    }

    // SERVICE TYPE TESTS

    @Test
    fun chooseServiceType_updatesStateCorrectly() {
        viewModel.chooseServiceType("Taxi Car")

        val state = viewModel.uiState.value
        assertEquals("Taxi Car", state.selectedServiceType)
    }

    @Test
    fun chooseServiceType_blank_shouldRemainBlank() {
        viewModel.chooseServiceType("")

        val state = viewModel.uiState.value
        assertTrue(state.selectedServiceType.isBlank())
    }

    // OTP TESTS

    @Test
    fun showOtpField_enablesOtp() {
        viewModel.showOtpField()

        val state = viewModel.uiState.value
        assertTrue(state.showOtpField)
        assertTrue(state.isOtpEnabled)
    }

    @Test
    fun hideOtpField_resetsOtpState() {
        viewModel.showOtpField()
        viewModel.onCustomerOtpChanged("1234")

        viewModel.hideOtpField()

        val state = viewModel.uiState.value
        assertFalse(state.showOtpField)
        assertFalse(state.isOtpEnabled)
        assertEquals("", state.customerOtp)
    }

    // DRIVER SELECTION TESTS

    @Test
    fun selectDriver_setsSelectedDriver() {
        val dummyDriver = createDummyDriver()

        viewModel.selectDriver(dummyDriver)

        val state = viewModel.uiState.value
        assertEquals(dummyDriver, state.selectedDriver)
    }

    @Test
    fun dismissSelectedDriver_clearsDriver() {
        val dummyDriver = createDummyDriver()

        viewModel.selectDriver(dummyDriver)
        viewModel.dismissSelectedDriver()

        val state = viewModel.uiState.value
        assertNull(state.selectedDriver)
    }

    // WRONG MODEL DETECTION TEST

    @Test
    fun updateCustomerLocation_withoutServiceType_shouldShowError() {
        viewModel.updateCustomerLocationAndFetchDrivers(
            latitude = 1.0,
            longitude = 1.0,
            onSuccess = {}
        )

        val state = viewModel.uiState.value

        assertEquals(
            "Please choose a ride service first",
            state.snackbarMessage
        )
    }
    
    // HELPER

    private fun createDummyDriver() =
        com.example.kigaliride.data.model.DriverInfo(
            id = 1,
            carPlate = "RAB123A",
            carMake = "Toyota",
            carModel = "Corolla",
            carColor = "White",
            carCapacity = 4,
            serviceType = "Taxi Car",
            latitude = 0.0,
            longitude = 0.0,
            phoneNumber = "0780000000",
            isAvailable = true,
            distanceKm = 1.2,
            paymentDate = null,
            paid = true,
            carImage = null,
            carBody = "SEDAN"
        )
}