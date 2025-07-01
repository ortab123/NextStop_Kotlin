package com.example.final_nextstop.ui.all_characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.final_nextstop.data.model.PostImage
import com.example.final_nextstop.R
import com.example.final_nextstop.databinding.GalleryLayoutBinding
import com.example.final_nextstop.ui.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.final_nextstop.util.autoCleared

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding : GalleryLayoutBinding by autoCleared()
    private val binding get() = _binding
    private val viewModel : PostsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.posts?.observe(viewLifecycleOwner) { postList ->

            val imageList = postList.filter { !it.images.isNullOrEmpty() }
                .flatMapIndexed { postIndex, post ->
                    post.images?.mapIndexedNotNull { imageIndex, imageUrl ->
                        if (imageUrl.isNotBlank()) PostImage(imageUrl, postIndex, imageIndex) else null
                    } ?: emptyList()
                }

            val adapter = GalleryImageAdapter(imageList) { image ->
                val post = postList[image.postIndex]
                viewModel.setPost(post)
                val action = GalleryFragmentDirections
                    .actionGalleryFragmentToDescriptionFragment(source = getString(R.string.gallery))
                findNavController().navigate(action)

            }


        binding.recyclerImages.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerImages.adapter = adapter
    }
}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GalleryLayoutBinding.inflate(inflater, container, false)


        binding.btnBackFromGallery.setOnClickListener {
            findNavController().navigate(R.id.action_galleryFragment_to_profileFragment)
      }
        return binding.root
    }

}