package com.example.mymapeditor


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.example.mymapeditor.databinding.ListMapActivityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import service.MapApiClient
import java.lang.Exception

class ListMapActivity : AppCompatActivity() {
    private lateinit var binding: ListMapActivityBinding
    private var maps: List<String>? = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListMapActivityBinding.inflate(layoutInflater)

        setContentView(binding.root);

        getMaps()

        binding.createMap.setOnClickListener {
            val intent = Intent( this, ModifyMapActivity::class.java)
            // Cette ligne sert à injecter des paramètres UTILE POUR LA MODIF D'UNE CARTE
            intent.putExtra(ModifyMapActivity.EXTRA_MAP_NAME , "")
            startActivity(intent)
            finish()
        }
    }

    private fun getMaps() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = MapApiClient.mapApiService.getMaps()
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    maps = content
                    val listView = findViewById<ListView>(R.id.listview)
                    listView.adapter = maps?.let { MapAdapter(this@ListMapActivity, it) }
                } else {
                    Toast.makeText(
                        this@ListMapActivity,
                        "Error occurred : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ListMapActivity,
                    "Error occurred : ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}