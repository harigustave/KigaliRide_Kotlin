package com.examples.kigaliride.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.examples.kigaliride.ui.theme.SpaceGrotesk

// Reusable header used across all screens
@Composable
fun AppHeader() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "KIGALIRIDE",
            color = Color(0xFF00FF5A),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = SpaceGrotesk,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier.width(46.dp),
            thickness = 4.dp,
            color = Color(0xFF00FF5A)
        )
    }
}