package com.git.alquilalotodo.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.git.alquilalotodo.ui.fragment.perfil.ProfileProductsFragment
import com.git.alquilalotodo.ui.fragment.perfil.ProfileValorationsFragment

class ViewPagerAdapter(fm: FragmentManager, var tabCount: Int,private val userId: String) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = ProfileProductsFragment()
                val bundle = Bundle()
                bundle.putString("userId", userId)
                fragment.arguments = bundle
                fragment}
            1 -> ProfileValorationsFragment()
            else -> ProfileProductsFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Tab " + (position + 1)
    }
}
