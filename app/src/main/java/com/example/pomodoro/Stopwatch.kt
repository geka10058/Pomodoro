package com.example.pomodoro

data class Stopwatch(
    //val time: String,
    val id: Int,
    var currentInMs: Long,
    var isStarted: Boolean,
    var initTime: Long
)
