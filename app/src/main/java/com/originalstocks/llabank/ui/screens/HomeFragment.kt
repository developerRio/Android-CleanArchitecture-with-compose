package com.originalstocks.llabank.ui.screens

import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.originalstocks.llabank.R
import com.originalstocks.llabank.data.cache.HotelEntity
import com.originalstocks.llabank.data.network.ApiState
import com.originalstocks.llabank.databinding.FragmentHomeBinding
import com.originalstocks.llabank.ui.base.BaseFragment
import com.originalstocks.llabank.ui.screens.adapters.HorizontalRecyclerAdapter
import com.originalstocks.llabank.ui.screens.adapters.VerticalRecyclerAdapter
import com.originalstocks.llabank.utils.LinePagerIndicatorDecoration
import com.originalstocks.llabank.utils.Utils
import com.originalstocks.llabank.utils.Utils.addOnTextChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {

    private var horizontalAdapter: HorizontalRecyclerAdapter? = null
    private var verticalAdapter: VerticalRecyclerAdapter? = null
    private var snapHelper: PagerSnapHelper? = null
    private var isSheetOpened: Boolean = false
    private var navBottomSheet: BottomSheetBehavior<View>? = null

    override val viewModel: HomeViewModel by viewModels()

    override fun setupUI() {
        super.setupUI()

        viewModel.getAllHotelsNetworkRequest()

        navBottomSheet = BottomSheetBehavior.from(binding.bottomNavSheet)
        navBottomSheet?.isDraggable = true
        navBottomSheet?.peekHeight = 0

        binding.horizontalRv.setHasFixedSize(true)
        binding.verticalRv.setHasFixedSize(true)
        horizontalAdapter = HorizontalRecyclerAdapter()
        verticalAdapter = VerticalRecyclerAdapter()
        binding.horizontalRv.adapter = horizontalAdapter
        binding.verticalRv.adapter = verticalAdapter

        snapHelper = PagerSnapHelper()
        snapHelper?.attachToRecyclerView(binding.horizontalRv)

        binding.horizontalRv.addItemDecoration(LinePagerIndicatorDecoration())

        binding.searchInputEt.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.appBar.setExpanded(false)
            } else {
                binding.appBar.setExpanded(true)
            }
        }

        binding.searchInputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchInputEt.text.toString()
                proceedForFiltering(query = query)
                Utils.closeSoftInput(context = requireContext(), currentFocus = binding.searchInputEt)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.searchInputEt.addOnTextChangeListener { query ->
            proceedForFiltering(query = query)
        }

        toggleBottomSheet(toOpen = false)
        binding.optionButton.setOnClickListener {
            if (!isSheetOpened) {
                toggleBottomSheet(toOpen = true)
                isSheetOpened = true
            } else {
                toggleBottomSheet(toOpen = false)
                isSheetOpened = false
            }
        }


    }

    private fun toggleBottomSheet(toOpen: Boolean) {
        Log.e(TAG, "toggleBottomSheet: $toOpen")
        if (toOpen) {
            navBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
            binding.optionButton.setImageDrawable(ContextCompat.getDrawable(binding.optionButton.context, R.drawable.round_close_24))
        } else {
            navBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
            binding.optionButton.setImageDrawable(ContextCompat.getDrawable(binding.optionButton.context, R.drawable.baseline_more_vert_24))
        }
    }

    private fun proceedForFiltering(query: String) {
        val filteredList = searchInListFor(query)
        verticalAdapter?.updateList(filteredList)
    }

    private fun searchInListFor(query: String): List<HotelEntity> {
        val filteredList = mutableListOf<HotelEntity>()
        viewModel.scope.launch(Dispatchers.IO) {
            viewModel.hotelResponseStateFlow.collectLatest { response: ApiState<List<HotelEntity>> ->
                if (response.value != null) {
                    for (hotel in response.value) {
                        if (hotel.name?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true) {
                            filteredList.add(hotel)
                        }
                    }
                }
            }
        }
        return filteredList
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.scope.launch(Dispatchers.Main) {
            viewModel.hotelResponseStateFlow.collectLatest { response: ApiState<List<HotelEntity>> ->
                setupDataToRecyclerViews(response.value)
            }
        }
    }

    private fun setupDataToRecyclerViews(value: List<HotelEntity>?) {
        if (value != null) {
            horizontalAdapter?.addData(value)
            verticalAdapter?.addData(value)
            "Number of hotels -> ${value.size} \nTop 3 Characters:\n${viewModel.tabulatingAnalyticsForBottomSheet(value)}".also { binding.countTextView.text = it }

        } else {
            Log.e(TAG, "setupDataToViews: error $value")
        }
    }

}