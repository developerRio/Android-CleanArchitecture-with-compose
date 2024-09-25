package com.originalstocks.llabank.ui.base

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.originalstocks.llabank.R
import com.originalstocks.llabank.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun toggleProgressDialog(loadingViewVisible: Boolean?) {
        Log.e(TAG, "toggleProgressDialog = $loadingViewVisible")
    }

    fun reactOnToast(dataString: String?) {
        Utils.showToast(this, dataString ?: "Something went wrong !")
    }
}