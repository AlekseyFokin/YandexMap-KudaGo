package org.sniffsnirr.simplephotogalery.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sniffsnirr.simplephotogalery.R
import org.sniffsnirr.simplephotogalery.databinding.FragmentMapsBinding
import org.sniffsnirr.simplephotogalery.entities.PlaceMark
import org.sniffsnirr.simplephotogalery.ui.map.MapsViewModel.Companion.START_ZOOM


@AndroidEntryPoint
class MapsFragment : Fragment() { //InputListener

    val viewModel: MapsViewModel by viewModels()

    private lateinit var mapKit: MapKit

    private lateinit var mapView: MapView

    private var _binding: FragmentMapsBinding? = null
    val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mapObjectCollection: MapObjectCollection

    //var placeMarkList = mutableListOf<PlacemarkMapObject>()
    private var currentZoom = START_ZOOM
    private var previusZoom = START_ZOOM

    private var cameraListener =
        CameraListener { _, camerePosition, _, ended ->      //слушатель перемещения по карте
            Log.d(
                "YANDEX",
                "onCameraPositionChanged: ${camerePosition.target.latitude},${camerePosition.target.longitude} "
            )
            binding.minusZoom.isEnabled = false
            binding.plusZoom.isEnabled = false
            if (ended) {
                viewModel.setCurrentLocation(camerePosition.target)
            }
        }

    val cameraCallback = object :
        Map.CameraCallback { // необходимо дождаться окончания движения, чтобы можно было работать с масштабом
        override fun onMoveFinished(isFinished: Boolean) {
            binding.minusZoom.isEnabled = true
            binding.plusZoom.isEnabled = true
        }
    }

