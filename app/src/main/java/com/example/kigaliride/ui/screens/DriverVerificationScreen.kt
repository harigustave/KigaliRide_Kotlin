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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kigaliride.R
import com.example.kigaliride.ui.components.AppHeader
import com.example.kigaliride.ui.theme.SpaceGrotesk

@Composable
fun DriverVerificationScreen(
    phoneNumber: String,
    plateNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    onPlateNumberChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize()
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
                    text = "Driver",
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
                                text = "MOBILE NUMBER",
                                color = Color(0xFFA5A5A5),
                                fontSize = 12.sp,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = SpaceGrotesk
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = onPhoneNumberChanged,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(68.dp),
                                placeholder = {
                                    Text(
                                        text = "078 000 0000",
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

                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "PLATE NUMBER",
                                    color = Color(0xFFA5A5A5),
                                    fontSize = 12.sp,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = SpaceGrotesk
                                )

                                Text(
                                    text = "FORGOT?",
                                    color = Color(0xFF00FF5A),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = SpaceGrotesk
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = plateNumber,
                                onValueChange = onPlateNumberChanged,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(68.dp),
                                placeholder = {
                                    Text(
                                        text = "RAB 123 A",
                                        color = Color(0xFF4F4F4F),
                                        fontFamily = SpaceGrotesk
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.DirectionsCar,
                                        contentDescription = "Plate",
                                        tint = Color(0xFFBFBFBF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

                            Spacer(modifier = Modifier.height(28.dp))

                            Button(
                                onClick = onLoginClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                shape = RoundedCornerShape(32.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00FF43),
                                    disabledContainerColor = Color(0xFF4A4A4A)
                                ),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(22.dp),
                                        color = Color.Black,
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Text(
                                        text = "LOG IN",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = SpaceGrotesk
                                    )

                                    Spacer(modifier = Modifier.size(10.dp))

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Login,
                                        contentDescription = "Login",
                                        tint = Color.Black,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Not registered? ",
                        color = Color(0xFFB7B7B7),
                        fontSize = 14.sp,
                        fontFamily = SpaceGrotesk
                    )

                    Text(
                        text = "Call Us: +250 782 256 907 ",
                        color = Color(0xFF00FF5A),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}