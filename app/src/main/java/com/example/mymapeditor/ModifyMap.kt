package com.example.mymapeditor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mymapeditor.databinding.ModifyMapActivityBinding
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Point
import service.MapApiClient
import java.lang.Exception
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.Mapbox

class ModifyMap : AppCompatActivity() {
    private lateinit var binding: ModifyMapActivityBinding
    private var oneMap: MutableList<Point>? = arrayListOf()
    private lateinit var mapview: MapView

    companion object {
        val EXTRA_KEY = "MAP_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        binding = ModifyMapActivityBinding.inflate(layoutInflater)

        setContentView(binding.root);

        // Displaying the map

        mapview = findViewById(R.id.mapview);
        mapview.onCreate(savedInstanceState)
        // Obtention de la carte de manière asynchrone
        mapview.getMapAsync { map ->
            // On choisi un style (et on ne fait rien avec)
            map.setStyle(Style.OUTDOORS) { style ->
            }
        }

        // Displaying the spinner

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ListMap::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getOneMap(){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = MapApiClient.mapApiService.getMapById(1)
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    oneMap = content
                } else {
                    Toast.makeText(
                        this@ModifyMap,
                        "Error occurred : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ModifyMap,
                    "Error occurred : ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}