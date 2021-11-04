package com.unlam.tupartidito.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.ui.detail_club.DetailClubFragment
import com.unlam.tupartidito.ui.detail_club.ScheduleClubFragment

class TabClubAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val ID_CLUB = "idClub"

    lateinit var idClub: String
    fun setDataClub(clubData: String) {
        idClub = clubData
    }

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = ScheduleClubFragment()
                val args = Bundle()
                args.putString(ID_CLUB,idClub)
                fragment.arguments = args
                return  fragment
            }
            1 -> DetailClubFragment()
            else -> ScheduleClubFragment()
        }
    }
//    lateinit var club : Club
//    fun setDataClub(clubData:Club){
//        club = clubData
//    }
//    override fun getCount(): Int {
//        return totalTabs
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return when (position) {
//            0 -> ScheduleClubFragment(club)
//            1 -> DetailClubFragment(club)
//            else -> getItem(position)
//        }
//    }

}