package com.example.mymapeditor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
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
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import model.Type

class ModifyMap : AppCompatActivity() {
    private lateinit var binding: ModifyMapActivityBinding
    private var oneMap: MutableList<Point>? = arrayListOf()
    private lateinit var mapview: MapView
    private var onePoint: Point? = null
    private var newMap: MutableList<Point>? = arrayListOf()

    companion object {
        const val EXTRA_MAP_NAME = ""
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
            map.setOnMarkerClickListener(object: MapboxMap.OnMarkerClickListener {
                override
                fun onMarkerClick(@NonNull marker:Marker):Boolean {
                    Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show()
                    onePoint = oneMap?.find { it.latitude == marker.position.latitude }
                    return true
                }
            })
        }

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ListMapActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.createPointButton.setOnClickListener {
            val pointName = binding.pointName.text.toString()
            val pointLatitude = binding.pointLatitude.text.toString()
            val pointLongitude = binding.pointLongitude.text.toString()
            val pointType = binding.pointType.text.toString()
            createPoint(pointName, pointLatitude, pointLongitude, pointType)
        }

        binding.createMapButton.setOnClickListener {
            saveMap()
        }

        if (intent.getStringExtra(EXTRA_MAP_NAME) != null && intent.getStringExtra(EXTRA_MAP_NAME) != "") {
            try {
                getOneMap()
                binding.mapNameInput.setText(intent.getStringExtra(EXTRA_MAP_NAME))
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        if (oneMap != null) {
            binding.editOrCreate.setText(R.string.edit_a_map)
            binding.createMapButton.setText(R.string.edit_a_map)
        }
    }

    private fun getOneMap(){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = intent.getStringExtra(EXTRA_MAP_NAME)
                    ?.let { MapApiClient.mapApiService.getMapById(it) }
                if (response?.isSuccessful == true && response.body() != null) {
                    val content = response.body()
                    oneMap = content
                    println(oneMap)
                    for (point in oneMap!!) {
                        createPoint(
                            point.name.toString(),
                            point.latitude.toString(),
                            point.longitude.toString(),
                            point.type.toString()
                        )
                    }
                } else {
                    Toast.makeText(
                        this@ModifyMap,
                        "Error occurred : ${response?.message()}",
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

    private fun saveMap() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val mapName = binding.mapNameInput.text.toString()
                val points: MutableList<Point> = (newMap ?: arrayListOf()) ?: (oneMap ?: arrayListOf())
                val response = MapApiClient.mapApiService.createMap(mapName, points)
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    oneMap = content
                    for (point in oneMap!!) {
                        createPoint(
                            point.name.toString(),
                            point.latitude.toString(),
                            point.longitude.toString(),
                            point.type.toString()
                        )
                    }
                    Toast.makeText(
                        this@ModifyMap,
                        "Create success",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ModifyMap,
                        "Error occurred : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun createPoint(pointName: String, pointLatitude: String, pointLongitude: String, pointType: String) {

        val point = Point(pointName, Type.TARGET , pointLatitude.toDouble(), pointLongitude.toDouble())

        mapview.getMapAsync{ map ->
            // On choisit un style ( et on ne fait rien avec)
            map.setStyle(Style.OUTDOORS) {
                    style ->
            }
            map.addMarker(MarkerOptions().title("Mon titre").position(LatLng(pointLatitude.toDouble(), pointLongitude.toDouble())))
        }

        if (newMap != null) {
            newMap?.add(point)
        } else {
            oneMap?.add(point)
        }
    }
}