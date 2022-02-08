package com.example.mymapeditor

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import model.Point

class PointAdapter(context: PointsEditActivity, points: List<Point>): ArrayAdapter<Point>(context, 0, points) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val pointsView = PointView(context as PointsEditActivity)
        pointsView.populate(getItem(position))
        return pointsView
    }
}