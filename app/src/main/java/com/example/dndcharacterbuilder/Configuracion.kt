package com.example.dndcharacterbuilder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.slider.Slider
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.dndcharacterbuilder2.R

class Configuracion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SettingsAdapter()
    }

    class SettingsAdapter : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

        private val settings = listOf(
            "Tamaño de letra",
            "Modo oscuro",
            "Modo daltonismo"
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_configuracion, parent, false)
            return SettingsViewHolder(view)
        }

        override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
            val setting = settings[position]
            holder.bind(setting, position)
        }

        override fun getItemCount(): Int = settings.size

        class SettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title: TextView = itemView.findViewById(R.id.settingTitle)
            private val slider: Slider = itemView.findViewById(R.id.settingSlider)
            private val switch: SwitchMaterial = itemView.findViewById(R.id.settingSwitch)

            fun bind(setting: String, position: Int) {
                title.text = setting
                when (position) {
                    0 -> { // Tamaño de letra
                        slider.visibility = View.VISIBLE
                        switch.visibility = View.GONE
                        slider.valueFrom = 10f
                        slider.valueTo = 30f
                        slider.stepSize = 1f
                        slider.value = 14f // Valor predeterminado
                    }
                    1 -> { // Modo oscuro
                        slider.visibility = View.GONE
                        switch.visibility = View.VISIBLE
                        switch.isChecked = false // Valor predeterminado
                    }
                    2 -> { // Modo daltonismo
                        slider.visibility = View.GONE
                        switch.visibility = View.VISIBLE
                        switch.isChecked = false // Valor predeterminado
                    }
                }
            }
        }
    }
}
