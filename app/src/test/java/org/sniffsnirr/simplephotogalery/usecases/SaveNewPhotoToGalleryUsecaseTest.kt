package org.sniffsnirr.simplephotogalery.usecases

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock


import org.sniffsnirr.simplephotogalery.database.AppDatabaseRepository
import org.sniffsnirr.simplephotogalery.database.TileDBO

class SaveNewPhotoToGalleryUsecaseTest {
    val AppDatabaseRepositoryMock=mock<AppDatabaseRepository>()

    @Test
    fun `should create same TileDBO` (){
        val expectTileDBO= TileDBO(
            id = null,
            photoPath = photoPath,
            dateTime = dateTime,
            description = null
        )

        val testSaveNewPhotoToGalleryUsecase=SaveNewPhotoToGalleryUsecase(AppDatabaseRepositoryMock)
        val actual=runBlocking{testSaveNewPhotoToGalleryUsecase(photoPath,dateTime)}

        Assertions.assertEquals(expectTileDBO,actual)
    }

    companion object
    private val photoPath = "photoUri"
    private val dateTime = 1700010123000L
}