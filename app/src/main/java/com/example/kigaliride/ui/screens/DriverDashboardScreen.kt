package com.example.kigaliride.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kigaliride.data.model.DriverInfo
import com.example.kigaliride.ui.theme.MutedText

@Composable
fun DriverDashboardScreen(
    driver: DriverInfo?,
    isLoading: Boolean,
    onUseLocationClicked: (Double, Double) -> Unit,
    onSetAvailability: (Boolean) -> Unit,
    showMessage: (String) -> Unit
) {
    // This screen shows driver details after successful login.
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            getHighAccuracyLocation(
                context = context,
                onSuccess = onUseLocationClicked,
                onFailure = showMessage
            )
        } else {
            showMessage("Location permission is required")
        }
    }

    ScreenContainer {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            KigaliRideTitle(
                title = "Driver Dashboard",
                subtitle = "Your active vehicle and current service details"
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoCard(
                title = "Vehicle",
                description = buildString {
                    appendLine("Plate: ${driver?.carPlate ?: "N/A"}")
                    appendLine("Make: ${driver?.carMake ?: "N/A"}")
                    appendLine("Model: ${driver?.carModel ?: "N/A"}")
                    appendLine("Color: ${driver?.carColor ?: "N/A"}")
                    appendLine("Capacity: ${driver?.carCapacity ?: 0}")
                    append("Service: ${driver?.serviceType ?: "N/A"}")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                title = "Driver Contact",
                description = "Phone: ${driver?.phoneNumber ?: "N/A"}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Availability", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = if (driver?.isAvailable == true) "Online" else "Offline", color = MutedText)
                }
                Switch(
                    checked = driver?.isAvailable == true,
                    onCheckedChange = onSetAvailability
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryActionButton(
                text = "USE THIS LOCATION",
                onClick = {
                    if (hasLocationPermission(context)) {
                        getHighAccuracyLocation(
                            context = context,
                            onSuccess = onUseLocationClicked,
                            onFailure = showMessage
                        )
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            )
        }

        LoadingOverlay(isLoading = isLoading)
    }
}
