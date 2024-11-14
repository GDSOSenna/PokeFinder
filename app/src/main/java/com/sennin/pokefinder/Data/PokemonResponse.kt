package com.sennin.pokefinder.Data

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("sprites") val sprites: Sprites,
    val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("types") val types: List<PokemonType>
)

data class PokemonType(
    @SerializedName("type") val type: TypeDetail
)

data class TypeDetail(
    val name: String
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String
)