package com.example.mymapeditor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;



import model.Point;

class MapAdapter(context: Context, maps:List<String>) : ArrayAdapter<String>(context, 0, maps) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mapView = MapView(context)
        mapView.populate(getItem(position));
        return mapView
    }
}