package com.example.mymapeditor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

class MapAdapter(context: ListMapActivity, maps:List<String>) : ArrayAdapter<String>(context, 0, maps) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mapView = MapView(context as ListMapActivity)
        mapView.populate(getItem(position));
        return mapView
    }
}