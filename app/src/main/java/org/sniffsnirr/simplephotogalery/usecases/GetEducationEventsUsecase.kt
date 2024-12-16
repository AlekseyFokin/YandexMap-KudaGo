package org.sniffsnirr.simplephotogalery.usecases

import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.simplephotogalery.entities.Event
import org.sniffsnirr.simplephotogalery.entities.PlaceMark
import org.sniffsnirr.simplephotogalery.restrepository.EducationEventsRepositoryImpl
import javax.inject.Inject

@ActivityRetainedScoped
class GetEducationEventsUsecase @Inject constructor(val educationEventsRepositoryImpl: EducationEventsRepositoryImpl) {

    suspend fun getEducationEvents(): List<PlaceMark>? {// фильтрую события, чтобы можно было показать на карте
        val educationEvents = educationEventsRepositoryImpl.getEducationEvents()
        if (educationEvents == null) return emptyList()
        else {
            val uniqeEducationEventsHasPlaceAndCoords = educationEvents.results
                .filter { it.place != null }
                .filter { it.place!!.coords != null }
                .distinctBy { it.place!!.coords }
            val placeMarks = makePlaceMark(uniqeEducationEventsHasPlaceAndCoords)
            return placeMarks
        }
    }

    private fun makePlaceMark(events: List<Event>): List<PlaceMark> {// преобразование событий в метки, для использования на карте
        val placeMarks = mutableListOf<PlaceMark>()
        events.forEach { event ->
            placeMarks.add(
                PlaceMark(
                    event.place!!.title,
                    event.title,
                    Point(event.place.coords!!.lat, event.place.coords.lon)
                )
            )
        }
        return placeMarks
    }
}