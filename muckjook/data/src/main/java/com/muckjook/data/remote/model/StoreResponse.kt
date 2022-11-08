package com.muckjook.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * StoreApi에서 반환형으로 사용되는 response data class
 */
data class StoreResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("created_at")
    val date: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("img_url")
    val imgUrls: String
)
