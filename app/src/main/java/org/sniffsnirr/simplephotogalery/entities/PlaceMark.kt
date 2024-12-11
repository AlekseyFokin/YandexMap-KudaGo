package org.sniffsnirr.simplephotogalery.entities

import com.yandex.mapkit.geometry.Point

data class PlaceMark(
    val title: String,
    val description: String,
    val point: Point
)