package com.originalstocks.llabank.ui.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.originalstocks.llabank.data.cache.HotelEntity
import com.originalstocks.llabank.databinding.HorizontalImageItemLayoutBinding
import com.originalstocks.llabank.utils.Utils.loadImageWithGlide

class HorizontalRecyclerAdapter :
    RecyclerView.Adapter<HorizontalRecyclerAdapter.HorizontalViewHolder>() {
    private var dataList: ArrayList<HotelEntity> = ArrayList()

    inner class HorizontalViewHolder(private val binding: HorizontalImageItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(dataList: ArrayList<HotelEntity>, position: Int) {

            val data = dataList[position]

            binding.propImageView.loadImageWithGlide(data.avatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder =
        HorizontalViewHolder(
            HorizontalImageItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    fun addData(list: List<HotelEntity>?) {
        val dataListSize = dataList.size
        list?.forEach { data ->
            if (!dataList.contains(data)) {
                dataList.add(data)
            }
        }
        notifyItemRangeInserted(dataListSize, list?.size ?: 0)
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) = holder.bindData(
        dataList, position
    )
}