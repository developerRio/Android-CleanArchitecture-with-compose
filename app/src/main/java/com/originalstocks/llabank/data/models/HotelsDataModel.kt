package com.originalstocks.llabank.data.models


import com.google.gson.annotations.SerializedName

class HotelsDataModel : ArrayList<HotelsDataModel.HorizontalDataModelItem>(){
    data class HorizontalDataModelItem(
        @SerializedName("avatar")
        val avatar: String?, // https://cloudflare-ipfs.com/ipfs/Qmd3W5DuhgHirLHGVixi6V76LhCkZUz6pnFt5AJBiyvHye/avatar/72.jpg
        @SerializedName("createdAt")
        val createdAt: String?, // 2024-09-23T01:31:01.626Z
        @SerializedName("id")
        val id: String?, // 1
        @SerializedName("description")
        val description: String?, // description
        @SerializedName("name")
        val name: String? // Bridget Friesen
    )
}