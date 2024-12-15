package org.sniffsnirr.simplephotogalery.usecases

import org.sniffsnirr.simplephotogalery.database.AppDatabaseRepository
import org.sniffsnirr.simplephotogalery.database.AppDatabaseRepositoryImpl
import org.sniffsnirr.simplephotogalery.database.TileDBO
import javax.inject.Inject

class SaveNewPhotoToGalleryUsecase @Inject constructor(private val repository: AppDatabaseRepository) {

operator suspend fun invoke(photoUri:String, createFileTime:Long):TileDBO{
        val newTile = TileDBO(
            id = null,
            photoPath = photoUri,
            dateTime = createFileTime,
            description = null
        )
        repository.saveTile(newTile)
    return newTile
    }
}