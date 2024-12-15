package org.sniffsnirr.simplephotogalery.usecases

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

import org.sniffsnirr.simplephotogalery.database.AppDatabaseRepository
import org.sniffsnirr.simplephotogalery.database.TileDBO

class SaveNewPhotoToGalleryUsecaseTest {
    val AppDatabaseRepositoryMock=mock<AppDatabaseRepository>()

    @Test
    fun `should create TileDBO` (){
        val testTileDBO= TileDBO(
            id = null,
            photoPath = photoPath,
            dateTime = dateTime,
            description = null
        )

        val testSaveNewPhotoToGalleryUsecase=SaveNewPhotoToGalleryUsecase(AppDatabaseRepositoryMock)

    }

    companion object
    private val photoPath = "photoUri"
    private val dateTime = 1700010123000L
}