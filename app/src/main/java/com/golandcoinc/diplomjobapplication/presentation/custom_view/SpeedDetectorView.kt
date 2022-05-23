package com.golandcoinc.diplomjobapplication.presentation.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.golandcoinc.diplomjobapplication.R
import com.golandcoinc.diplomjobapplication.databinding.SpeedDetectorBinding

class SpeedDetectorView(
    context: Context,
    attributes: AttributeSet?
) : ConstraintLayout(context, attributes) {

    private val binding: SpeedDetectorBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.speed_detector, this, true)
        binding = SpeedDetectorBinding.bind(this)
    }

    fun setSpeed(speed: Int?) {
        binding.tvDetectorRecommendSpeed.text = speed.toString()
    }

    fun activateArrowUp() = with(binding) {
        imArrowDown.visibility = View.INVISIBLE
        imArrowUp.visibility = View.VISIBLE
    }

    fun hideArrow() = with(binding) {
        imArrowDown.visibility = View.INVISIBLE
        imArrowUp.visibility = View.INVISIBLE
    }

    fun activateArrowDown() = with(binding) {
        imArrowDown.visibility = View.VISIBLE
        imArrowUp.visibility = View.INVISIBLE
    }
}