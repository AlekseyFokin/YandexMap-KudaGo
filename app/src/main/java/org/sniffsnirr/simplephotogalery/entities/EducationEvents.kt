package org.sniffsnirr.simplephotogalery.entities

data class EducationEvents(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Event>
)

data class Event(
    val id: Int,
    val place: Place?,
    val title: String
)

data class Place(
    val address: String,
    val coords: Coords?,
    val id: Int,
    val is_closed: Boolean,
    val is_stub: Boolean,
    val location: String,
    val phone: String,
    val site_url: String,
    val slug: String,
    val subway: String,
    val title: String
)

data class Coords(
    val lat: Double,
    val lon: Double
)