package com.kacper.rickandmorty

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

data class CharacterList(val results: List<Character>)

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(): CharacterList

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Character
}

object ApiClient {
    val api: RickAndMortyApi = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RickAndMortyApi::class.java)
}
