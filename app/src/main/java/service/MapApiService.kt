package service

import model.Point
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MapApiService {
    @GET("iut/game-list")
    suspend fun getMaps(): Response<MutableList<String>>

    @GET("iut/game/{id}")
    suspend fun getMapById(@Path("id") id: Int): Response<MutableList<Point>>

    @POST("iut/game/{id}")
    suspend fun createMap(@Path("id") id: Int): Response<MutableList<Point>>

    @DELETE("iut/game/{id}")
    suspend fun deleteMap(@Path("id") id: Int): Response<String>
}