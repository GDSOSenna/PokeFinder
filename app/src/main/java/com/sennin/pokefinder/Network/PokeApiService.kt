package com.sennin.pokefinder.Network

import com.sennin.pokefinder.Data.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): PokemonResponse
}
