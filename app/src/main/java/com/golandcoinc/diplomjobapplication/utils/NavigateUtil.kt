package com.golandcoinc.diplomjobapplication.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.golandcoinc.diplomjobapplication.R

object NavigateUtil {
    var currentFragment: Fragment? = null

    fun navigateTo(
        parentFragmentManager: FragmentManager,
        fragment: Fragment
    ) {
        currentFragment = fragment
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.main_container, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            commit()
        }
    }
}