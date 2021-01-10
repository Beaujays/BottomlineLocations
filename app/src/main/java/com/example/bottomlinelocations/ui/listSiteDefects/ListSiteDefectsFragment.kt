package com.example.bottomlinelocations.ui.listSiteDefects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomlinelocations.ui.MainActivity
import com.example.bottomlinelocations.MyApplication
import com.example.bottomlinelocations.R
import com.example.bottomlinelocations.databinding.FragmentListSiteDefectsBinding

class ListSiteDefectsFragment : Fragment() {
    private val listSiteDefectsViewModel: ListSiteDefectsViewModel by viewModels {
        val application = requireActivity().application as MyApplication
        val siteDefectRepository = application.siteDefectRepository
        ListSiteDefectsViewModelFactory(siteDefectRepository)
    }

    private lateinit var binding: FragmentListSiteDefectsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_site_defects, container, false)
        Log.i("ListDefectsFragment", "OnCreateView: ${listSiteDefectsViewModel.allSiteDefects}")
        val adapter = SiteDefectsRecyclerViewAdapter(
            activity as MainActivity,
            listSiteDefectsViewModel.allSiteDefects
        )
        binding.siteDefectRecycler.setHasFixedSize(true)
        binding.siteDefectRecycler.layoutManager = LinearLayoutManager(activity)
        binding.siteDefectRecycler.adapter = adapter

        // Navigate to add site fragment
        binding.addNewSiteDefectButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_listSiteDefects_to_nav_addSiteDefect)
        }

        return binding.root
    }
}