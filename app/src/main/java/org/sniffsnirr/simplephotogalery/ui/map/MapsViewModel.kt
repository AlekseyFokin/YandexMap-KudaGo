package org.sniffsnirr.simplephotogalery.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.simplephotogalery.entities.PlaceMark
import org.sniffsnirr.simplephotogalery.usecases.GetEducationEventsUsecase
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class MapsViewModel @Inject constructor(val getEducationEventsUsecase: GetEducationEventsUsecase) : ViewModel() {

   var firstOpen = true

    private val _currentLocationOnMap = MutableStateFlow( // положение на карте
             Point(
                START_POINT_LATITUDE,
                START_POINT_LONGITUDE
            )
    )
   val currentLocationOnMap = _currentLocationOnMap.asStateFlow()

    private val _currentZoomOnMap=MutableStateFlow<Float>(START_ZOOM)
    val currentZoomOnMap=_currentZoomOnMap.asStateFlow()

    private val _currentGeoLocation=MutableStateFlow(Point( // текущая геопозиция
        START_POINT_LATITUDE,
        START_POINT_LONGITUDE
    ))
    var currentGeoLocation=_currentGeoLocation.asStateFlow()

    private val _placeMarks = MutableStateFlow<List<PlaceMark>>(emptyList()) // метки объектов на карте
    val placeMarks = _placeMarks.asStateFlow()

    init {
        loadPlaceMarks()
    }

    private fun loadPlaceMarks() {// загрузка меток
        viewModelScope.launch {
            val placeMarks = getEducationEventsUsecase.getEducationEvents()
            if (!placeMarks.isNullOrEmpty()) {
                _placeMarks.value = placeMarks
            }
        }
    }

    fun setZoom(deltaZoom: Float) { // смена масштаба текущего
        val newZoom = _currentZoomOnMap.value + deltaZoom
        _currentZoomOnMap.value = newZoom
    }

    fun setCurrentLocation(newLocation: Point) {
        val tempLatitude = _currentLocationOnMap.value.latitude
        val tempLongitude = _currentLocationOnMap.value.longitude

        if ((abs(tempLatitude - newLocation.latitude) > 0.0005) && (abs(tempLongitude - newLocation.longitude)) > 0.0005) {
            _currentLocationOnMap.value = newLocation
        }
    }

    fun setGeoLocation(newGeoLocation:Point){
        _currentGeoLocation.value=newGeoLocation
    }

    companion object {
    const val START_POINT_LATITUDE = 58.590190
    const val START_POINT_LONGITUDE = 49.661316
    const val START_ZOOM=16.5f

    }
}


