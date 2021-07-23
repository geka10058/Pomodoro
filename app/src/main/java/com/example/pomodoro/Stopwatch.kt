package com.example.pomodoro

data class Stopwatch(
    //val time: String,
    val id: Int,
    var currentMinutes: Long,
    var isStarted: Boolean
)
