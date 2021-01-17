package com.example.bottomlinelocations.ui.detailSiteDefect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bottomlinelocations.API.GlideApp
import com.example.bottomlinelocations.MyApplication
import com.example.bottomlinelocations.R
import com.example.bottomlinelocations.data.SiteDefects
import com.example.bottomlinelocations.databinding.FragmentDetailSiteDefectsBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetailSiteDefectsFragment : Fragment() {

    private val detailSiteDefectsViewModel: DetailSiteDefectsViewModel by viewModels {
        val application = requireActivity().application as MyApplication
        val siteDefectRepository = application.siteDefectRepository
        DetailSiteDefectsViewModelFactory(siteDefectRepository, siteId)
    }

    private var siteId: String = ""
    private lateinit var image: ImageView
    private lateinit var binding: FragmentDetailSiteDefectsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Get siteId from arguments
            siteId = DetailSiteDefectsFragmentArgs.fromBundle(requireArguments()).siteId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_detail_site_defects,
                container,
                false
            )

        val nameObserver = Observer<SiteDefects> { newName ->
            // Update picId
            val picId = newName.picId
            image = binding.imageView2
            downloadImage(picId)
        }
        // Observe the LiveData, passing in this fragment as the LifecycleOwner and the observer.
        detailSiteDefectsViewModel.siteDefect.observe(requireActivity(), nameObserver)

        val dataObserver = androidx.lifecycle.Observer<SiteDefects> {
            binding.siteDefects = detailSiteDefectsViewModel.siteDefect.value
        }
        detailSiteDefectsViewModel.siteDefect.observe(requireActivity(), dataObserver)
        binding.siteDefects = detailSiteDefectsViewModel.siteDefect.value

        // Delete record
        binding.buttonDelete.setOnClickListener {
            GlobalScope.launch {
                detailSiteDefectsViewModel.deleteSingle(siteId)
            }
            Toast.makeText(context,"Site defect deleted",Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_detailSiteDefectsFragment_to_nav_listSiteDefects)
        }
        return binding.root
    }

    private fun downloadImage(picId: String) {
        val storage = FirebaseStorage.getInstance()
        // Create a reference to a file from a Google Cloud Storage URI
        val gsReference =
            storage.getReferenceFromUrl("gs://bottomlinelocations.appspot.com/uploads/sitedefect$picId")
        context?.let {
            GlideApp.with(it)
                .load(gsReference)
                .into(image)
        }
    }
}