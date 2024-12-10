package com.example.dndcharacterbuilder.api


import com.example.dndcharacterbuilder.api.model.BackgroundsResponse
import com.example.dndcharacterbuilder.api.model.ClassesResponse
import com.example.dndcharacterbuilder.api.model.ItemResponse
import com.example.dndcharacterbuilder.api.model.RacesResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/races")
    suspend fun getRaces(): Response<RacesResponse>

    @GET("api/spells")
    suspend fun getSpells(@Query("name") name: String? = null): Response<ItemResponse>

    @GET("api/feats")
    suspend fun getFeats(@Query("name") name: String? = null): Response<ItemResponse>

    @GET("api/equipment")
    suspend fun getEquipment(@Query("name") name: String? = null): Response<ItemResponse>

    @GET("api/classes")
    suspend fun getClassesItem(@Query("name") name: String? = null): Response<ItemResponse>

    @GET("api/classes")
    suspend fun getClasses(): Response<ClassesResponse>

    @GET("api/classes")
    suspend fun getClassesItem(): Response<ItemResponse>

    @GET("api/backgrounds")
    suspend fun getBackgrounds(): Response<BackgroundsResponse>

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.dnd5eapi.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}