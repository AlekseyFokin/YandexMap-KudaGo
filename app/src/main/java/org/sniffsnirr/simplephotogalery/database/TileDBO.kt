package org.sniffsnirr.simplephotogalery.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.sniffsnirr.simplephotogalery.entities.Tile

@Entity(tableName = "tiles")
class TileDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int?,
    @ColumnInfo(name = "photo_path")
    override val photoPath: String,
    @ColumnInfo(name = "datetime")
    override val dateTime: Long,
    @ColumnInfo(name = "description")
    override val description: String?
) : Tile