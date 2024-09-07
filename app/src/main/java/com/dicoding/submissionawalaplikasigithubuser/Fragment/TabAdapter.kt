package com.dicoding.submissionawalaplikasigithubuser.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter(fragmentmanager: FragmentManager, lifecycle: Lifecycle, val data: String): FragmentStateAdapter(fragmentmanager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = if (position == 0)
            FollowerFragment()
        else
            FollowingFragment()

        val bundle = Bundle()
        bundle.putString("user", data)
        fragment.arguments = bundle

        return fragment
    }
}