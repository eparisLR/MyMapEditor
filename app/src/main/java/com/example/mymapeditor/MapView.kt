package com.example.mymapeditor;

import android.content.Context;
import android.content.Intent
import android.widget.Button
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Point
import service.MapApiClient
import java.lang.Exception

class MapView(context: ListMapActivity) : FrameLayout(context) {

    private var map: String? = null;
    private var contextInMapView: ListMapActivity = context;

    init {
        inflate(context, R.layout.view_map, this)
        findViewById<Button>(R.id.buttonSelectMap).setOnClickListener {
            val intent = Intent( context, ModifyMap::class.java)
            // Cette ligne sert à injecter des paramètres UTILE POUR LA MODIF D'UNE CARTE
            intent.putExtra(ModifyMap.EXTRA_MAP_NAME , findViewById<TextView>(R.id.mapName).text);
            context.startActivity(intent)
            context.finish()
        }

        findViewById<Button>(R.id.buttonDeleteMap).setOnClickListener {
            deleteMap()
        }
    }
    fun populate(map: String?){
        this.map = map;
        findViewById<TextView>(R.id.mapName).setText(map)
    }

    private fun deleteMap() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val mapName: String = findViewById<TextView>(R.id.mapName).text.toString()
                val response = MapApiClient.mapApiService.deleteMap(mapName)
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        contextInMapView,
                        "Delete success",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        contextInMapView,
                        "Error occurred : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {

            }
        }
    }
}