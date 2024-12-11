package org.sniffsnirr.simplephotogalery.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TileDBO::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tileDao(): TileDao
}