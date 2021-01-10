package com.example.bottomlinelocations.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.bottomlinelocations.ui.MainActivity
import com.example.bottomlinelocations.MyApplication
import com.example.bottomlinelocations.R
import com.example.bottomlinelocations.data.Settings
import com.example.bottomlinelocations.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels {
        val application = requireActivity().application as MyApplication
        val settingsRepository = application.settingsRepository
        HomeViewModelFactory(settingsRepository)
    }
    private lateinit var binding: FragmentHomeBinding
    private var languageNL: String = "nl"
    private var languageEN: String = "en"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // Log out from app
        binding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            requireActivity().recreate()
        }

        // Set language to dutch
        binding.buttonLanguage1.setOnClickListener {
            val settings = Settings(0, languageNL)
            homeViewModel.insert(settings)
            (activity as MainActivity).setLocale(languageNL)
        }

        // Set language to english
        binding.buttonLanguage2.setOnClickListener {
            val settings = Settings(0, languageEN)
            homeViewModel.insert(settings)
            (activity as MainActivity).setLocale(languageEN)
        }

        // Get last chosen language
        binding.buttonGetLastLanguage.setOnClickListener {
            val languageObserver = Observer<Settings> { newLanguage ->
                // Update value last language
                val lastLanguage = newLanguage.language
                (activity as MainActivity).setLocale(lastLanguage)
                Log.i("HomeFragment", "Language is $lastLanguage")
            }
            homeViewModel.lastLanguage.observe(requireActivity(), languageObserver)
        }
        return binding.root
    }
}


