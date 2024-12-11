package org.sniffsnirr.simplephotogalery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.sniffsnirr.simplephotogalery.entities.Tile
import org.sniffsnirr.simplephotogalery.usecases.GetAllPhotosUseCase
import org.sniffsnirr.simplephotogalery.usecases.SaveNewPhotoToGalleryUseCase
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    val saveNewPhotoToGalleryUseCase: SaveNewPhotoToGalleryUseCase,
    val getAllPhotosUseCase: GetAllPhotosUseCase

) : ViewModel() {

    val _tiles= getAllTales()
    val tiles get ()=_tiles

    fun getAllTales(): Flow<List<Tile>> {
        return getAllPhotosUseCase.getAllTales()
    }

fun addTile(photoPath: String, createFileTime: Long) {
        viewModelScope.launch {
        saveNewPhotoToGalleryUseCase.saveNewPhoto(photoPath, createFileTime)
        }
    }

}