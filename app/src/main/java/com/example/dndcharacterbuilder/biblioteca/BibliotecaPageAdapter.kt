package com.example.dndcharacterbuilder.biblioteca

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.FragmentActivity

class BibliotecaPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BibliotecaFragment("spells") // Hechizos
            1 -> BibliotecaFragment("feats") // Feats
            2 -> BibliotecaFragment("equipment") // Equipo
            3 -> BibliotecaFragment("classes") // Clases
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}


