package com.originalstocks.llabank.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import com.originalstocks.llabank.databinding.FragmentSplashBinding
import com.originalstocks.llabank.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, HomeViewModel>(FragmentSplashBinding::inflate) {

    override val viewModel: HomeViewModel by viewModels()

    override fun setupUI() {
        super.setupUI()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.moveToDashboardPage()
        }, 1000)
    }
}