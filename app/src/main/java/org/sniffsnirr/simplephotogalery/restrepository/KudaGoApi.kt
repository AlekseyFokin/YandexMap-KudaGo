package org.sniffsnirr.simplephotogalery.restrepository

import org.sniffsnirr.simplephotogalery.entities.EducationEvents
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

interface KudaGoApi {
    @GET("/public-api/v1.4/events")
    suspend fun getEducationEvents(
        @Query("fields") fields: String = "id,title,place",
        @Query("expand") expand: String = "place",
        @Query("actual_since") actual_since: Long = Date().time / 1000,
        @Query("categories") categories: String = "education",
        @Query("page_size") page_size: Int = 100,
        @Query("page") page: Int = 3
    ): Response<EducationEvents>

}
