package com.pruebas.earthquakemonitor

class Geometry(val coordinates: Array<Double>) {
    val longitud: Double
        get() = coordinates[0]

    val latitud: Double
        get() = coordinates[1]
}