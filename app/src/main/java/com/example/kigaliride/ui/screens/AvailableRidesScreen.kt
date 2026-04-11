package com.example.kigaliride.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kigaliride.R
import com.example.kigaliride.data.model.DriverInfo
import com.example.kigaliride.ui.components.AppHeader
import com.example.kigaliride.ui.theme.SpaceGrotesk
import androidx.compose.material3.ExperimentalMaterial3Api
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Message
import coil.compose.AsyncImage

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AvailableRidesScreen(
    drivers: List<DriverInfo>,
    selectedDriver: DriverInfo?,
    onContactDriver: (DriverInfo) -> Unit,
    onDismissBottomSheet: () -> Unit,
    isLoading: Boolean
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All Rides") }

    val filteredDrivers = drivers.filter { driver ->
        val carMake = driver.carMake.orEmpty()
        val carModel = driver.carModel.orEmpty()
        val carColor = driver.carColor.orEmpty()
        val carPlate = driver.carPlate.orEmpty()
        val serviceType = driver.serviceType.orEmpty()

        val matchesSearch =
            carMake.contains(searchText, ignoreCase = true) ||
                    carModel.contains(searchText, ignoreCase = true) ||
                    carColor.contains(searchText, ignoreCase = true) ||
                    carPlate.contains(searchText, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "All Rides" -> true
            "SUV" -> driver.carBody.orEmpty().equals("SUV", ignoreCase = true)
            "SEDAN" -> driver.carBody.orEmpty().equals("SEDAN", ignoreCase = true)
            "PICKUP" -> driver.carBody.orEmpty().equals("PICKUP", ignoreCase = true)
            "BUS" -> driver.carBody.orEmpty().equals("BUS", ignoreCase = true)
            "TRUCK" -> driver.carBody.orEmpty().equals("TRUCK", ignoreCase = true)
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            AppHeader()

            Spacer(modifier = Modifier.height(18.dp))

            SearchBar(
                value = searchText,
                onValueChange = { searchText = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("All Rides", "SUV", "SEDAN", "PICKUP", "BUS", "TRUCK").forEach { filter ->
                    FilterChip(
                        text = filter,
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF00FF43))
                    }
                }

                filteredDrivers.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No nearby drivers found yet",
                            color = Color(0xFFB5B5B5),
                            fontFamily = SpaceGrotesk,
                            fontSize = 16.sp
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(filteredDrivers) { driver ->
                            RideCard(
                                driver = driver,
                                onContactClick = { onContactDriver(driver) }
                            )
                        }
                    }
                }
            }
        }

        if (selectedDriver != null) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            ModalBottomSheet(
                onDismissRequest = onDismissBottomSheet,
                sheetState = sheetState,
                containerColor = Color(0xFF141414)
            ) {
                ContactDriverSheet(
                    driver = selectedDriver,
                    onDismiss = onDismissBottomSheet
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1C1C1C))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search",
            tint = Color(0xFF9E9E9E),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 15.sp,
                fontFamily = SpaceGrotesk
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = "Search car by make, model or color...",
                        color = Color(0xFF8D8D8D),
                        fontSize = 15.sp,
                        fontFamily = SpaceGrotesk
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFF00FF43) else Color(0xFF232323))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.Black else Color(0xFFD0D0D0),
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun RideCard(
    driver: DriverInfo,
    onContactClick: () -> Unit
) {
    val serviceType = driver.serviceType.orEmpty()
    val carMake = driver.carMake.orEmpty()
    val carModel = driver.carModel.orEmpty()
    val carColor = driver.carColor.orEmpty()
    val carPlate = driver.carPlate.orEmpty()

    val rating = remember(driver.carPlate) {
        when (driver.carPlate.length % 3) {
            0 -> "4.9"
            1 -> "4.8"
            else -> "4.7"
        }
    }

    val colorDot = Color(0xFFD9D9D9)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF171717)
        )
    ) {
        Column {
            Box {
                AsyncImage(
                    model = driver.carImage ?: R.drawable.car_dummy,
                    contentDescription = "Car",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF102E14))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = serviceType.uppercase(),
                        color = Color(0xFF00FF43),
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${carMake.uppercase()} ${carModel.uppercase()}",
                            color = Color(0xFFEDEDED),
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Plate: ",
                                color = Color(0xFFB0B0B0),
                                fontFamily = SpaceGrotesk,
                                fontSize = 14.sp
                            )
                            Text(
                                text = carPlate,
                                color = Color(0xFF00FF43),
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "COLOR",
                            color = Color(0xFFB0B0B0),
                            fontFamily = SpaceGrotesk,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(11.dp)
                                    .clip(CircleShape)
                                    .background(colorDot)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = carColor.uppercase(),
                                color = Color(0xFFEDEDED),
                                fontFamily = SpaceGrotesk,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Seats",
                        tint = Color(0xFF00FF43),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${driver.carCapacity} Seats",
                        color = Color(0xFFEDEDED),
                        fontFamily = SpaceGrotesk,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.width(22.dp))

                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFF00FF43),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = rating,
                        color = Color(0xFFEDEDED),
                        fontFamily = SpaceGrotesk,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = onContactClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00FF43)
                    )
                ) {
                    Text(
                        text = "Contact Driver",
                        color = Color.Black,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Go",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ContactDriverSheet(
    driver: DriverInfo,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val carMake = driver.carMake.orEmpty()
    val carModel = driver.carModel.orEmpty()
    val carPlate = driver.carPlate.orEmpty()
    val phone = driver.phoneNumber.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp)
    ) {
        Text(
            text = "Contact Driver",
            color = Color.White,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "$carMake $carModel",
            color = Color(0xFF00FF43),
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Phone: $phone",
            color = Color(0xFFEDEDED),
            fontFamily = SpaceGrotesk,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Plate: $carPlate",
            color = Color(0xFFB5B5B5),
            fontFamily = SpaceGrotesk,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = {
                if (phone.isNotBlank()) {
                    val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:$phone")
                    }
                    context.startActivity(smsIntent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2A2A2A)
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Message,
                contentDescription = "Message",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Send Message",
                color = Color.White,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (phone.isNotBlank()) {
                    val callIntent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phone")
                    }
                    context.startActivity(callIntent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00FF43)
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Call,
                contentDescription = "Call",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Call Driver",
                color = Color.Black,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Text(
                text = "Close",
                color = Color.White,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }
    }
}