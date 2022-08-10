package com.muckjook.android.src.main

data class Shop(
    val id: Int,
    var name: String,
    /*
        category
          1. 고기
          2. 한식
          3. 일식
          4. 양식
          5. 중식
          6. 아시안
          7. 패스트푸드
          8. 술집
          9. 카페ㆍ디저트
          10.해산물
    */
    var category: Int,
    var latitude: String,
    var longitude: String,
    var region: String
)
