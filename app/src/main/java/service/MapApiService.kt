package service

import model.Point
import retrofit2.Response
import retrofit2.http.*

interface MapApiService {
    @GET("iut/game-list")
    suspend fun getMaps(): Response<MutableList<String>>

    @GET("iut/game/{id}")
    suspend fun getMapById(@Path("id") name: String): Response<MutableList<Point>>

    @POST("iut/game/{id}")
    suspend fun createMap(@Path("id") name: String, @Body points: MutableList<Point>): Response<MutableList<Point>>

    @DELETE("iut/game/{id}")
    suspend fun deleteMap(@Path("id") name: String): Response<String>
}