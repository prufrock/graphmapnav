package com.dkanen.graphmapnav.game

enum class Services(val v: String) {
    Ios("iOS"), Api("api"), Database("database");

    override fun toString(): String = v
}