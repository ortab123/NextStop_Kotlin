package com.example.final_nextstop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_nextstop.databinding.FavoritePostsLayoutBinding
import com.example.final_nextstop.ui.all_characters.PostAdapter
import com.example.final_nextstop.ui.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.final_nextstop.data.model.Post
import com.example.final_nextstop.ui.all_characters.ProfileFragmentDirections
import com.example.final_nextstop.util.autoCleared


@AndroidEntryPoint
class FavoritePostsFragment : Fragment() {

    private var _binding: FavoritePostsLayoutBinding by autoCleared()
    private val binding get() = _binding!!
    private val viewModel: PostsViewModel by activityViewModels()
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritePostsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PostAdapter(object : PostAdapter.PostListener {
            override fun onPostClicked(index: Int) {
                val post = adapter.postAt(index)
                viewModel.setPost(post)
                findNavController().navigate(
                    FavoritePostsFragmentDirections.actionFavoritePostsFragmentToDescriptionFragment("favorite")
                )
            }

            override fun onFavoriteClicked(post : Post) {
                viewModel.setFavorite(post.id, !post.isFavorite)
            }
        })

        binding.recyclerFavoritePosts.adapter = adapter
        binding.recyclerFavoritePosts.layoutManager = LinearLayoutManager(requireContext())

        viewModel.posts?.observe(viewLifecycleOwner) { allPosts ->
            val favoritePosts = allPosts.filter { it.isFavorite }
            adapter.setPosts(favoritePosts)

            if (favoritePosts.isNotEmpty()) {
                binding.recyclerFavoritePosts.visibility = View.VISIBLE
                binding.textViewNoPosts.visibility = View.GONE
            }
            else {
                binding.recyclerFavoritePosts.visibility = View.GONE
                binding.textViewNoPosts.visibility = View.VISIBLE
            }
        }
    }
}