package org.sniffsnirr.simplephotogalery.restrepository

import android.util.Log
import org.sniffsnirr.simplephotogalery.entities.EducationEvents
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EducationEventsRepositoryImpl @Inject constructor(val retrofitInstance: EducationEventsDataSource):
    EducationEventsRepository {

    override suspend fun getEducationEvents(): EducationEvents? {

        val educationEventsApi = retrofitInstance.getApi()
        val response: Response<EducationEvents>
        return try {
            response = educationEventsApi.getEducationEvents()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.d("RetrofitResponse", response.code().toString())
                null
            }
        } catch (e: Exception) {
            Log.d("RetrofitResponse", e.stackTraceToString())
            null
        }
    }
}