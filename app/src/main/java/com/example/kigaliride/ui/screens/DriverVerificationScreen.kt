package com.example.kigaliride.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DriverVerificationScreen(
    phoneNumber: String,
    plateNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    onPlateNumberChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean
) {
    // This screen lets a driver log in using phone number and plate number.
    ScreenContainer {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            KigaliRideTitle(
                title = "Driver Verification",
                subtitle = "Enter your phone number and plate number"
            )

            Spacer(modifier = Modifier.height(24.dp))
            DarkTextInput(value = phoneNumber, onValueChange = onPhoneNumberChanged, label = "Phone number")
            Spacer(modifier = Modifier.height(14.dp))
            DarkTextInput(value = plateNumber, onValueChange = onPlateNumberChanged, label = "Car plate number")
            Spacer(modifier = Modifier.height(18.dp))
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryActionButton(text = "LOGIN", onClick = onLoginClick)
        }

        LoadingOverlay(isLoading = isLoading)
    }
}
