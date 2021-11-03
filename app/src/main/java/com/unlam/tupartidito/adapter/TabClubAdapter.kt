package com.unlam.tupartidito.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.ui.detail_club.DetailClubFragment
import com.unlam.tupartidito.ui.detail_club.ScheduleClubFragment

class TabClubAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) : FragmentPagerAdapter(fm) {
    lateinit var club : Club
    fun setDataClub(clubData:Club){
        club = clubData
    }
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ScheduleClubFragment(club)
            1 -> DetailClubFragment(club)
            else -> getItem(position)
        }
    }

}