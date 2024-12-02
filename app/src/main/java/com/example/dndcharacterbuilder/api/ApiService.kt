package com.example.dndcharacterbuilder.api


import com.example.dndcharacterbuilder.api.model.ClassesResponse
import com.example.dndcharacterbuilder.api.model.ItemResponse
import com.example.dndcharacterbuilder.api.model.RacesResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    @GET("api/races")
    suspend fun getRaces(): Response<RacesResponse>

    @GET("api/spells")
    suspend fun getSpells(): Response<ItemResponse>

    @GET("api/feats")
    suspend fun getFeats(): Response<ItemResponse>

    @GET("api/equipment")
    suspend fun getEquipment(): Response<ItemResponse>

    @GET("api/classes")
    suspend fun getClasses(): Response<ItemResponse>

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