package com.muckjook.android.src.search.model

data class SearchShop(
    val idx: Int,
    val name: String,
    val imgUrl: String,
    val category: Int,
    val latitude: String,
    val longitude: String,
    val region: String,
    val address: String
)
