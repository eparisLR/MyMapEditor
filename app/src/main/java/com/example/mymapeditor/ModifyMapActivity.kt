package com.example.mymapeditor

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
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

class ModifyMapActivity : AppCompatActivity() {
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
            map.addOnMapLongClickListener {
                showCoords(it)
            }
        }

        checkChangeTitle()

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ListMapActivity::class.java)
            startActivity(intent)
            finish()
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

        binding.pointsList.setOnClickListener {
            val intent = Intent(this, PointsEditActivity::class.java)
            (application as MapApplication).setCurrentMapName(binding.mapNameInput.text.toString())
            if (oneMap!!.size > 0) {
                (application as MapApplication).setCurrentPoints(oneMap!!)
            }
            if(newMap!!.size > 0){
                (application as MapApplication).setCurrentPoints(newMap!!)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun showCoords(coords: LatLng): Boolean {
        // AlertDialog builder
        val dialogBuilder: AlertDialog? = this@ModifyMapActivity.let {
            val builder = AlertDialog.Builder(it)
            val input: EditText = EditText(this@ModifyMapActivity)

            builder.setView(R.layout.dialog_add_point)
                // Add actions buttond
                .setPositiveButton(R.string.add_a_point, DialogInterface.OnClickListener {
                        dialog, id -> createPoint(input.text.toString(), coords.latitude.toString(), coords.longitude.toString(), "TARGET" )
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id ->
                })
            builder.setView(R.layout.dialog_add_point)
            builder.setTitle(R.string.add_a_point)
            builder.create()
        }

        dialogBuilder?.show()
        return true
    }

    private fun checkChangeTitle() {
        if (oneMap!!.size > 0) {
            binding.editOrCreate.setText(R.string.edit_a_map)
            binding.createMapButton.setText(R.string.edit_a_map)
        } else {
            binding.editOrCreate.setText(R.string.create_a_map)
            binding.createMapButton.setText(R.string.create_a_map)
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
                    for (point in oneMap!!) {
                        createPoint(
                            point.name.toString(),
                            point.latitude.toString(),
                            point.longitude.toString(),
                            point.type.toString()
                        )
                    }
                    checkChangeTitle()
                    (application as MapApplication).setCurrentPoints(oneMap!!)
                } else {
                    Toast.makeText(
                        this@ModifyMapActivity,
                        "Error occurred : ${response?.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ModifyMapActivity,
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
                    checkChangeTitle()

                    Toast.makeText(
                        this@ModifyMapActivity,
                        "Create success",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ModifyMapActivity,
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