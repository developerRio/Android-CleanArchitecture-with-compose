package com.originalstocks.llabank.ui.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.originalstocks.llabank.data.cache.HotelEntity
import com.originalstocks.llabank.databinding.HotelInfoItemLayoutBinding
import com.originalstocks.llabank.utils.Utils.loadImageWithGlide

class VerticalRecyclerAdapter : RecyclerView.Adapter<VerticalRecyclerAdapter.VerticalViewHolder>() {
    private var dataList: ArrayList<HotelEntity> = ArrayList()

    inner class VerticalViewHolder(private val binding: HotelInfoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(dataList: ArrayList<HotelEntity>, position: Int) {

            val data = dataList[position]

            binding.propImageView.loadImageWithGlide(data.avatar)
            binding.apply {
                propertyNameTextView.text = "${data.name}"
                propertyAddressTextView.text = "${data.description}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalViewHolder =
        VerticalViewHolder(
            HotelInfoItemLayoutBinding.inflate(
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

    fun updateList(newList: List<HotelEntity>) {
        dataList = newList as ArrayList<HotelEntity>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: VerticalViewHolder, position: Int) = holder.bindData(
        dataList, position
    )
}