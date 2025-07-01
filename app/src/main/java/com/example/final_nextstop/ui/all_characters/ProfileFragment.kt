package com.example.final_nextstop.ui.all_characters

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.final_nextstop.R
import com.example.final_nextstop.databinding.ProfileLayoutBinding
import com.example.final_nextstop.ui.PostsViewModel
import android.view.animation.AnimationUtils
import com.example.final_nextstop.data.model.Post
import dagger.hilt.android.AndroidEntryPoint
import com.example.final_nextstop.util.autoCleared

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: ProfileLayoutBinding by autoCleared()
    private val binding get() = _binding
    private val viewModel: PostsViewModel by activityViewModels()
    private lateinit var adapter: PostAdapter

    private val pickImageLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide.with(requireContext())
                    .load(it)
                    .transform(CircleCrop())
                    .into(binding.imgMyProfile)

                val firstPost = viewModel.posts?.value?.firstOrNull()
                if (firstPost != null) {
                    viewModel.updateProfileImageUri(firstPost.id, it.toString())
                }

                val rotateAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
                binding.imgMyProfile.startAnimation(rotateAnim)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.myToolbar)

        val profileImageUri = viewModel.posts?.value?.firstOrNull()?.profileImageUri
        if (!profileImageUri.isNullOrBlank()) {
            Glide.with(requireContext())
                .load(profileImageUri)
                .transform(CircleCrop())
                .into(binding.imgMyProfile)
        }

            adapter = PostAdapter(object : PostAdapter.PostListener {
                override fun onPostClicked(index: Int) {
                    val post = adapter.postAt(index)
                    viewModel.setPost(post)
                    findNavController().navigate(
                        ProfileFragmentDirections.actionProfileFragmentToDescriptionFragment("profile")
                    )
                }

                override fun onFavoriteClicked(post: Post) {
                    viewModel.setFavorite(post.id, !post.isFavorite)
                }
            })
            binding.recyclerPosts.adapter = adapter
            binding.recyclerPosts.layoutManager = LinearLayoutManager(requireContext())


        viewModel.posts?.observe(viewLifecycleOwner) {
            val sortedPosts = it.sortedByDescending { post -> post.date }
            adapter.setPosts(sortedPosts)

            val updatedProfileUri = viewModel.posts?.value?.firstOrNull()?.profileImageUri
            if (!updatedProfileUri.isNullOrBlank()) {
                Glide.with(requireContext())
                    .load(updatedProfileUri)
                    .transform(CircleCrop())
                    .into(binding.imgMyProfile)

            }

            if (sortedPosts.isNotEmpty()) {
                binding.recyclerPosts.visibility = View.VISIBLE
                binding.textViewNoPosts.visibility = View.GONE
                binding.textSwipeHint.visibility = View.VISIBLE
            }
            else {
                binding.recyclerPosts.visibility = View.GONE
                binding.textViewNoPosts.visibility = View.VISIBLE
                binding.textSwipeHint.visibility = View.GONE
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val post = (binding.recyclerPosts.adapter as PostAdapter).postAt(viewHolder.adapterPosition)
                val dialog = DeletePostConfirmationDialogFragment.newInstance()
                dialog.onDeletePostConfirmed = {
                    viewModel.deletePost(post)
                    Toast.makeText(requireContext(),
                        getString(R.string.the_post_deleted), Toast.LENGTH_SHORT).show()
                }
                dialog.show(parentFragmentManager, "DeleteDialog")

                (binding.recyclerPosts.adapter as PostAdapter).notifyItemChanged(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recyclerPosts)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete -> {
                        val dialog = DeleteAllPostsConfirmationDialogFragment.newInstance()
                        dialog.onDeleteAllPostsConfirmed = {
                            viewModel.deleteAll()
                            Toast.makeText(requireContext(),
                                getString(R.string.all_posts_deleted), Toast.LENGTH_SHORT).show()
                        }
                        dialog.show(parentFragmentManager, "DeleteDialog")

                        true
                    }

                    R.id.action_add -> {
                        findNavController().navigate(R.id.action_profileFragment_to_addPostFragment)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)

        binding.imgMyProfileEdit.setOnClickListener {
            pickImageLauncher.launch("image/*")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileLayoutBinding.inflate(inflater, container, false)

        binding.btnAlbum.setOnClickListener {
            val bounceAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_bounce)

            bounceAnim.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}

                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    findNavController().navigate(R.id.action_profileFragment_to_galleryFragment)
                }

                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            })

            binding.btnAlbum.startAnimation(bounceAnim)
        }
        return binding.root
    }
}
