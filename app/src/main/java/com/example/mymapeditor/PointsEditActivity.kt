package com.example.mymapeditor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.mymapeditor.databinding.ActivityPointsEditActivityBinding
import model.Point

class PointsEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPointsEditActivityBinding
    private lateinit var listView: ListView
    private lateinit var points: MutableList<Point>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointsEditActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        listView = binding.pointsListview

        points = (application as MapApplication).getCurrentPoints()
        listView.adapter = PointAdapter(this@PointsEditActivity, points)

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ModifyMapActivity::class.java)
            var mapName = (application as MapApplication).getCurrentMapName()
            intent.putExtra(ModifyMapActivity.EXTRA_MAP_NAME , mapName)
            startActivity(intent)
            finish()
        }
    }
}