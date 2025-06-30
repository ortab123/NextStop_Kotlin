package com.example.final_nextstop.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.final_nextstop.R
import com.example.final_nextstop.databinding.MapLayoutBinding
import com.example.final_nextstop.ui.all_characters.ProfileFragmentDirections
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
            val geocoder = Geocoder(requireContext(), Locale.getDefault())

            posts.forEach { post ->
                try {
                    val addressList = geocoder.getFromLocationName(post.location, 1)
                    if (!addressList.isNullOrEmpty()) {
                        val address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)

                        val imageUri = post.images?.firstOrNull()?.takeIf { it.isNotBlank() }
                        val imageToLoad = imageUri
                            ?: "android.resource://${requireContext().packageName}/${R.drawable.ic_launcher_foreground}"

                        Glide.with(this)
                            .asBitmap()
                            .load(imageToLoad)
                            .override(100, 100)
                            .centerCrop()
                            .into(object : com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                                ) {
                                    val markerOptions = MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromBitmap(resource))

                                    val marker = googleMap.addMarker(markerOptions)
                                    marker?.tag = post.id // שמירת מזהה הפוסט בטאג
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(30.0, 0.0), 2f)
            )

            // מעבר לפוסט בלחיצה על התמונה (marker)
            googleMap.setOnMarkerClickListener { marker ->
                val postId = marker.tag as? Int
                val post = posts.find { it.id == postId }
                post?.let {
                    viewModel.setPost(it)
                    findNavController().navigate(
                        MapFragmentDirections.actionMapFragmentToDescriptionFragment("map")
                    )
                }
                true // מציין שטיפלנו בלחיצה (לא להראות info window)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
