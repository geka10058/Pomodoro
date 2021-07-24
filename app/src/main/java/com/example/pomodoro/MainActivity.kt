package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pomodoro.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), StopwatchListener {

    private lateinit var binding: ActivityMainBinding

    private val stopwatchAdapter = StopwatchAdapter(this)
    private val stopwatches = mutableListOf<Stopwatch>()
    private var nextId = 0
    private var field: EditText? = null
    private var current = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        field = binding.textField

        //if (binding.textField.text.isEmpty()) minutes = binding.textField.text.toString().toLong()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }

        binding.addNewStopwatchButton.setOnClickListener {
            when {
                binding.textField.text.isEmpty() -> Toast.makeText(
                    this,
                    "Поле пусто. Введите время в минутах",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    current = field?.text.toString().toLong()*60000
                    stopwatches.add(Stopwatch(nextId++, current, false, current))
                    stopwatchAdapter.submitList(stopwatches.toList())
                }
            }
        }
    }

    override fun start(id: Int) {
        changeStopwatch(id, null, true, 0L)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeStopwatch(id, currentMs, false, 0L)
    }

    override fun reset(id: Int) {
        changeStopwatch(id, null, false, 0L)
    }

    override fun delete(id: Int) {
        stopwatches.remove(stopwatches.find { it.id == id })
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean, initTine: Long) {
        val newTimers = mutableListOf<Stopwatch>()
        stopwatches.forEach {
            if (it.id == id) {
                newTimers.add(Stopwatch(it.id, currentMs ?: it.currentInMs, isStarted, 0L))
            } else {
                newTimers.add(it)
            }

        }
        stopwatchAdapter.submitList(newTimers)
        stopwatches.clear()
        stopwatches.addAll(newTimers)
    }

}