package com.example.final_nextstop.ui

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.final_nextstop.R
import com.example.final_nextstop.databinding.MapLayoutBinding
import com.example.final_nextstop.ui.PostsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: MapLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private val viewModel: PostsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapLayoutBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        viewModel.posts?.observe(viewLifecycleOwner) { posts ->
            posts.forEach { post ->
                val locationName = post.location
                val geocoder = Geocoder(requireContext(), Locale.getDefault())

                try {
                    val addressList = geocoder.getFromLocationName(locationName, 1)
                    if (!addressList.isNullOrEmpty()) {
                        val address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)

                        googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(post.place ?: locationName)
                                .snippet(post.description)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(30.0, 0.0), 2f)
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}