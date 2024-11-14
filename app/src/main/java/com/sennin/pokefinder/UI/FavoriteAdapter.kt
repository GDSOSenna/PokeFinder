package com.sennin.pokefinder.UI

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sennin.pokefinder.Data.PokemonResponse
import com.sennin.pokefinder.R
import java.util.Locale

class FavoriteAdapter(
    private val context: Context,
    private val favoritePokemonList: List<PokemonResponse>,
    private val onItemClick: (PokemonResponse) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val pokemon = favoritePokemonList[position]
        holder.pokemonNameTextView.text = pokemon.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
        holder.pokemonTypeTextView.text = pokemon.types.joinToString(", ") {
            it.type.name.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString()
            }
        }
        holder.pokemonOrderTextView.text = "Ordem: ${pokemon.order}"
        holder.itemView.setOnClickListener { onItemClick(pokemon) }
    }

    override fun getItemCount(): Int {
        return favoritePokemonList.size
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonNameTextView: TextView = itemView.findViewById(R.id.pokemon_name)
        val pokemonTypeTextView: TextView = itemView.findViewById(R.id.pokemon_type)
        val pokemonOrderTextView: TextView = itemView.findViewById(R.id.pokemon_order)
    }
}
