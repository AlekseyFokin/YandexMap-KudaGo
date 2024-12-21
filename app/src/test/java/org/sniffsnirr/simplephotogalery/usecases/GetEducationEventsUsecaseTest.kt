package org.sniffsnirr.simplephotogalery.usecases

import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.sniffsnirr.simplephotogalery.entities.Coords
import org.sniffsnirr.simplephotogalery.entities.EducationEvents
import org.sniffsnirr.simplephotogalery.entities.Event
import org.sniffsnirr.simplephotogalery.entities.Place
import org.sniffsnirr.simplephotogalery.entities.PlaceMark
import org.sniffsnirr.simplephotogalery.restrepository.EducationEventsRepository

class GetEducationEventsUsecaseTest {

    val educationEventsRepositoryImpl = mock<EducationEventsRepository>()

    @AfterEach
    fun resetRepository() {
        Mockito.reset(educationEventsRepositoryImpl)
    }

    @Test
    fun `should create PlaceMarks from Events`() { //тестирование функции makePlaceMark
        val testEducationEvents = getPositiveTestEducationEvents()

        val expectPlaceMarks = mutableListOf(
            PlaceMark(
                "titlePlace", "titleEvent",
                Point(0.0, 0.0)
            )
        )

        val testedUsecase = GetEducationEventsUsecase(educationEventsRepositoryImpl)
        val actual = testedUsecase.makePlaceMark(testEducationEvents.results)
        Assertions.assertTrue(
            (expectPlaceMarks[0].point.latitude == actual[0].point.latitude) &&
                    (expectPlaceMarks[0].point.longitude == actual[0].point.longitude) &&
                    (expectPlaceMarks[0].title == actual[0].title) &&
                    (expectPlaceMarks[0].description == actual[0].description)
        )
    }

    @Test
    fun `positive test of getEducationEventUsecase`() {  //тестирование GetEducationEventsUsecase с правильными данными
        val testEducationEvents = getPositiveTestEducationEvents()
        val expectPlaceMarks = testEducationEvents.results

        runBlocking {
            Mockito.`when`(educationEventsRepositoryImpl.getEducationEvents())
                .thenReturn(testEducationEvents)
        }

        val testedUsecase = GetEducationEventsUsecase(educationEventsRepositoryImpl)
        lateinit var actual: List<PlaceMark>
        runBlocking { actual = testedUsecase.getEducationEvents() ?: emptyList() }

        Assertions.assertTrue(
            (expectPlaceMarks[0].place?.coords?.lat == actual[0].point.latitude) &&
                    (expectPlaceMarks[0].place?.coords?.lon == actual[0].point.longitude) &&
                    (expectPlaceMarks[0].place?.title == actual[0].title) &&
                    (expectPlaceMarks[0].title == actual[0].description)
        )
    }

    @Test
    fun `negative test of getEducationEventUsecase with null place`() {  //тестирование GetEducationEventsUsecase с набором данных в котором есть пустой объект Place - должен отсеяться
        val testEducationEvents = getNegativeTestEducationEventsWithPlaceIsNull()

        runBlocking {
            Mockito.`when`(educationEventsRepositoryImpl.getEducationEvents())
                .thenReturn(testEducationEvents)
        }

        val testedUsecase = GetEducationEventsUsecase(educationEventsRepositoryImpl)
        lateinit var actual: List<PlaceMark>
        runBlocking { actual = testedUsecase.getEducationEvents() ?: emptyList() }

        Assertions.assertTrue(actual.size == 0)
    }

    @Test
    fun `negative test of getEducationEventUsecase with null coords`() {  //тестирование GetEducationEventsUsecase с набором данных в котором есть пустой объект Coords - должен отсеяться
        val testEducationEvents = getNegativeTestEducationEventsWithCoordsIsNull()

        runBlocking {
            Mockito.`when`(educationEventsRepositoryImpl.getEducationEvents())
                .thenReturn(testEducationEvents)
        }

        val testedUsecase = GetEducationEventsUsecase(educationEventsRepositoryImpl)
        lateinit var actual: List<PlaceMark>
        runBlocking { actual = testedUsecase.getEducationEvents() ?: emptyList() }

        Assertions.assertTrue(actual.size == 0)
    }
    @Test
    fun `negative test of getEducationEventUsecase with 2 objects with same coords`() {  //тестирование GetEducationEventsUsecase с набором данных в котором два объекта с одинаковыми Coords - должен отсеяться
        val testEducationEvents = getNegativeTestEducationEventsWithTwoSameCoordsIsNull()

        runBlocking {
            Mockito.`when`(educationEventsRepositoryImpl.getEducationEvents())
                .thenReturn(testEducationEvents)
        }

        val testedUsecase = GetEducationEventsUsecase(educationEventsRepositoryImpl)
        lateinit var actual: List<PlaceMark>
        runBlocking { actual = testedUsecase.getEducationEvents() ?: emptyList() }

        Assertions.assertTrue(actual.size == 1)
    }

    fun getPositiveTestEducationEvents(): EducationEvents {
        val testCoords = Coords(0.0, 0.0)
        val testPlace = Place(
            "address",
            testCoords,
            0,
            true,
            true,
            "location",
            "+71111111111",
            "http://",
            "slug",
            "subway",
            "titlePlace"
        )
        val testEvent = Event(0, testPlace, "titleEvent")
        return EducationEvents(1, "next", "previous", listOf(testEvent))
    }

    fun getNegativeTestEducationEventsWithPlaceIsNull(): EducationEvents {
        val testPlace2 = null
        val testEvent2 = Event(0, testPlace2, "titleEvent")
        return EducationEvents(1, "next", "previous", listOf( testEvent2))
    }

    fun getNegativeTestEducationEventsWithCoordsIsNull(): EducationEvents {
        val testCoords = null
        val testPlace = Place(
            "address",
            testCoords,
            0,
            true,
            true,
            "location",
            "+71111111111",
            "http://",
            "slug",
            "subway",
            "titlePlace"
        )
        val testEvent = Event(0, testPlace, "titleEvent")
        return EducationEvents(1, "next", "previous", listOf(testEvent))
    }

    fun getNegativeTestEducationEventsWithTwoSameCoordsIsNull(): EducationEvents {
        val testCoords = Coords(0.0, 0.0)
        val testPlace1 = Place(
            "address",
            testCoords,
            0,
            true,
            true,
            "location",
            "+71111111111",
            "http://",
            "slug",
            "subway",
            "titlePlace"
        )
        val testEvent1 = Event(0, testPlace1, "titleEvent")

        val testPlace2 = Place(
            "address",
            testCoords,
            1,
            true,
            true,
            "location",
            "+71111111111",
            "http://",
            "slug",
            "subway",
            "titlePlace"
        )
        val testEvent2 = Event(1, testPlace2, "titleEvent")

        return EducationEvents(2, "next", "previous", listOf(testEvent1, testEvent2))
    }
}