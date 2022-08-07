package com.muckjook.android.src.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muckjook.android.R
import com.muckjook.android.databinding.ActivityMainBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import java.util.*

class MainActivity:AppCompatActivity(), OnMapReadyCallback {

    val mContext = this


    private lateinit var mBinding:ActivityMainBinding
    private val model : MainViewModel by viewModels()

    private lateinit var mNaverMap : NaverMap
    private lateinit var mLocationSource: FusedLocationSource

    var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = model




    }

    override fun onMapReady(naverMap: NaverMap){
        val cameraPosition = CameraPosition(
            LatLng(37.450820099999724, 126.89661480000015), 16.0)
        naverMap.cameraPosition = cameraPosition
        this.mNaverMap = naverMap

        val marker = Marker().apply{
            position = LatLng(37.450820099999724, 126.89661480000015)
            setOnClickListener {
                Toast.makeText(mContext, "마커 클릭", Toast.LENGTH_SHORT).show()
                true
            }

            map = naverMap
        }


    }




}