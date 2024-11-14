package com.sennin.pokefinder.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sennin.pokefinder.Data.PokemonResponse
import com.sennin.pokefinder.Network.NetworkModule
import com.sennin.pokefinder.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SearchActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var favoritesButton: Button
    private lateinit var imageButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchInput = findViewById(R.id.search_input)
        searchButton = findViewById(R.id.search_button)
        favoritesButton = findViewById(R.id.favorites_button)
        imageButton = findViewById(R.id.btn_back_home)

        searchButton.setOnClickListener {
            val pokemonName = searchInput.text.toString().trim().lowercase()
            if (pokemonName.isNotEmpty()) {
                searchPokemon(pokemonName)
            } else {
                Toast.makeText(this, "Por favor, insira o nome de um Pokémon", Toast.LENGTH_SHORT).show()
            }
        }

        favoritesButton.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

        imageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun searchPokemon(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NetworkModule.apiService.getPokemon(name)

                withContext(Dispatchers.Main) {
                    openResultActivity(response)
                }

            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    if (e.code() == 404) {
                        Toast.makeText(this@SearchActivity, "Pokémon não encontrado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SearchActivity, "Erro ao buscar Pokémon: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SearchActivity, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SearchActivity, "Erro inesperado: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    Log.e("SearchActivity", "Erro ao buscar Pokémon", e)
                }
            }
        }
    }

    private fun openResultActivity(pokemon: PokemonResponse) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("pokemon_image_url", pokemon.sprites.frontDefault)
            putExtra("pokemon_name", pokemon.name)
            putExtra("pokemon_order", pokemon.order)
            putExtra("pokemon_type", pokemon.types.firstOrNull()?.type?.name ?: "Desconhecido")
        }
        startActivity(intent)
    }
}
