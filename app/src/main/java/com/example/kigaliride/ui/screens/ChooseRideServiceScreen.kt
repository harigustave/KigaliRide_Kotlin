package com.example.kigaliride.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TimeToLeave
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kigaliride.R
import com.example.kigaliride.ui.components.AppHeader
import com.example.kigaliride.ui.theme.SpaceGrotesk
import androidx.compose.material.icons.filled.LocalTaxi

@Composable
fun ChooseRideServiceScreen(
    selectedService: String,
    onServiceSelected: (String) -> Unit,
    onContinueClick: () -> Unit,
    isLoading: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Background image
        Image(
            painter = painterResource(id = R.drawable.welcome_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark overlay
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            // Shared header used on all screens
            AppHeader()

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Choose Ride",
                color = Color(0xFFEDEDED),
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = SpaceGrotesk
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Service",
                color = Color(0xFF00FF5A),
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = SpaceGrotesk
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Select the best way to move across Kigali",
                color = Color(0xFF9FA59F),
                fontSize = 14.sp,
                fontFamily = SpaceGrotesk
            )

            Spacer(modifier = Modifier.height(34.dp))

            RideServiceCard(
                title = "TAXI CAR",
                subtitle = "Fast urban commuting",
                icon = {
                    Icon(
                        imageVector = Icons.Filled.LocalTaxi,
                        contentDescription = "Taxi Car",
                        tint = Color(0xFF00FF5A),
                        modifier = Modifier.size(28.dp)
                    )
                },
                isSelected = selectedService == "Taxi Car",
                onClick = { onServiceSelected("Taxi Car") }
            )

            Spacer(modifier = Modifier.height(22.dp))

            RideServiceCard(
                title = "RENT CAR",
                subtitle = "Full-day luxury & freedom",
                icon = {
                    Icon(
                        imageVector = Icons.Filled.DirectionsCar,
                        contentDescription = "Rent Car",
                        tint = Color(0xFF00FF5A),
                        modifier = Modifier.size(28.dp)
                    )
                },
                isSelected = selectedService == "Rent Car",
                onClick = { onServiceSelected("Rent Car") }
            )

            Spacer(modifier = Modifier.height(22.dp))

            RideServiceCard(
                title = "RELOCATION CAR",
                subtitle = "Seamless moving services",
                icon = {
                    Icon(
                        imageVector = Icons.Filled.LocalShipping,
                        contentDescription = "Relocation Car",
                        tint = Color(0xFF00FF5A),
                        modifier = Modifier.size(28.dp)
                    )
                },
                isSelected = selectedService == "Relocation Car",
                onClick = { onServiceSelected("Relocation Car") }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FF43),
                    disabledContainerColor = Color(0xFF4A4A4A)
                ),
                enabled = selectedService.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.Black,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "FIND AVAILABLE RIDES",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk
                    )
                }
            }
        }
    }
}

@Composable
fun RideServiceCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(126.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF00FF5A) else Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF214A2A) else Color(0xFF171717)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 10.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1A1A1A),
                            Color(0xFF161616),
                            Color(0xFF121212)
                        )
                    )
                )
                .padding(horizontal = 24.dp, vertical = 22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        color = Color(0xFF00FF5A),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = subtitle,
                        color = Color(0xFFB5B5B5),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = SpaceGrotesk
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF183B1E)),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
        }
    }
}