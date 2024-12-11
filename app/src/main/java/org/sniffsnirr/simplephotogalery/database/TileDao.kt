package org.sniffsnirr.simplephotogalery.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TileDao {
    @Insert
    suspend fun insertTile(tileDBO: TileDBO)

    @Query("Select * from tiles")
    fun getAllTiles(): Flow<List<TileDBO>>
}