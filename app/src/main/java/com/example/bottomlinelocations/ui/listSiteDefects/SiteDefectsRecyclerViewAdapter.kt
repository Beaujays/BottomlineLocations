package com.example.bottomlinelocations.ui.listSiteDefects

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomlinelocations.ui.MainActivity
import com.example.bottomlinelocations.R
import com.example.bottomlinelocations.data.SiteDefects


class SiteDefectsRecyclerViewAdapter(
    private val activity: MainActivity,
    private val siteDefects: LiveData<List<SiteDefects>>
) : RecyclerView.Adapter<SiteDefectsRecyclerViewAdapter.ViewHolder>() {

    init {
        val dataObserver = Observer<List<SiteDefects>> {
            this.notifyDataSetChanged()
        }

        siteDefects.observe(activity, dataObserver)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val siteNameText: TextView = view.findViewById(R.id.siteNameText)
        val timestampText: TextView = view.findViewById(R.id.timestampText)
        private var navController: NavController? = null
        var id: String? = null

        init {
            view.setOnClickListener {
                val id: String = id ?: throw IllegalStateException()
                navController = Navigation.findNavController(view)
                navController!!.navigate(
                    ListSiteDefectsFragmentDirections.actionNavListSiteDefectsToDetailSiteDefectsFragment(
                    id) // Set Id argument
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.view_site_defects_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("SiteDefectsRecyclerView", "onBindViewHolder: $position")
        val siteDefect = siteDefects.value?.get(position) ?: throw java.lang.IllegalStateException()

        // makes title of subject in list
        holder.id = siteDefect.id
        holder.siteNameText.text = siteDefect.siteName
        holder.timestampText.text = siteDefect.timestamp
    }

    override fun getItemCount(): Int = siteDefects.value?.size ?: 0
}
