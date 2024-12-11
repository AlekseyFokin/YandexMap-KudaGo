package org.sniffsnirr.simplephotogalery.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.simplephotogalery.database.AppDatabaseRepository
import javax.inject.Inject
@ActivityRetainedScoped
class GetAllPhotosUseCase @Inject constructor(val repository: AppDatabaseRepository) {

  fun getAllTales()=repository.giveMeTiles()

}