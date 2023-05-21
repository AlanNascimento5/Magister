package com.example.magister.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = listOf(
        ConversasFragment(),
        BuscarFragment(),
        PerfilFragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Conversas"
            1 -> "Buscar"
            2 -> "Perfil"
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}


