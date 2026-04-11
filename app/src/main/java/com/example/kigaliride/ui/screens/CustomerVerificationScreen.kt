package com.example.kigaliride.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kigaliride.R
import com.example.kigaliride.ui.components.AppHeader
import com.example.kigaliride.ui.theme.SpaceGrotesk
import com.example.kigaliride.ui.viewmodel.AppViewModel
import androidx.compose.material.icons.filled.Lock

@Composable
fun CustomerVerificationScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        containerColor = Color.Black,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_bg),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 10.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                AppHeader()

                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = "Customer",
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = SpaceGrotesk
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Verification",
                        color = Color(0xFF00FF5A),
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = SpaceGrotesk
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141414))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1A1A1A),
                                        Color(0xFF171717),
                                        Color(0xFF102715)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column {
                            Text(
                                text = "PHONE NUMBER",
                                color = Color(0xFFA5A5A5),
                                fontSize = 12.sp,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = SpaceGrotesk
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = uiState.customerPhoneNumber,
                                onValueChange = { viewModel.onCustomerPhoneChanged(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(68.dp),
                                placeholder = {
                                    Text(
                                        text = "078XXXXXXX",
                                        color = Color(0xFF4F4F4F),
                                        fontFamily = SpaceGrotesk
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Phone,
                                        contentDescription = "Phone",
                                        tint = Color(0xFFBFBFBF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Black,
                                    unfocusedContainerColor = Color.Black,
                                    disabledContainerColor = Color.Black,
                                    focusedBorderColor = Color.Black,
                                    unfocusedBorderColor = Color.Black,
                                    cursorColor = Color(0xFF00FF5A),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            if (uiState.showOtpField) {
                                Text(
                                    text = "OTP CODE",
                                    color = Color(0xFFA5A5A5),
                                    fontSize = 12.sp,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = SpaceGrotesk
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = uiState.customerOtp,
                                    onValueChange = { viewModel.onCustomerOtpChanged(it) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(68.dp),
                                    placeholder = {
                                        Text(
                                            text = "Enter OTP Code",
                                            color = Color(0xFF4F4F4F),
                                            fontFamily = SpaceGrotesk
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Lock,
                                            contentDescription = "OTP",
                                            tint = Color(0xFFBFBFBF),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    singleLine = true,
                                    enabled = uiState.isOtpEnabled,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.Black,
                                        unfocusedContainerColor = Color.Black,
                                        disabledContainerColor = Color.Black,
                                        focusedBorderColor = Color.Black,
                                        unfocusedBorderColor = Color.Black,
                                        cursorColor = Color(0xFF00FF5A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            if (!uiState.showOtpField) {

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF232323)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(18.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF1A1A1A)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Security,
                                                contentDescription = "Shield",
                                                tint = Color(0xFF00FF5A),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.size(12.dp))

                                        Column {
                                            Text(
                                                text = "Why Verify?",
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                fontFamily = SpaceGrotesk
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = "This prevents fraud and allows",
                                                color = Color(0xFFB7B7B7),
                                                fontSize = 14.sp,
                                                fontFamily = SpaceGrotesk
                                            )
                                            Text(
                                                text = "drivers to reach you easily during pick-up. Your data is encrypted and never shared.",
                                                color = Color(0xFFB7B7B7),
                                                fontSize = 14.sp,
                                                fontFamily = SpaceGrotesk
                                            )
                                        }
                                    }
                                }

                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Button(
                                onClick = {
                                    viewModel.loginCustomer(
                                        onSuccess = {
                                            navController.navigate("choose_service")
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                shape = RoundedCornerShape(32.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00FF43)
                                )
                            ) {
                                Text(
                                    text = "VERIFY HERE",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = SpaceGrotesk
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}
}
