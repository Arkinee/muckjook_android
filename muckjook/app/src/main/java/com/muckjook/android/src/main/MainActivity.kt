package com.muckjook.android.src.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.muckjook.android.R
import com.muckjook.android.databinding.ActivityMainBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    val mContext = this
    var mFlag = false
    var mShopIndex = 0
    var mLatitude: Double = 0.0
    var mLongitude: Double = 0.0
    var mShopName: String = ""

    private lateinit var mBinding: ActivityMainBinding
    private val mShopModel: MainViewModel by viewModels()

    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1000
    private lateinit var mNaverMap: NaverMap
    private lateinit var mMapView: MapView
    private lateinit var mLocationSource: FusedLocationSource

    private lateinit var mMainInfoConstraint: ConstraintLayout
    private lateinit var mMainInfoTvTitle: TextView
    private lateinit var mMainInfoTvCategory: TextView
    private lateinit var mMainInfoIvNavigate: ImageView

    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var mList = ArrayList<Marker>()
    private var mShopList = ArrayList<Shop>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        mBinding.shop = mShopModel

        mMapView = findViewById(R.id.main_mapview)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)

        mLocationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        mMainInfoConstraint = findViewById(R.id.main_constraint_info)
        mMainInfoTvTitle = findViewById(R.id.main_tv_info_title)
        mMainInfoTvCategory = findViewById(R.id.main_tv_info_category)
        mMainInfoIvNavigate = findViewById(R.id.main_iv_info_navigation)

        dataSetting()

    }

    override fun onMapReady(map: NaverMap) {

        mNaverMap = map
        mNaverMap.mapType = NaverMap.MapType.Navi
        mNaverMap.locationSource = mLocationSource
        val uiSetting: UiSettings = mNaverMap.uiSettings
        uiSetting.isCompassEnabled = false
        uiSetting.isScaleBarEnabled = false
        uiSetting.isZoomControlEnabled = true
        uiSetting.isLocationButtonEnabled = true

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(33.50692329999986, 126.4888091000001))
            .animate(CameraAnimation.Fly, 1000)
        mNaverMap.moveCamera(cameraUpdate)

        var infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
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

        ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
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

    private fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(mContext, Locale.KOREA)
        val address: ArrayList<Address>
        var addressResult = "주소를 가져 올 수 없습니다."
        try {
            //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
            //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
            address = geoCoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
            if (address.size > 0) {
                // 주소 받아오기
                val currentLocationAddress = address[0].getAddressLine(0)
                    .toString()
                addressResult = currentLocationAddress

            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressResult
    }

    fun dataSetting() {

        mShopList.add(Shop(1, "레이식당", 4, "33.52056269999966", "126.84161879999986"))
        mShopList.add(Shop(2, "이런날엔", 4, "33.54265209999991", "126.82647729999937"))
        mShopList.add(Shop(3, "애월고사리밥", 2, "33.46665650000003", "126.33414050000017"))
        mShopList.add(Shop(4, "인디안썸머 애월", 9, "33.46335240000014", "126.31101799999988"))
        mShopList.add(Shop(5, "만덕이네", 1, "33.39334699999968", "126.79690839999955"))
        mShopList.add(Shop(6, "고깃집돈누리", 1, "33.49088609999987", "126.53025679999963"))
        mShopList.add(Shop(7, "단백", 1, "33.46174880704856", "126.93190868722678"))
        mShopList.add(Shop(8, "삼화풍년식당", 2, "33.521834200000015", "126.57980519999944"))
        mShopList.add(Shop(9, "덕성원중문동점", 5, "33.24922866444424", "126.4300345137437"))
        mShopList.add(Shop(10, "비스트로 홀린체", 8, "33.30786492570516", "126.16451038342343"))
        mShopList.add(Shop(11, "물질 식육식당", 5, "33.30786492570516", "126.16451038342343"))
        mShopList.add(Shop(12, "뽈살집 한림점", 1, "33.41563219999978", "126.26504719999953"))
        mShopList.add(Shop(13, "뽈살집 본점", 1, "33.250822599999566", "126.55867399999991"))
        mShopList.add(Shop(14, "호림식당", 2, "33.24105948868381", "126.56468023128372"))
        mShopList.add(Shop(15, "효퇴국수", 2, "33.501518000000225", "126.5154517999996"))
        mShopList.add(Shop(16, "한스K55흑돼지 참나무장작구이", 1, "33.5050294899549", "126.46424418114654"))
        mShopList.add(Shop(17, "한라닭강정", 7, "33.511390599999594", "126.5123944999996"))
        mShopList.add(Shop(18, "큰여", 2, "33.444203039437255", "126.2952478468848"))
        mShopList.add(Shop(19, "탐라가든", 1, "33.50776069999996", "126.5160925999996"))
        mShopList.add(Shop(20, "조천수산", 10, "33.54159693030245", "126.6344464563005"))
        mShopList.add(Shop(21, "제주서문수산", 10, "33.51139299999963", "126.51372829999943"))
        mShopList.add(Shop(22, "제주돔베고기집", 1, "33.48779080000026", "126.47410929999977"))
        mShopList.add(Shop(23, "이스방한상", 10, "33.51570290000012", "126.50946209999962"))
        mShopList.add(Shop(24, "으뜨미식당", 10, "33.4706785000002", "126.78289460000023"))
        mShopList.add(Shop(25, "우진해장국", 2, "33.51153889999977", "126.51578520000022"))
        mShopList.add(Shop(26, "윤옥", 3, "33.4891655999996", "126.53156409999951"))
        mShopList.add(Shop(27, "우도 근고기", 1, "33.51591620000013", "126.51771869999979"))
        mShopList.add(Shop(28, "와흘길따라", 2, "33.47276400000003", "126.65467080000006"))
        mShopList.add(Shop(29, "올리다버거", 7, "33.50879790000017", "126.51747900000015"))
        mShopList.add(Shop(30, "스시애월", 3, "33.46673199999958", "126.3150013000001"))
        mShopList.add(Shop(31, "옥만이네 제주금능협재점", 2, "33.41636299403067", "126.26374881311096"))
        mShopList.add(Shop(32, "숯불덮밥 화리", 3, "33.4243043000003", "126.40161649999959"))
        mShopList.add(Shop(33, "숙성도 중문점", 1, "33.25832860000001", "126.40333549999981"))
        mShopList.add(Shop(34, "수가성 오메기떡", 9, "33.49531320000006", "126.48955549999985"))

        mMainInfoIvNavigate.setOnClickListener {

            if (isPackageInstalled(mContext, "com.nhn.android.nmap")) { //카카오맵: net.daum.android.map
                //val url = "nmap://place?lat=$mLatitude&lng=$mLongitude&name=$mShopName&appname=com.muckjook.android"
                //Log.d("태그", "$mLatitude $mLongitude")
                val url = "nmap://search?query=$mShopName 제주도"
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

    fun isPackageInstalled(ctx: Context, packageName: String): Boolean {
        return try {
            ctx.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            Log.d("태그", "installed")
            true
        } catch (e: Exception) {
            Log.d("태그", "not installed")
            false
        }
    }

    fun infoSetting(shop: Shop) {

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

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
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

    /*
    val marker = Marker()
    marker.tag = "레이식당"
    marker.position = LatLng(33.52056269999966, 126.84161879999986)
    mList.add(marker)

    val marker2 = Marker()
    marker2.tag = "이런날엔"
    marker2.position = LatLng(33.54265209999991, 126.82647729999937)
    mList.add(marker2)

    val marker3 = Marker()
    marker3.tag = "애월고사리밥"
    marker3.position = LatLng(33.46665650000003, 126.33414050000017)
    mList.add(marker3)

    val marker4 = Marker()
    marker4.tag = "인디안썸머 애월"
    marker4.position = LatLng(33.46335240000014, 126.31101799999988)
    mList.add(marker4)

    val marker5 = Marker()
    marker5.tag = "만덕이네"
    marker5.position = LatLng(33.39334699999968, 126.79690839999955)
    mList.add(marker5)

    val marker6 = Marker()
    marker6.tag = "고깃집돈누리"
    marker6.position = LatLng(33.49088609999987, 126.53025679999963)
    mList.add(marker6)

    val marker7 = Marker()
    marker7.tag = "삼화풍년식당"
    marker7.position = LatLng(33.521834200000015, 126.57980519999944)
    mList.add(marker7)

    val marker8 = Marker()
    marker8.tag = "덕성원중문동점"
    marker8.position = LatLng(33.24920179999977, 126.42588019999994)
    mList.add(marker8)

    val marker9 = Marker()
    marker9.tag = "물질 식육식당"
    marker9.position = LatLng(33.235356599999655, 126.47610589999961)
    mList.add(marker9)

    val marker10 = Marker()
    marker10.tag = "뽈살집 한림점"
    marker10.position = LatLng(33.41563219999978, 126.26504719999953)
    mList.add(marker10)

    val marker11 = Marker()
    marker11.tag = "뽈살집 본점"
    marker11.position = LatLng(33.250822599999566, 126.55867399999991)
    mList.add(marker11)

    val marker12 = Marker()
    marker12.tag = "효퇴국수"
    marker12.position = LatLng(33.501518000000225, 126.5154517999996)
    mList.add(marker12)

    val marker13 = Marker()
    marker13.tag = "한스K55흑돼지 참나무장작구이"
    marker13.position = LatLng(33.5050294899549, 126.46424418114654)
    mList.add(marker13)

    val marker14 = Marker()
    marker14.tag = "한라닭강정"
    marker14.position = LatLng(33.511390599999594, 126.5123944999996)
    mList.add(marker14)

    val marker15 = Marker()
    marker15.tag = "큰여"
    marker15.position = LatLng(33.444203039437255, 126.2952478468848)
    mList.add(marker15)

    val marker16 = Marker()
    marker16.tag = "탐라가든"
    marker16.position = LatLng(33.50776069999996, 126.5160925999996)
    mList.add(marker16)

    val marker17 = Marker()
    marker17.tag = "조천수산"
    marker17.position = LatLng(33.54159693030245, 126.6344464563005)
    mList.add(marker17)

    val marker18 = Marker()
    marker18.tag = "제주서문수산"
    marker18.position = LatLng(33.51139299999963, 126.51372829999943)
    mList.add(marker18)

    val marker19 = Marker()
    marker19.tag = "제주돔베고기집"
    marker19.position = LatLng(33.48779080000026, 126.47410929999977)
    mList.add(marker19)

    val marker20 = Marker()
    marker20.tag = "이스방한상"
    marker20.position = LatLng(33.51570290000012, 126.50946209999962)
    mList.add(marker20)

    val marker21 = Marker()
    marker21.tag = "으뜨미식당"
    marker21.position = LatLng(33.4706785000002, 126.78289460000023)
    mList.add(marker21)

    val marker22 = Marker()
    marker22.tag = "우진해장국"
    marker22.position = LatLng(33.51153889999977, 126.51578520000022)
    mList.add(marker22)

    val marker23 = Marker()
    marker23.tag = "윤옥"
    marker23.position = LatLng(33.4891655999996, 126.53156409999951)
    mList.add(marker23)

    val marker24 = Marker()
    marker24.tag = "우도 근고기"
    marker24.position = LatLng(33.51591620000013, 126.51771869999979)
    mList.add(marker24)

    val marker25 = Marker()
    marker25.tag = "와흘길따라"
    marker25.position = LatLng(33.47276400000003, 126.65467080000006)
    mList.add(marker25)

    val marker26 = Marker()
    marker26.tag = "올리다버거"
    marker26.position = LatLng(33.50879790000017, 126.51747900000015)
    mList.add(marker26)

    val marker27 = Marker()
    marker27.tag = "스시애월"
    marker27.position = LatLng(33.46673199999958, 126.3150013000001)
    mList.add(marker27)

    val marker28 = Marker()
    marker28.tag = "옥만이네 제주금능협재점"
    marker28.position = LatLng(33.41636299403067, 126.26374881311096)
    mList.add(marker28)

    val marker29 = Marker()
    marker29.tag = "숯불덮밥 화리"
    marker29.position = LatLng(33.4243043000003, 126.40161649999959)
    mList.add(marker29)

    val marker30 = Marker()
    marker30.tag = "숙성도 중문점"
    marker30.position = LatLng(33.25832860000001, 126.40333549999981)
    mList.add(marker30)

    val marker31 = Marker()
    marker31.tag = "수가성 오메기떡"
    marker31.position = LatLng(33.49531320000006, 126.48955549999985)
    mList.add(marker31)
    */

}