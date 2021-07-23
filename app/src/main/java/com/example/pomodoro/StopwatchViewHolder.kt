package com.example.pomodoro

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.provider.Settings
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.pomodoro.databinding.StopwatchItemBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Period
import kotlinx.coroutines.delay as delay1

class StopwatchViewHolder(
    private val binding: StopwatchItemBinding,
    private val listener: StopwatchListener,
    //private val resources: Resources
): RecyclerView.ViewHolder(binding.root) {

    private var current = 0L
    private var timer: CountDownTimer? = null


    fun bind(stopwatch: Stopwatch) {
        //stopwatch.isStarted = true
        binding.stopwatchTimer.text = stopwatch.currentMinutes.displayTime()

        if (stopwatch.isStarted) {
            startTimer(stopwatch)
            /*binding.customViewOne.setPeriod(PERIOD)

            GlobalScope.launch {
                while (current < PERIOD* REPEAT){
                    current += UNIT_TEN_MS
                    binding.customViewOne.setCurrent(current)
                    delay1(UNIT_TEN_MS)
                }
            }*/
        } else {
            stopTimer(stopwatch)
        }

        initButtonsListeners(stopwatch)


    }

    private fun initButtonsListeners(stopwatch: Stopwatch) {
        binding.startStopButton.setOnClickListener {
            if (stopwatch.isStarted) {
                listener.stop(stopwatch.id, stopwatch.currentMinutes)
            } else {
                listener.start(stopwatch.id)
            }
        }

        binding.restartButton.setOnClickListener { listener.reset(stopwatch.id) }

        binding.deleteButton.setOnClickListener { listener.delete(stopwatch.id) }
    }

    private fun startTimer(stopwatch: Stopwatch) {

        val drawable = getDrawable(binding.root.context,R.drawable.ic_baseline_pause_24)
        //binding.startPauseButton.setImageDrawable(drawable)
        binding.startStopButton.text = "STOP"

        timer?.cancel()
        timer = getCountDownTimer(stopwatch)
        timer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(stopwatch: Stopwatch) {
        val drawable = getDrawable(binding.root.context, R.drawable.ic_baseline_play_arrow_24)
        //binding.startPauseButton.setImageDrawable(drawable)
        binding.startStopButton.text = "START"

        timer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(stopwatch: Stopwatch): CountDownTimer {
        return object : CountDownTimer(PERIOD, UNIT_TEN_MS) {
            val interval = UNIT_TEN_MS

            override fun onTick(millisUntilFinished: Long) {
                stopwatch.currentMinutes -= interval
                binding.stopwatchTimer.text = stopwatch.currentMinutes.displayTime()
            }

            @SuppressLint("ResourceAsColor")
            override fun onFinish() {
                binding.stopwatchTimer.text = stopwatch.currentMinutes.displayTime()
                binding.root.setBackgroundColor(R.color.purple_700)
                binding.startStopButton.text = "StArT"
                binding.blinkingIndicator.isInvisible = true
            }
        }
    }

    private fun Long.displayTime(): String {
        /*if (this >= PERIOD-300L) {
            return START_TIME
        }*/
        val h = this / 3600
        val m = this % 3600 / 60
        val s = this % 60


        /*val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60
        val ms = this % 1000 / 10*/

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"   //:${displaySlot(ms)}
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private companion object {

        private const val START_TIME = "00:00:00"
        private const val UNIT_TEN_MS = 50L
        private const val PERIOD  = 1000L * 5L /*60L * 60L * 24L*/ // Day
        private const val REPEAT = 10
    }
}