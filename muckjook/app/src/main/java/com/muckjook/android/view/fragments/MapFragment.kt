package com.muckjook.android.view.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.muckjook.android.R
import com.muckjook.android.view.MainActivity
import com.muckjook.domain.model.Shop
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
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

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

        val infoWindow = InfoWindow()
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

        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        var currentLocation: Location?
        mFusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            currentLocation = location

            val cameraUpdate = CameraUpdate.scrollTo(
                LatLng(
                    currentLocation!!.latitude,
                    currentLocation!!.longitude
                )
            )
            mNaverMap.moveCamera(cameraUpdate)
        }
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
        mShopList.add(Shop(9, "덕성원중문동점", 5, "33.24922507773015", "126.43003882926632", "제주도"))
        mShopList.add(Shop(10, "비스트로 홀린체", 8, "33.307872080237594", "126.16450172516208", "제주도"))

        mShopList.add(Shop(11, "몰질 식육식당", 5, "33.2353616599884", "126.48025432118125", "제주도"))
        mShopList.add(Shop(12, "뽈살집 한림점", 1, "33.41565975346263", "126.26923770022388", "제주도"))
        mShopList.add(Shop(13, "뽈살집 본점", 1, "33.250826689169656", "126.56283331393357", "제주도"))
        mShopList.add(Shop(14, "호림식당", 2, "33.241059473663206", "126.56467593948219", "제주도"))
        mShopList.add(Shop(15, "효퇴국수", 2, "33.50153832720454", "126.51959791689822", "제주도"))
        mShopList.add(
            Shop(
                16,
                "한스K55흑돼지 참나무장작구이",
                1,
                "33.50503311482628",
                "126.46424846364707",
                "제주도"
            )
        )
        mShopList.add(Shop(17, "한라닭강정", 7, "33.51132508572514", "126.51658188875055", "제주도"))
        mShopList.add(Shop(18, "큰여", 2, "33.44419938436569", "126.29523927289478", "제주도"))
        mShopList.add(Shop(19, "탐라가든", 1, "33.50779802227351", "126.52028226448687", "제주도"))
        mShopList.add(Shop(20, "조천수산", 10, "33.541589730376316", "126.63445079329827", "제주도"))

        mShopList.add(Shop(21, "제주서문수산", 10, "33.51134823394941", "126.51789483448226", "제주도"))
        mShopList.add(Shop(22, "제주돔베고기집", 1, "33.487895120473645", "126.478484011992", "제주도"))
        mShopList.add(Shop(23, "이스방한상", 10, "33.51568804460995", "126.51362996779162", "제주도"))
        mShopList.add(Shop(24, "으뜨미식당", 10, "33.47069812938922", "126.78709021328336", "제주도"))
        mShopList.add(Shop(25, "우진해장국", 2, "33.51151146211769", "126.51999485252269", "제주도"))
        mShopList.add(Shop(26, "윤옥", 3, "33.489168652023416", "126.5357290060833", "제주도"))
        mShopList.add(Shop(27, "우도근고기", 1, "33.51591620000013", "126.51771869999979", "제주도"))
        mShopList.add(Shop(28, "와흘길따라", 2, "33.472600676489606", "126.6588691836615", "제주도"))
        mShopList.add(Shop(29, "올리다버거", 7, "33.50878425872066", "126.52165443419446", "제주도"))
        mShopList.add(Shop(30, "스시애월", 3, "33.4667212392274", "126.3191661382996", "제주도"))

        mShopList.add(Shop(31, "옥만이네 제주금능협재점", 2, "33.416516578740726", "126.2634980940149", "제주도"))
        mShopList.add(Shop(32, "숯불덮밥 화리", 3, "33.424301386105775", "126.40578901715666", "제주도"))
        mShopList.add(Shop(33, "숙성도 중문점", 1, "33.25840667452936", "126.40754993432338", "제주도"))
        mShopList.add(Shop(34, "수가성 오메기떡", 9, "33.49531163114395", "126.4937071155684", "제주도"))
        mShopList.add(Shop(35, "솔지식당", 2, "33.49232394700777", "126.4725693045283", "제주도"))
        mShopList.add(Shop(36, "소금바치 순이네", 10, "33.503643390226365", "126.91262896423164", "제주도"))
        mShopList.add(Shop(37, "성읍칠십리식당", 2, "33.389194226085856", "126.80124425653581", "제주도"))
        mShopList.add(Shop(38, "섬소나이", 10, "33.513332107408985", "126.95754523544697", "제주도"))
        mShopList.add(Shop(39, "새벽숯불가든", 1, "33.4676253102346", "126.90669247769175", "제주도"))
        mShopList.add(Shop(40, "삼대국수회관 본점", 2, "33.50689420099592", "126.53005086330323", "제주도"))

        mShopList.add(Shop(41, "산지해장국(성산점)", 2, "33.46228670812121", "126.9329022121954", "제주도"))
        mShopList.add(Shop(42, "산포식당", 10, "33.44512286815272", "126.8996958016191", "제주도"))
        mShopList.add(Shop(43, "상춘재", 2, "33.4586822401374", "126.70473769355048", "제주도"))
        mShopList.add(Shop(44, "모살물", 10, "33.49054382507552", "126.49454403940396", "제주도"))
        mShopList.add(Shop(45, "버거스테이", 7, "33.52562863414613", "126.85546722132544", "제주도"))
        mShopList.add(Shop(46, "목화식당휴게소", 2, "33.48181634278964", "126.90167629861178", "제주도"))
        mShopList.add(Shop(47, "엘플로리디타", 8, "33.526402164206594", "126.85389425816689", "제주도"))
        mShopList.add(Shop(48, "미영이네", 10, "33.21773279597403", "126.24982466649594", "제주도"))
        mShopList.add(Shop(49, "사계의시간", 10, "33.2323020514834", "126.30882237312464", "제주도"))
        mShopList.add(Shop(50, "문쏘", 10, "33.40568586842554", "126.25670542568331", "제주도"))

        mShopList.add(Shop(51, "바당길", 2, "33.40974665787033", "126.25851986465756", "제주도"))
        mShopList.add(Shop(52, "모리노아루요", 3, "33.41942887402164", "126.40197748350968", "제주도"))
        mShopList.add(Shop(53, "바당고지", 1, "33.39656989857666", "126.24431476012253", "제주도"))
        mShopList.add(Shop(54, "미향해장국", 2, "33.4475172788496", "126.91371777400646", "제주도"))
        mShopList.add(Shop(55, "밀면촌", 3, "33.49655628499795", "126.53910299373793", "제주도"))
        mShopList.add(Shop(56, "모들한상", 4, "33.455348572909216", "126.34697499531555", "제주도"))
        mShopList.add(Shop(57, "모다정", 1, "33.52555519487316", "126.85434780410596", "제주도"))
        mShopList.add(Shop(58, "맛나식당", 10, "33.448600827129404", "126.91606995956654", "제주도"))
        mShopList.add(Shop(59, "명리동식당 본점", 1, "33.3165922248778", "126.27041338945914", "제주도"))
        mShopList.add(Shop(60, "말고기연구소", 1, "33.515526505696634", "126.52029558302695", "제주도"))

        mShopList.add(Shop(61, "우솔해장국", 2, "33.25667520073909", "126.57706863331876", "제주도"))
        mShopList.add(Shop(62, "금박돈", 1, "33.46133502537777", "126.93367743988199", "제주도"))
        mShopList.add(Shop(63, "육고깃집", 1, "33.41162474556525", "126.26504874058655", "제주도"))
        mShopList.add(Shop(64, "고집돌우럭 중문점", 2, "33.25797387923608", "126.41670051818225", "제주도"))
        mShopList.add(Shop(65, "고씨네천지국수", 2, "33.25174257642138", "126.5607383558146", "제주도"))
        mShopList.add(Shop(66, "국시트멍", 2, "33.47937076345954", "126.48132380868866", "제주도"))
        mShopList.add(Shop(67, "고기장", 1, "33.517508498362155", "126.48750284136514", "제주도"))
        mShopList.add(Shop(68, "남원바당", 10, "33.50304351829479", "126.54282666556928", "제주도"))
        mShopList.add(Shop(69, "도갈비", 1, "33.476469954704015", "126.4792969666699", "제주도"))
        mShopList.add(Shop(70, "대기정", 10, "33.24028498789594", "126.42855609329192", "제주도"))

        mShopList.add(Shop(71, "돈블랙 올레시장점", 1, "33.249696856169464", "126.5666719617688", "제주도"))
        mShopList.add(Shop(72, "타코밤", 4, "33.516256941171555", "126.95765144347648", "제주도"))
        mShopList.add(Shop(73, "남해수산", 10, "33.51268209354781", "126.52617930867845", "제주도"))
        mShopList.add(Shop(74, "다람쥐식탁", 3, "33.40777737552521", "126.26576053739814", "제주도"))
        mShopList.add(Shop(75, "등경돌", 2, "33.4626157907575", "126.93458005483186", "제주도"))
        mShopList.add(Shop(76, "도너츠윤 제주점", 9, "33.449455744027446", "126.91116467381184", "제주도"))
        mShopList.add(Shop(77, "넉둥베기", 2, "33.50984174317203", "126.51261223697749", "제주도"))
        mShopList.add(Shop(78, "도토리키친 본점", 2, "33.5153828699007", "126.51859575755903", "제주도"))
        mShopList.add(Shop(79, "천짓골식당", 1, "33.24821720393972", "126.5612495985375", "제주도"))
        mShopList.add(Shop(80, "체면", 3, "33.24831343440119", "126.28435986910745", "제주도"))

        mShopList.add(Shop(81, "제주약수터 본점", 8, "33.24774980214562", "126.56165969322575", "제주도"))
        mShopList.add(Shop(82, "자리돔횟집", 10, "33.25554877919941", "126.57460582301924", "제주도"))
        mShopList.add(Shop(83, "표선수산마트", 10, "33.325391996883496", "126.83597669586594", "제주도"))
        mShopList.add(Shop(84, "중문수두리보말칼국수", 2, "33.25156408181898", "126.42498871468118", "제주도"))
        mShopList.add(Shop(85, "제일수산횟집", 10, "33.251384430168166", "126.56042680397935", "제주도"))
        mShopList.add(Shop(86, "돈사돈 본관", 1, "33.47887516893701", "126.4640136066572", "제주도"))
        mShopList.add(Shop(87, "옥돔식당", 2, "33.219963817821245", "126.24899896573044", "제주도"))

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