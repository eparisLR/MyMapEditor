package com.example.mymapeditor


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.example.mymapeditor.databinding.ActivityListMapBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Point
import service.MapApiClient
import java.lang.Exception

class ListMap : AppCompatActivity() {
    private lateinit var binding: ActivityListMapBinding
    private var maps: List<String>? = emptyList()
    private var oneMap: MutableList<Point>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMapBinding.inflate(layoutInflater)

        setContentView(binding.root);

        getMaps()
    }

    private fun getMaps() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = MapApiClient.mapApiService.getMaps()
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    maps = content
                    println(maps)
                    val listView = findViewById<ListView>(R.id.listview)
                    listView.adapter = maps?.let { MapAdapter(this@ListMap, it) }
                } else {
                    Toast.makeText(
                        this@ListMap,
                        "Error occurred : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ListMap,
                    "Error occurred : ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getOneMap(){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = MapApiClient.mapApiService.getMapById(1)
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    oneMap = content
                    println(maps)
                } else {
                    Toast.makeText(
                        this@ListMap,
                        "Error occurred : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ListMap,
                    "Error occurred : ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}