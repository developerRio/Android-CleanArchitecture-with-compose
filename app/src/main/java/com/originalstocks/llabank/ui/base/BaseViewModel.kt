package com.originalstocks.llabank.ui.base

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.originalstocks.llabank.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    /** [scope] will be accessible for Coroutines to handle async tasks in viewModels */
    private val viewModelJob = Job()
    val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /** [getNavOptions] is something responsible for setting up animations while page transitioning.*/
    private fun getNavOptions(
        optionalPopUpToId: Int? = null,
        isInclusive: Boolean? = null
    ) = NavOptions.Builder().apply {
        setPopEnterAnim(android.R.anim.slide_in_left)
        setPopExitAnim(android.R.anim.slide_out_right)
        optionalPopUpToId?.let {
            setPopUpTo(optionalPopUpToId, isInclusive ?: false)
        }
    }.build()

    val loaderState: LiveData<Boolean> get() = _loaderState
    private val _loaderState = MutableLiveData<Boolean>()

    val toastMessage: LiveData<String?> get() = mToastMessage
    private val mToastMessage = SingleLiveEvent<String?>()

    val navigationEvent: LiveData<NavController.() -> Any> get() = _navigationEvent
    private val _navigationEvent = SingleLiveEvent<NavController.() -> Any>()

    val finishRequest: LiveData<Unit> get() = _finishRequest
    private val _finishRequest = SingleLiveEvent<Unit>()

    protected fun showToast(message: String?) = mToastMessage.postValue(message)

    protected fun navigateTo(
        navigationRouteId: Int,
        bundle: Bundle? = null,
        optionalPopUpToId: Int? = null,
        isInclusive: Boolean? = false
    ) {
        _navigationEvent.postValue {
            navigate(
                navigationRouteId, bundle, getNavOptions(
                    optionalPopUpToId = optionalPopUpToId,
                    isInclusive = isInclusive,
                )
            )
        }
    }

    protected fun navigateUp() {
        _navigationEvent.postValue {
            navigateUp()
        }
    }

    protected fun showLoader() = _loaderState.postValue(true)

    protected fun hideLoader() = _loaderState.postValue(false)

    protected fun finishActivity() = _finishRequest.postValue(Unit)
}