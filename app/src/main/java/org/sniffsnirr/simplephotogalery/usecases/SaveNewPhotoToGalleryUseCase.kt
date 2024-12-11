package org.sniffsnirr.simplephotogalery.usecases

import org.sniffsnirr.simplephotogalery.database.AppDatabaseRepository
import org.sniffsnirr.simplephotogalery.database.TileDBO
import javax.inject.Inject

class SaveNewPhotoToGalleryUseCase @Inject constructor(val repository: AppDatabaseRepository) {

suspend fun saveNewPhoto(photoUri:String, createFileTime:Long){
        val newTile = TileDBO(
            id = null,
            photoPath = photoUri,
            dateTime = createFileTime,
            description = null
        )
        repository.saveTile(newTile)
    }
}