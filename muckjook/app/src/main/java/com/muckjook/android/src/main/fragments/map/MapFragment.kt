package com.muckjook.android.src.main.fragments.map

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.muckjook.android.R
import com.muckjook.android.src.main.MainActivity
import com.muckjook.android.src.main.Shop
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.LocationButtonView

class MapFragment(context: Context) : Fragment(), OnMapReadyCallback {

    val mContext = context
    var mFlag = false
    var mShopIndex = 0
    var mLatitude: Double = 0.0
    var mLongitude: Double = 0.0
    var mShopName: String = ""
    var mRegion: String = ""

    private var mList = ArrayList<Marker>()
    private var mShopList = ArrayList<Shop>()

    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1000
    private lateinit var mNaverMap: NaverMap
    private lateinit var mMapView: MapView
    private lateinit var mLocationSource: FusedLocationSource

    private lateinit var mMainInfoConstraint: ConstraintLayout
    private lateinit var mMainInfoTvTitle: TextView
    private lateinit var mMainInfoTvCategory: TextView
    private lateinit var mMainInfoIvNavigate: ImageView
    private lateinit var mMainLocationBtn: LocationButtonView


    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main_map, container, false)

        mMainInfoConstraint = view.findViewById(R.id.main_constraint_info)

        mMapView = view.findViewById(R.id.main_mapview)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)

        mMainInfoTvTitle = view.findViewById(R.id.main_tv_info_title)
        mMainInfoTvCategory = view.findViewById(R.id.main_tv_info_category)
        mMainInfoIvNavigate = view.findViewById(R.id.main_iv_info_navigation)
        mMainLocationBtn = view.findViewById(R.id.main_location_btn)

        mLocationSource =
            FusedLocationSource(activity as MainActivity, LOCATION_PERMISSION_REQUEST_CODE)

        //가게 데이터 세팅
        dataSetting()

        return view
    }

    override fun onMapReady(map: NaverMap) {

        mNaverMap = map
        mNaverMap.mapType = NaverMap.MapType.Navi
        mNaverMap.locationSource = mLocationSource
        val uiSetting: UiSettings = mNaverMap.uiSettings
        uiSetting.isCompassEnabled = false
        uiSetting.isScaleBarEnabled = false
        uiSetting.isZoomControlEnabled = true
        uiSetting.isLocationButtonEnabled = false
        mMainLocationBtn.map = mNaverMap

        /*
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(33.50692329999986, 126.4888091000001))
            .animate(CameraAnimation.Fly, 1000)
        mNaverMap.moveCamera(cameraUpdate)
        */

        var infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mContext) {
            override fun getText(p0: InfoWindow): CharSequence {
                return infoWindow.marker?.tag as CharSequence
            }
        }

        //마커 세팅
        for (shop: Shop in mShopList) {
            val marker = Marker()
            marker.tag = shop.name
            marker.position = LatLng(shop.latitude.toDouble(), shop.longitude.toDouble())
            mList.add(marker)
        }

        //마커 맵에 표시
        for (i: Int in 0 until mList.size) {

            val m: Marker = mList[i]
            m.setOnClickListener {
                //Toast.makeText(mContext, "레이식당", Toast.LENGTH_SHORT).show()
                infoWindow.open(m)

                if (mShopIndex != i) mFlag = false
                mShopIndex = i
                mLatitude = mShopList[i].latitude.toDouble()
                mLongitude = mShopList[i].longitude.toDouble()
                mShopName = mShopList[i].name
                mRegion = mShopList[i].region
                infoSetting(mShopList[i])

                if (mFlag) {
                    mMainInfoConstraint.isVisible = false
                    mFlag = false
                } else {
                    mMainInfoConstraint.isVisible = true
                    mFlag = true
                }

                true
            }

            m.width = 70
            m.height = 70

            m.icon = OverlayImage.fromResource(R.drawable.mark)
            m.map = mNaverMap

        }

        ActivityCompat.requestPermissions(
            activity as MainActivity,
            permissions,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.locationTrackingMode = LocationTrackingMode.Follow
            }
        }
    }

    fun dataSetting() {

        mShopList.add(Shop(1, "레이식당", 4, "33.53599084032845", "126.83877545378792", "제주도"))
        mShopList.add(Shop(2, "이런날엔", 4, "33.54252465747185", "126.82996891812257", "제주도"))
        mShopList.add(Shop(3, "애월고사리밥", 2, "33.4666516159749", "126.3376002824", "제주도"))
        mShopList.add(Shop(4, "인디안썸머 애월", 9, "33.46333452823021", "126.31451985525442", "제주도"))
        mShopList.add(Shop(5, "만덕이네", 1, "33.3933366883094", "126.80038784968663", "제주도"))
        mShopList.add(Shop(6, "고깃집돈누리", 1, "33.49090671849354", "126.53375273239463", "제주도"))
        mShopList.add(Shop(7, "단백", 1, "33.461748422252086", "126.93121163915245", "제주도"))
        mShopList.add(Shop(8, "삼화풍년식당", 2, "33.521834200000015", "126.57980519999944", "제주도"))
        mShopList.add(Shop(9, "덕성원중문동점", 5, "33.24922866444424", "126.4300345137437", "제주도"))
        mShopList.add(Shop(10, "비스트로 홀린체", 8, "33.30786492570516", "126.16451038342343", "제주도"))
        mShopList.add(Shop(11, "물질 식육식당", 5, "33.30786492570516", "126.16451038342343", "제주도"))
        mShopList.add(Shop(12, "뽈살집 한림점", 1, "33.41563219999978", "126.26504719999953", "제주도"))
        mShopList.add(Shop(13, "뽈살집 본점", 1, "33.250822599999566", "126.55867399999991", "제주도"))
        mShopList.add(Shop(14, "호림식당", 2, "33.24105948868381", "126.56468023128372", "제주도"))
        mShopList.add(Shop(15, "효퇴국수", 2, "33.501518000000225", "126.5154517999996", "제주도"))
        mShopList.add(
            Shop(
                16,
                "한스K55흑돼지 참나무장작구이",
                1,
                "33.5050294899549",
                "126.46424418114654",
                "제주도"
            )
        )
        mShopList.add(Shop(17, "한라닭강정", 7, "33.511390599999594", "126.5123944999996", "제주도"))
        mShopList.add(Shop(18, "큰여", 2, "33.444203039437255", "126.2952478468848", "제주도"))
        mShopList.add(Shop(19, "탐라가든", 1, "33.50776069999996", "126.5160925999996", "제주도"))
        mShopList.add(Shop(20, "조천수산", 10, "33.54159693030245", "126.6344464563005", "제주도"))
        mShopList.add(Shop(21, "제주서문수산", 10, "33.51139299999963", "126.51372829999943", "제주도"))
        mShopList.add(Shop(22, "제주돔베고기집", 1, "33.48779080000026", "126.47410929999977", "제주도"))
        mShopList.add(Shop(23, "이스방한상", 10, "33.51570290000012", "126.50946209999962", "제주도"))
        mShopList.add(Shop(24, "으뜨미식당", 10, "33.4706785000002", "126.78289460000023", "제주도"))
        mShopList.add(Shop(25, "우진해장국", 2, "33.51153889999977", "126.51578520000022", "제주도"))
        mShopList.add(Shop(26, "윤옥", 3, "33.4891655999996", "126.53156409999951", "제주도"))
        mShopList.add(Shop(27, "우도 근고기", 1, "33.51591620000013", "126.51771869999979", "제주도"))
        mShopList.add(Shop(28, "와흘길따라", 2, "33.47276400000003", "126.65467080000006", "제주도"))
        mShopList.add(Shop(29, "올리다버거", 7, "33.50879790000017", "126.51747900000015", "제주도"))
        mShopList.add(Shop(30, "스시애월", 3, "33.46673199999958", "126.3150013000001", "제주도"))
        mShopList.add(Shop(31, "옥만이네 제주금능협재점", 2, "33.41636299403067", "126.26374881311096", "제주도"))
        mShopList.add(Shop(32, "숯불덮밥 화리", 3, "33.4243043000003", "126.40161649999959", "제주도"))
        mShopList.add(Shop(33, "숙성도 중문점", 1, "33.25832860000001", "126.40333549999981", "제주도"))
        mShopList.add(Shop(34, "수가성 오메기떡", 9, "33.49531320000006", "126.48955549999985", "제주도"))

        mMainInfoIvNavigate.setOnClickListener {

            if (isPackageInstalled(mContext, "com.nhn.android.nmap")) { //카카오맵: net.daum.android.map
                //val url = "nmap://place?lat=$mLatitude&lng=$mLongitude&name=$mShopName&appname=com.muckjook.android"
                //Log.d("태그", "$mLatitude $mLongitude")
                val url = "nmap://search?query=$mShopName $mRegion"
                val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(appIntent)
            } else {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.nhn.android.nmap")
                    )
                )
            }

            true
        }

    }

    private fun isPackageInstalled(ctx: Context, packageName: String): Boolean {
        return try {
            ctx.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            Log.d("태그", "installed")
            true
        } catch (e: Exception) {
            Log.d("태그", "not installed")
            false
        }
    }

    private fun infoSetting(shop: Shop) {

        mMainInfoTvTitle.text = shop.name

        when (shop.category) {
            1 -> mMainInfoTvCategory.text = "고기"
            2 -> mMainInfoTvCategory.text = "한식"
            3 -> mMainInfoTvCategory.text = "일식"
            4 -> mMainInfoTvCategory.text = "양식"
            5 -> mMainInfoTvCategory.text = "중식"
            6 -> mMainInfoTvCategory.text = "아시안"
            7 -> mMainInfoTvCategory.text = "패스트푸드"
            8 -> mMainInfoTvCategory.text = "술집"
            9 -> mMainInfoTvCategory.text = "카페ㆍ디저트"
            10 -> mMainInfoTvCategory.text = "해산물"
        }

    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

}