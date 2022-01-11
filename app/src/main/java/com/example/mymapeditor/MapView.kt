package com.example.mymapeditor;

import android.content.Context;
import android.graphics.Point
import android.widget.FrameLayout;
import android.widget.TextView;

class MapView(context: Context) : FrameLayout(context) {
    init {
        inflate(context, R.layout.view_map, this)
    }
    fun populate(map: String?){
        findViewById<TextView>(R.id.name).setText(map)
    }
}