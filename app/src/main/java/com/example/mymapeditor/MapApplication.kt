package com.example.mymapeditor

import android.app.Application
import model.Point
import model.Type

class MapApplication : Application() {
    private var currentPoint: Point = Point("", Type.TARGET, 0.0, 0.0)
    private var currentPoints: MutableList<Point> = arrayListOf()
    private var currentMapName: String = ""

    fun setCurrentPoints(points: MutableList<Point>) {
        currentPoints = points
    }

    fun getCurrentPoints(): MutableList<Point> {
        return currentPoints
    }

    fun setCurrentMapName(name: String?) {
        currentMapName = name.toString()
    }

    fun getCurrentMapName(): String {
        return currentMapName
    }
}