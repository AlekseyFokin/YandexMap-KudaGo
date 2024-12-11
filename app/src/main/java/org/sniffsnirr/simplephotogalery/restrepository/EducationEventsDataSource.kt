package org.sniffsnirr.simplephotogalery.restrepository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EducationEventsDataSource @Inject constructor() {
    private val interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
        .addConverterFactory(
            GsonConverterFactory.create()
        ).build()

    fun getApi(): KudaGoApi {
        return retrofit.create(KudaGoApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://kudago.com"
    }

}
