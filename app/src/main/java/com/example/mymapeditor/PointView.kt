package com.example.mymapeditor

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import model.Point

class PointView(context: PointsEditActivity) : FrameLayout(context) {
    init {
        inflate(context, R.layout.view_point, this)
    }

    fun populate(point: Point?){
        findViewById<TextView>(R.id.point_string_name).setText(point?.name)
    }
}