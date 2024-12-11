package org.sniffsnirr.simplephotogalery.entities

interface Tile {
    val id: Int?
    val photoPath: String
    val dateTime: Long
    val description: String?
}
