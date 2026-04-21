package com.example.kigaliride.ui.navigation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kigaliride.ui.screens.AvailableRidesScreen
import com.example.kigaliride.ui.screens.ChooseRideServiceScreen
import com.example.kigaliride.ui.screens.CustomerVerificationScreen
import com.example.kigaliride.ui.screens.DriverDashboardScreen
import com.example.kigaliride.ui.screens.DriverVerificationScreen
import com.example.kigaliride.ui.screens.WelcomeScreen
import com.example.kigaliride.ui.screens.getHighAccuracyLocation
import com.example.kigaliride.ui.screens.hasLocationPermission
import com.example.kigaliride.ui.viewmodel.AppViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KigaliRideApp(viewModel: AppViewModel = viewModel()) {
    // This composable is the root of the whole app.
    // It contains navigation and a global snackbar host.
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val customerLocationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            getHighAccuracyLocation(
                context = context,
                onSuccess = { latitude, longitude ->
                    viewModel.updateCustomerLocationAndFetchDrivers(latitude, longitude) {
                        // do nothing
                    }
                    navController.navigate(Routes.AvailableRides)
                },
                onFailure = { viewModel.showAppMessage(it) }
            )
        } else {
            viewModel.showAppMessage("Location permission is required")
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage
        if (!message.isNullOrBlank()) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.Welcome
            ) {
                composable(Routes.Welcome) {
                    WelcomeScreen(navController = navController)
                }

                composable(Routes.CustomerVerification) {
                    CustomerVerificationScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                composable(Routes.ChooseService) {
                    ChooseRideServiceScreen(
                        selectedService = uiState.selectedServiceType,
                        onServiceSelected = viewModel::chooseServiceType,
                        onContinueClick = {
                            if (hasLocationPermission(context)) {
                                getHighAccuracyLocation(
                                    context = context,
                                    onSuccess = { latitude, longitude ->

                                        // Start fetching drivers
                                        viewModel.updateCustomerLocationAndFetchDrivers(latitude, longitude) {
                                            // do nothing
                                        }

                                        // Navigate immediately
                                        navController.navigate(Routes.AvailableRides)
                                    },
                                    onFailure = { message ->
                                        viewModel.showAppMessage(message)
                                    }
                                )
                            } else {
                                customerLocationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        isLoading = uiState.isLoading
                    )
                }

                composable(Routes.AvailableRides) {
                    AvailableRidesScreen(
                        drivers = uiState.nearbyDrivers,
                        selectedDriver = uiState.selectedDriver,
                        onContactDriver = viewModel::selectDriver,
                        onDismissBottomSheet = viewModel::dismissSelectedDriver,
                        isLoading = uiState.isLoading
                    )
                }

                composable(Routes.DriverVerification) {
                    DriverVerificationScreen(
                        phoneNumber = uiState.driverPhoneNumber,
                        plateNumber = uiState.driverPlateNumber,
                        onPhoneNumberChanged = viewModel::onDriverPhoneChanged,
                        onPlateNumberChanged = viewModel::onDriverPlateChanged,
                        onLoginClick = {
                            viewModel.loginDriver {
                                navController.navigate(Routes.DriverDashboard)
                            }
                        },
                        isLoading = uiState.isLoading
                    )
                }

                composable(Routes.DriverDashboard) {
                    DriverDashboardScreen(
                        driver = uiState.loggedInDriver,
                        isLoading = uiState.isLoading,
                        onUseLocationClicked = viewModel::updateDriverLocation,
                        onSetAvailability = viewModel::setDriverAvailability,
                        showMessage = { message ->
                            viewModel.showAppMessage(message)
                        }
                    )
                }
            }
        }
    }
}
