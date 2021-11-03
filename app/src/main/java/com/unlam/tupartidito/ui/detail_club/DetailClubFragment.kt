package com.unlam.tupartidito.ui.detail_club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unlam.tupartidito.R
import com.unlam.tupartidito.data.model.club.Club

class DetailClubFragment(clubData: Club?) : Fragment() {

    private val club: Club? = clubData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_club,container,false)
        return view
    }

}