    private val mapObjectTapListener =
        object : MapObjectTapListener { // слушатель тапов по объектам
            override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
                AlertDialog.Builder(requireContext())
                    .setTitle(((mapObject.userData) as Pair<String, String>).first)
                    .setMessage(((mapObject.userData) as Pair<String, String>).second)
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .create().show()
                return true
            }
        }

    private var launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.isNotEmpty() && map.values.all { it }) {
                putCurrentGeoLocation()
            } else {
                Toast.makeText(requireContext(), "Locations is not available", Toast.LENGTH_LONG)
                    .show()
            }
        }

    private fun checkPermissions() {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            putCurrentGeoLocation()
        } else {
            launcher.launch(REQUIRED_PERMISSIONS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
        mapKit = MapKitFactory.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        mapView = binding.mapview
        mapObjectCollection =
            binding.mapview.mapWindow.map.mapObjects// ссылка на коллекцию объектов карты

        if (viewModel.firstOpen) {
            checkPermissions()
            viewModel.firstOpen = false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.minusZoom.setOnClickListener {// кнопка zoom-
            viewModel.setZoom(-DELTA_ZOOM)
        }
        binding.plusZoom.setOnClickListener {// кнопка zoom+
            viewModel.setZoom(DELTA_ZOOM)
        }

        binding.position.setOnClickListener { // кнопка кнопка перехода к текущему полажению географическому
            moveToLocation(viewModel.currentGeoLocation.value)
        }

        viewLifecycleOwner.lifecycleScope.launch {// установка места на карте
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentLocationOnMap.collect { currentLocation ->
                    moveToLocation(currentLocation)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {// установка зума на карте
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentZoomOnMap.collect { currentZoom ->
                    setZoom(currentZoom)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {// установка географического положения
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentGeoLocation.collect { currentGeoLocation ->
                    mapObjectCollection.clear()
                    reloadPlaceMarks(true)
                    addMarkerToMap(
                        PlaceMark("Текущее положение", "Вы находитесь здесь.", currentGeoLocation),
                        RED_MARK, true
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {// загрузка меток на карте, по факту загружается 1 раз в начале работы
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.placeMarks.collect { placeMarks ->
                    placeMarks.forEach {
                        addMarkerToMap(it, BLUE_MARK, true)
                    }
                }
            }
        }

    }

    private fun reloadPlaceMarks(isTitleVisible: Boolean) {// перезагрузка меток, требуется при смене текущего географического положения,
        // так как ссылку на прежнее географическое положение удалить нельзя, а только полностью очистить коллекцию меток на карте
        viewModel.placeMarks.value.forEach { addMarkerToMap(it, BLUE_MARK, isTitleVisible) }
    }

    @SuppressLint("MissingPermission")
    private fun putCurrentGeoLocation() { // Получение текущих гео координат
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                viewModel.setGeoLocation(Point(location.latitude, location.longitude))
                moveToLocation(Point(location.latitude, location.longitude))
            }
        }
    }

    private fun addMarkerToMap(
        myPlaceMark: PlaceMark,
        marker: Int,
        titleIsVisible: Boolean
    ) { // установка метки на карту
        val placeMark = mapObjectCollection.addPlacemark()
        placeMark.geometry = myPlaceMark.point
        if (titleIsVisible) {
            placeMark.setText(myPlaceMark.title)
        }
        placeMark.userData = Pair(myPlaceMark.title, myPlaceMark.description)

        placeMark.setScaleFunction(generateScaleFunction())
        placeMark.setTextStyle(getTextStyle())
        placeMark.setIcon(ImageProvider.fromResource(requireContext(), marker))

        placeMark.addTapListener(mapObjectTapListener)

    }

    override fun onStart() {
        super.onStart()
        mapKit.onStart()
        mapView.onStart()
        mapKit.createUserLocationLayer(mapView.mapWindow)
        mapView.mapWindow.map.addCameraListener(cameraListener)
    }

    override fun onStop() {
        mapView.onStop()
        mapKit.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun moveToLocation(currentLocation: Point) {// переход к месту на карте
        binding.mapview.mapWindow.map
            .move(
                CameraPosition(currentLocation, viewModel.currentZoomOnMap.value, AZIMUTH, TILT),
                Animation(Animation.Type.SMOOTH, ANIMATE_MAP_DURATION),
                cameraCallback
            )
    }

    private fun setZoom(zoom: Float) {// изменение масштаба карты
        previusZoom = currentZoom
        binding.mapview.mapWindow.map
            .move(
                CameraPosition(viewModel.currentLocationOnMap.value, zoom, AZIMUTH, TILT),
                Animation(Animation.Type.SMOOTH, ANIMATE_MAP_DURATION),
                cameraCallback
            )
        currentZoom = zoom

        if ((previusZoom <= 14.0f) && (currentZoom > 14.0f)) { // перезагрузкак меток чтобы при крупном масштабе убирались title
            mapObjectCollection.clear()
            reloadPlaceMarks(true)
        }
        if ((previusZoom >= 14.0f) && (currentZoom < 14.0f)) {
            mapObjectCollection.clear()
            reloadPlaceMarks(false)
        }
        addMarkerToMap(
            PlaceMark("Текущее положение", "Вы находитесь здесь.", viewModel.currentGeoLocation.value),
            RED_MARK, true)
    }

    companion object {
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val AZIMUTH = 0.0f
        private const val TILT = 0.0f
        private const val ANIMATE_MAP_DURATION = 0f
        private const val DELTA_ZOOM = 0.5f
        private val RED_MARK = R.drawable.ic_pin_red_png
        private val BLUE_MARK = R.drawable.ic_pin_blue_png

        fun generateScaleFunction(): List<PointF> { //генерация последовательности для масштабирования метки
            val xPoints = generateSequence(-0.015f) { it + 0.01f }
            val listPointF = mutableListOf<PointF>()
            xPoints.take(60).forEach { listPointF.add(PointF(it, it)) }
            return listPointF
        }

        fun getTextStyle(): TextStyle {// стиль текста для метки
            val textStyle = TextStyle()
            textStyle.size = 11.0f
            return textStyle
        }

    }
}