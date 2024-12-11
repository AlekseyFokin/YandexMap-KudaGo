package org.sniffsnirr.simplephotogalery.database

import kotlinx.coroutines.flow.Flow
import org.sniffsnirr.simplephotogalery.entities.Tile
import javax.inject.Inject

class AppDatabaseRepository @Inject constructor(
    private val dao: TileDao
) {
    suspend fun saveTile(tile: TileDBO) = dao.insertTile(tile)

    fun giveMeTiles(): Flow<List<Tile>> = dao.getAllTiles()

}