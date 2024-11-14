package com.sennin.pokefinder.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sennin.pokefinder.Data.PokemonResponse
import com.sennin.pokefinder.R

class FavoriteActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoritePokemonList: MutableList<PokemonResponse> = mutableListOf()
    private lateinit var imageButton: ImageButton

    private lateinit var emptyListMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        recyclerView = findViewById(R.id.favorites_recycler_view)
        emptyListMessage = findViewById(R.id.empty_list_message)
        imageButton = findViewById(R.id.btn_back_home)
        recyclerView.layoutManager = LinearLayoutManager(this)

        imageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        loadFavorites()

        if (favoritePokemonList.isEmpty()) {
            emptyListMessage.visibility = View.VISIBLE
        } else {
            emptyListMessage.visibility = View.GONE
        }

        favoriteAdapter = FavoriteAdapter(this, favoritePokemonList) { pokemon ->
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("pokemon_name", pokemon.name)
                putExtra("pokemon_order", pokemon.order)
                putExtra("pokemon_type", pokemon.types.joinToString(", ") { it.type.name })
                putExtra("pokemon_image_url", pokemon.sprites.frontDefault ?: "")
            }
            startActivity(intent)
        }

        recyclerView.adapter = favoriteAdapter
    }

    private fun loadFavorites() {
        try {
            val sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)
            val favoritesJson = sharedPreferences.getString("pokemon_list", "[]") ?: "[]"

            Log.d("FavoriteActivity", "JSON de favoritos: $favoritesJson")

            val gson = Gson()
            val type = object : TypeToken<List<PokemonResponse>>() {}.type
            val favoritesList: List<PokemonResponse> = gson.fromJson(favoritesJson, type)

            if (favoritesList.isNotEmpty()) {
                Log.d("FavoriteActivity", "Lista de favoritos carregada com sucesso: $favoritesList")
                favoritePokemonList.clear()
                favoritePokemonList.addAll(favoritesList)

                for (pokemon in favoritePokemonList) {
                    Log.d("FavoriteActivity", "Pokémon: ${pokemon.name}, Imagem URL: ${pokemon.sprites?.frontDefault}")
                }
            } else {
                Log.d("FavoriteActivity", "A lista de favoritos está vazia.")
            }

        } catch (e: Exception) {
            Log.e("FavoriteActivity", "Erro ao carregar favoritos", e)
            e.printStackTrace()

            runOnUiThread {
                Toast.makeText(this, "Erro ao carregar favoritos", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
