package com.examples.kigaliride.ui.screens

import android.Manifest
import android.content.Context
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.examples.kigaliride.R
import com.examples.kigaliride.data.model.DriverInfo
import com.examples.kigaliride.ui.components.AppHeader
import com.examples.kigaliride.ui.theme.SpaceGrotesk
import java.util.Locale

@Composable
fun DriverDashboardScreen(
    driver: DriverInfo?,
    isLoading: Boolean,
    onUseLocationClicked: (Double, Double) -> Unit,
    onSetAvailability: (Boolean) -> Unit,
    showMessage: (String) -> Unit
) {
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

    val carMake = driver?.carMake.orEmpty()
    val carModel = driver?.carModel.orEmpty()
    val carPlate = driver?.carPlate.orEmpty()
    val carColor = driver?.carColor.orEmpty()
    val serviceType = driver?.serviceType.orEmpty()
    val isAvailable = driver?.isAvailable == true

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
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
                .verticalScroll(scrollState)
                .padding(horizontal = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            AppHeader()

            Spacer(modifier = Modifier.height(18.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.driver_avatar),
                    contentDescription = "Driver Avatar",
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF0E2A14))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "★ 4.98 Rating",
                        color = Color(0xFF00FF5A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            DriverDashboardMainCard(
                title = "ACTIVE VEHICLE",
                content = {
                    Text(
                        text = "$carMake $carModel",
                        color = Color(0xFF00FF5A),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "PLATE NUMBER",
                                color = Color(0xFF8A8A8A),
                                fontSize = 11.sp,
                                fontFamily = SpaceGrotesk
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = carPlate,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = SpaceGrotesk
                            )
                        }

                        Column {
                            Text(
                                text = "COLOR",
                                color = Color(0xFF8A8A8A),
                                fontSize = 11.sp,
                                fontFamily = SpaceGrotesk
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = carColor,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = SpaceGrotesk
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            DriverDashboardMainCard(
                title = "SERVICE TYPE",
                icon = Icons.Filled.Security,
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        DashboardRadioLine(
                            text = "Taxi Car",
                            selected = serviceType.equals("Taxi Car", ignoreCase = true)
                        )
                        DashboardRadioLine(
                            text = "Rent Car",
                            selected = serviceType.equals("Rent Car", ignoreCase = true)
                        )
                        DashboardRadioLine(
                            text = "Relocation",
                            selected = serviceType.equals("Relocation Car", ignoreCase = true)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            DriverDashboardStatusCard(
                title = "STATUS",
                value = if (isAvailable) "Online" else "Offline",
                checked = isAvailable,
                onCheckedChange = onSetAvailability
            )

            Spacer(modifier = Modifier.height(14.dp))

            DriverMapPlaceholderCard(
                latitude = driver?.latitude,
                longitude = driver?.longitude
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FF43)
                ),
                enabled = !isLoading
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "USE THIS LOCATION",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        LoadingOverlay(isLoading = isLoading)
    }
}

@Composable
fun DriverDashboardMainCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171717))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1A1A1A),
                            Color(0xFF171717),
                            Color(0xFF121212)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (icon != null) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1A1A1A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = Color(0xFF00FF5A),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Text(
                        text = title,
                        color = Color(0xFF8A8A8A),
                        fontSize = 11.sp,
                        fontFamily = SpaceGrotesk
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                content()
            }
        }
    }
}

@Composable
fun DriverDashboardStatusCard(
    title: String,
    value: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171717))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF272727)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = title,
                        tint = Color(0xFFBFBFBF),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        color = Color(0xFF8A8A8A),
                        fontSize = 11.sp,
                        fontFamily = SpaceGrotesk
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = value,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = SpaceGrotesk
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = Color(0xFF00FF43)
                )
            )
        }
    }
}

@Composable
fun DashboardRadioLine(
    text: String,
    selected: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (selected) Color(0xFF00FF5A) else Color.Transparent)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = if (selected) Color(0xFF00FF5A) else Color(0xFFA0A0A0),
            fontSize = 16.sp,
            fontFamily = SpaceGrotesk
        )
    }
}

@Composable
fun DriverMapPlaceholderCard(
    latitude: Double?,
    longitude: Double?
) {
    val context = LocalContext.current
    var locationName by remember { mutableStateOf("Getting location...") }

    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            getAddressFromLocation(context, latitude, longitude) {
                locationName = it
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF12A8B0))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.map_placeholder),
                contentDescription = "Map Placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0x6600FF43)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00FF43)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location Marker",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (latitude != null && longitude != null)
                        "📍 $locationName"
                    else
                        "Location unavailable",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = SpaceGrotesk
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "You can use the button below to change your location anytime.",
                    color = Color(0xFFE8E8E8),
                    fontSize = 11.sp,
                    fontFamily = SpaceGrotesk
                )
            }
        }
    }
}

fun getAddressFromLocation(
    context: Context,
    latitude: Double,
    longitude: Double,
    onResult: (String) -> Unit
) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]

            val locationName = listOfNotNull(
                address.subLocality,
                address.locality,
                address.adminArea
            ).joinToString(", ")

            onResult(
                locationName.ifBlank {
                    address.getAddressLine(0) ?: "Unknown location"
                }
            )
        } else {
            onResult("Unknown location")
        }
    } catch (e: Exception) {
        onResult("Location unavailable")
    }
}