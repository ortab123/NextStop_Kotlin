package com.example.final_nextstop.ui.single_characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.final_nextstop.R
import com.example.final_nextstop.databinding.DescriptionLayoutBinding
import com.example.final_nextstop.ui.PostsViewModel
import com.example.final_nextstop.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DescriptionFragment : Fragment() {
    private var _binding : DescriptionLayoutBinding by autoCleared()
    private val viewModel : PostsViewModel by activityViewModels()
    private val binding get() = _binding
    private val args: DescriptionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DescriptionLayoutBinding.inflate(inflater, container, false)
        binding.imageViewEditPost.setOnClickListener {
            val action = DescriptionFragmentDirections.actionDescriptionFragmentToEditDescriptionFragment(args.source)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenPost.observe(viewLifecycleOwner){
            binding.txtViewPostLocation.text = it.location
            binding.txtViewPostPlace.text = it.place
            binding.txtViewPostDescription.text = it.description
            binding.txtViewDateDescription.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                Date(it.date)
            )

            val validImages = it.images?.filter { imageUrl -> imageUrl.isNotBlank() } ?: emptyList()

            if (validImages.isNotEmpty()) {
                val adapter = DescriptionImagesAdapter(validImages)
                binding.viewPagerImages.adapter = adapter
                binding.indicator.setViewPager(binding.viewPagerImages)

                binding.viewPagerImages.offscreenPageLimit = 1
                binding.viewPagerImages.clipToPadding = false
                binding.viewPagerImages.clipChildren = false
                binding.viewPagerImages.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                val transformer = androidx.viewpager2.widget.CompositePageTransformer().apply {
                    addTransformer(androidx.viewpager2.widget.MarginPageTransformer(1))
                }

                binding.viewPagerImages.setPageTransformer(transformer)

                binding.viewPagerImages.visibility = View.VISIBLE
                binding.indicator.visibility = View.VISIBLE
            } else {
                binding.viewPagerImages.visibility = View.GONE
                binding.indicator.visibility = View.GONE
            }
        }

        val source = args.source
        binding.btnBackFromDescription.setOnClickListener {
            when (source) {
                "favorite" -> findNavController().navigate(R.id.favoritePostsFragment)//
                "profile" -> findNavController().navigate(R.id.profileFragment)
                "gallery" -> findNavController().navigate(R.id.galleryFragment)
                "map" -> findNavController().navigate(R.id.mapFragment)
                "editDescription" -> findNavController().navigate(R.id.editDescriptionFragment)
                else -> findNavController().popBackStack()
            }
        }
    }
}