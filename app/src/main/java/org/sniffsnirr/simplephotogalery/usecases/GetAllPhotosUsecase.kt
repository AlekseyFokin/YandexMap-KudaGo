package org.sniffsnirr.simplephotogalery.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.simplephotogalery.database.AppDatabaseRepositoryImpl
import javax.inject.Inject
@ActivityRetainedScoped
class GetAllPhotosUsecase @Inject constructor(val repository: AppDatabaseRepositoryImpl) {

  fun getAllTales()=repository.giveMeTiles()

}