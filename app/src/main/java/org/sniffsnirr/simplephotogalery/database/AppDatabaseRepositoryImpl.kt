package org.sniffsnirr.simplephotogalery.database

import kotlinx.coroutines.flow.Flow
import org.sniffsnirr.simplephotogalery.entities.Tile
import javax.inject.Inject

class AppDatabaseRepositoryImpl @Inject constructor(
    private val dao: TileDao
):AppDatabaseRepository {
    override suspend fun saveTile(tile: TileDBO) = dao.insertTile(tile)

    override fun giveMeTiles(): Flow<List<Tile>> = dao.getAllTiles()

}