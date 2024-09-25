package com.originalstocks.llabank.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.originalstocks.llabank.ui.screens.HomeViewModel
import com.originalstocks.llabank.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController? = null,
    viewModel: HomeViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(LocalContext.current.getColor(R.color.gen_bg)))
    ) {
        Image(
            painter = painterResource(id = R.drawable.round_yard_24),
            contentDescription = "App Logo",
            colorFilter = ColorFilter.tint(color = Color(LocalContext.current.getColor(R.color.logo_bg))),
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
        )
        Text(
            text = getString(LocalContext.current, R.string.local_to),
            fontSize = 56.sp,
            color = Color(LocalContext.current.getColor(R.color.logo_bg)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 80.dp)
        )
    }

    LaunchedEffect(Unit) {
        delay(1000L)
        navController?.navigate("home") {
            popUpTo("splash") { inclusive = true }
            launchSingleTop = true
        }
    }
}