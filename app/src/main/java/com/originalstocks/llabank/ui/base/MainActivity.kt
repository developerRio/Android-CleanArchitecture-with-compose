package com.originalstocks.llabank.ui.base

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.originalstocks.llabank.ui.screens.HomeViewModel
import com.originalstocks.llabank.utils.Utils
import com.originalstocks.llabank.ui.composables.HomeScreen
import com.originalstocks.llabank.ui.composables.SplashScreen
import com.originalstocks.llabank.ui.composables.themes.LLABankTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LLABankTheme {
                AppNavigation()
            }
        }
    }

    @Composable
    fun AppNavigation() {

        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") { SplashScreen(navController = navController, viewModel = viewModel) }
            composable("home") { HomeScreen(navController = navController, viewModel = viewModel) }
        }
    }


    fun toggleProgressDialog(loadingViewVisible: Boolean?) {
        Log.e(TAG, "toggleProgressDialog = $loadingViewVisible")
    }

    fun reactOnToast(dataString: String?) {
        Utils.showToast(this, dataString ?: "Something went wrong !")
    }
}