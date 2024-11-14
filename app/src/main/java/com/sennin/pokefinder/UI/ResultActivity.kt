package com.sennin.pokefinder.UI

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sennin.pokefinder.Data.PokemonResponse
import com.sennin.pokefinder.Data.PokemonType
import com.sennin.pokefinder.Data.TypeDetail
import com.sennin.pokefinder.R
import java.util.Locale
import com.bumptech.glide.Glide
import com.sennin.pokefinder.Data.Sprites

class ResultActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private lateinit var imageButton: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)

        val pokemonImageUrl = intent.getStringExtra("pokemon_image_url")
        val pokemonName = intent.getStringExtra("pokemon_name")
        val pokemonOrder = intent.getIntExtra("pokemon_order", 0)
        val pokemonType = intent.getStringExtra("pokemon_type")
        val pokemonImageView = findViewById<ImageView>(R.id.pokemon_image)
        imageButton = findViewById(R.id.btn_back_home)

        imageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<TextView>(R.id.pokemon_name).text = "Nome: ${pokemonName?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }}"

        findViewById<TextView>(R.id.pokemon_order).text = "Ordem: $pokemonOrder"

        findViewById<TextView>(R.id.pokemon_type).text = "Tipo: ${pokemonType?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }}"

        Log.d("ResultActivity", "URL da imagem que está sendo carregada: $pokemonImageUrl")

        Glide.with(this)
            .load(pokemonImageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(pokemonImageView)

        val btnFavorite: Button = findViewById(R.id.btn_favorite)
        btnFavorite.setOnClickListener {
            if (pokemonName != null) {
                val spriteUrl = intent.getStringExtra("pokemon_sprite") ?: ""

                val sprites = Sprites(frontDefault = spriteUrl)

                val pokemon = PokemonResponse(
                    name = pokemonName,
                    order = pokemonOrder,
                    types = listOf(PokemonType(TypeDetail(pokemonType ?: ""))),
                    sprites = sprites
                )

                addToFavorites(pokemon)
                Toast.makeText(this, "$pokemonName adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro ao adicionar aos favoritos.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun addToFavorites(pokemon: PokemonResponse) {
        try {
            if (pokemon.sprites.frontDefault == null) {
                Log.e("addToFavorites", "A URL da imagem está nula para o Pokémon: ${pokemon.name}")
            }

            val favoritesJson = sharedPreferences.getString("pokemon_list", "[]") ?: "[]"
            val type = object : TypeToken<MutableList<PokemonResponse>>() {}.type
            val favoritesList: MutableList<PokemonResponse> = gson.fromJson(favoritesJson, type)

            if (favoritesList.none { it.name == pokemon.name }) {
                Log.d("addToFavorites", "Adicionando Pokémon: ${pokemon.name}, URL da imagem: ${pokemon.sprites.frontDefault}")
                favoritesList.add(pokemon)
            }

            val updatedJson = gson.toJson(favoritesList)
            sharedPreferences.edit().putString("pokemon_list", updatedJson).apply()

            Log.d("ResultActivity", "JSON atualizado de favoritos: $updatedJson")

        } catch (e: Exception) {
            Log.e("ResultActivity", "Erro ao adicionar aos favoritos", e)
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Erro ao adicionar aos favoritos.", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
