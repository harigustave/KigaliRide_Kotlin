package com.examples.kigaliride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.examples.kigaliride.ui.navigation.KigaliRideApp
import com.examples.kigaliride.ui.theme.KigaliRideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KigaliRideTheme(darkTheme = true) {
                Surface {
                    KigaliRideApp()
                }
            }
        }
    }
}