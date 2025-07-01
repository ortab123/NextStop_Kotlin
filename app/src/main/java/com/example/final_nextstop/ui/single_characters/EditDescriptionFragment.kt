package com.example.final_nextstop.ui.single_characters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.final_nextstop.R
import com.example.final_nextstop.data.model.Post
import com.example.final_nextstop.databinding.EditDescriptionLayoutBinding
import com.example.final_nextstop.ui.PostsViewModel
import com.example.final_nextstop.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditDescriptionFragment : Fragment() {
    private var _binding : EditDescriptionLayoutBinding by autoCleared()
    private val viewModel : PostsViewModel by activityViewModels()
    private val binding get() = _binding
    private var editableImages = mutableListOf<String>()
    private lateinit var editImagesAdapter: EditableImagesAdapter
    private val args: EditDescriptionFragmentArgs by navArgs()


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uriStr = uri.toString()

            val wasEmpty = editableImages.isEmpty()
            editableImages.add(uriStr)


            if (wasEmpty) {
                binding.editViewPagerImages.adapter = editImagesAdapter

                binding.editViewPagerImages.offscreenPageLimit = 1
                binding.editViewPagerImages.clipToPadding = false
                binding.editViewPagerImages.clipChildren = false
                binding.editViewPagerImages.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                val transformer = androidx.viewpager2.widget.CompositePageTransformer().apply {
                    addTransformer(androidx.viewpager2.widget.MarginPageTransformer(1))
                }
                binding.editViewPagerImages.setPageTransformer(transformer)
            }

            binding.editIndicator.setViewPager(binding.editViewPagerImages)


            binding.editViewPagerImages.visibility = View.VISIBLE
            binding.editIndicator.visibility = View.VISIBLE

            editImagesAdapter.notifyDataSetChanged()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditDescriptionLayoutBinding.inflate(inflater, container, false)


        binding.btnBackFromEditDescription.setOnClickListener {
            findNavController().navigate(R.id.action_editDescriptionFragment_to_descriptionFragment)
        }
        binding.btnAddImage.setOnClickListener {
            if (editableImages.size >= 10) {
                Toast.makeText(requireContext(),
                    getString(R.string.maximum_10_images), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            pickImageLauncher.launch(arrayOf("image/*"))
        }


        binding.mbSavePost.setOnClickListener {

            val isFavoritePost = viewModel.chosenPost.value.isFavorite
            val selectedLocation = binding.spinnerCountriesEdit.selectedItem.toString()
            val currentDescription = binding.editTextEditPostDescription.text.toString()

            if (selectedLocation == getString(R.string.select_location)) {
                Toast.makeText(requireContext(), getString(R.string.please_select_location), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentDescription.isNullOrEmpty() ) {
                Toast.makeText(requireContext(), getString(R.string.please_write_description), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val originalPost = viewModel.chosenPost.value
            val updatedPost = Post(
                id = originalPost!!.id,
                location = binding.spinnerCountriesEdit.selectedItem.toString(),
                place = binding.editTxtEditPostPlace.text.toString(),
                description = binding.editTextEditPostDescription.text.toString(),
                images = editableImages.filterNotNull(),
                profileImageUri = viewModel.profileImageUri.value,
                date = originalPost.date,
                isFavorite = isFavoritePost

            )

            viewModel.setPost(updatedPost)
            viewModel.updatePost(updatedPost)


            val action = EditDescriptionFragmentDirections.actionEditDescriptionFragmentToDescriptionFragment(args.source)
            findNavController().navigate(action)
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenPost.observe(viewLifecycleOwner) {
            val countries = mutableListOf(getString(R.string.select_location)) + Locale.getAvailableLocales()
                .map { Locale("", it.country).displayCountry }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()

            val spinnerAdapter =
                ArrayAdapter(requireContext(),R.layout.spinner_item, countries)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCountriesEdit.adapter = spinnerAdapter


            val index = spinnerAdapter.getPosition(it.location)
            if (index >= 0) {
                binding.spinnerCountriesEdit.setSelection(index)
            }

            binding.editTxtEditPostPlace.setText(it.place)
            binding.editTextEditPostDescription.setText(it.description)
            binding.txtViewDateEditDescription.text =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    Date(it.date)
                )

            editableImages.clear()
            editableImages.addAll(it.images.orEmpty())

            editableImages = it.images?.filter { imageUrl -> imageUrl.isNotBlank() }?.toMutableList() ?: mutableListOf()


            editImagesAdapter = EditableImagesAdapter(
                editableImages,
                onRemoveClick = { pos ->
                    editableImages.removeAt(pos)
                    binding.editIndicator.setViewPager(binding.editViewPagerImages)
                    editImagesAdapter.notifyDataSetChanged()
                },

                )
            binding.editViewPagerImages.adapter = editImagesAdapter
            binding.editIndicator.setViewPager(binding.editViewPagerImages)


            if (editableImages.isNotEmpty()) {

                binding.editViewPagerImages.offscreenPageLimit = 1
                binding.editViewPagerImages.clipToPadding = false
                binding.editViewPagerImages.clipChildren = false
                binding.editViewPagerImages.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                val transformer = androidx.viewpager2.widget.CompositePageTransformer().apply {
                    addTransformer(androidx.viewpager2.widget.MarginPageTransformer(1))
                }
                binding.editViewPagerImages.setPageTransformer(transformer)

                binding.editViewPagerImages.visibility = View.VISIBLE
                binding.editIndicator.visibility = View.VISIBLE
            } else {
                binding.editViewPagerImages.visibility = View.GONE
                binding.editIndicator.visibility = View.GONE
            }

        }
    }
}