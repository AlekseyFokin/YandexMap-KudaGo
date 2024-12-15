package org.sniffsnirr.simplephotogalery.database

import kotlinx.coroutines.flow.Flow
import org.sniffsnirr.simplephotogalery.entities.Tile

interface AppDatabaseRepository {
    suspend fun saveTile(tile: TileDBO):Unit

    fun giveMeTiles(): Flow<List<Tile>>
}