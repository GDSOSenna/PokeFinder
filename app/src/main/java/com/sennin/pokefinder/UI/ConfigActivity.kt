package com.sennin.pokefinder.UI

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sennin.pokefinder.R

class ConfigActivity : AppCompatActivity() {

    private lateinit var resetButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)

        resetButton = findViewById(R.id.reset_favorites_button)
        imageButton = findViewById(R.id.btn_back_home)

        resetButton.setOnClickListener {
            clearSharedPreferences()
        }

        imageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun clearSharedPreferences() {
        sharedPreferences.edit().clear().apply()
        Toast.makeText(this, "Favoritos limpos com sucesso", Toast.LENGTH_SHORT).show()
    }

}
