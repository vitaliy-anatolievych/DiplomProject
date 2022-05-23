package com.golandcoinc.diplomjobapplication.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.golandcoinc.diplomjobapplication.R
import com.golandcoinc.domain.entities.data.StatsData

class StatsAdapter: RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    var list = listOf<StatsData>()

    inner class StatsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvMedianSpeed = view.findViewById<TextView>(R.id.tv_speed)
        val tvTotalDistance = view.findViewById<TextView>(R.id.tv_distance)
        val tvCountFuel = view.findViewById<TextView>(R.id.tv_count_fuel)
        val tvFuelConsumption = view.findViewById<TextView>(R.id.tv_fuel_consumption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        return StatsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.table_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        with(holder) {
            tvMedianSpeed.text =  String.format("%.1f",list[position].medianSpeedForTravel)
            tvTotalDistance.text =  String.format("%.2f",list[position].totalDistanceTraveled)
            tvCountFuel.text =  list[position].fuelVolume.toString()
            tvFuelConsumption.text = String.format("%.2f", list[position].fuelConsumption)
        }
    }

    override fun getItemCount(): Int = list.size

}