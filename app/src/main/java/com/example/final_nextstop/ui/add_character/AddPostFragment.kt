package com.example.final_nextstop.ui.add_character

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.net.Uri
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_nextstop.R
import com.example.final_nextstop.data.model.Post
import com.example.final_nextstop.databinding.AddPostLayoutBinding
import com.example.final_nextstop.ui.PostsViewModel
import com.example.final_nextstop.ui.all_characters.ImagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.example.final_nextstop.util.autoCleared
import com.example.final_nextstop.util.CapitalResolver
import java.util.*


@AndroidEntryPoint
class AddPostFragment : Fragment() {

    private var _binding: AddPostLayoutBinding by autoCleared()
    private val binding get() = _binding
    private val viewModel: PostsViewModel by activityViewModels()
    private lateinit var imagesAdapter: ImagesAdapter
    private val imageUris = mutableListOf<Uri?>(null)


    private var currentSelectedPosition = -1

    private val pickImagesLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            requireActivity().contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            if (imageUris.size == 1) {
                imageUris.add(uri)  // הפלוס ב-0, התמונה ב-1
            } else if (currentSelectedPosition == 0) {
                // אם בחרת את הפלוס - הוסף תמונה אחרי הפלוס
                imageUris.add(1, uri)
            } else {
                // החלף תמונה קיימת במקום שנבחר (אם רצית את זה)
                imageUris[currentSelectedPosition] = uri
            }

            // אם יש יותר מ-9 תמונות, הסר את הפלוס
            if (imageUris.size > 10) {
                imageUris.removeAt(0) // הסר פלוס אם עברנו 9 תמונות
            }

            viewModel.setImageUris(imageUris.filterNotNull())


            imagesAdapter.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.imageUris.observe(viewLifecycleOwner) { uris ->
            imageUris.clear()
            if (uris.isNullOrEmpty()) {
                imageUris.add(null)
            } else {
                imageUris.add(null)
                imageUris.addAll(uris.toMutableList())

            }

            imagesAdapter.notifyDataSetChanged()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddPostLayoutBinding.inflate(inflater, container, false)

        // מילוי הספינר של המדינות
        val countries = mutableListOf(getString(R.string.select_location)) + Locale.getAvailableLocales()
            .map { Locale("", it.country).displayCountry }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, countries)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountriesAddPost.adapter = spinnerAdapter

        // חזרה לפרופיל
        binding.btnBackFromAddPost.setOnClickListener {
            viewModel.setImageUris(emptyList())
            findNavController().navigate(R.id.action_addPostFragment_to_profileFragment)
        }

        // שמירת הפוסט
        binding.btnAddPost.setOnClickListener {
            val selectedLocation = binding.spinnerCountriesAddPost.selectedItem.toString()
            val selectedCountry = binding.spinnerCountriesAddPost.selectedItem.toString()
            val capital = CapitalResolver.getCapitalForCountry(selectedCountry)
            val currentDescription = binding.editTxtPostDescription.text.toString()

            if (selectedLocation == getString(R.string.select_location)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_location),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (currentDescription.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_write_description),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val post = Post(
                location = selectedLocation,
                place = binding.editTxtPostPlace.text.toString(),
                description = currentDescription,
                images = viewModel.imageUris.value?.map { it.toString() },
                profileImageUri = viewModel.profileImageUri.value,
                date = Date().time
            )

            viewModel.addPost(post)


            viewModel.setImageUris(emptyList())
            findNavController().navigate(R.id.action_addPostFragment_to_profileFragment)
        }

        imagesAdapter = ImagesAdapter(imageUris) { position ->
            currentSelectedPosition = position
            // פתח גלריה לבחירת תמונה אחת
            pickImagesLauncher.launch(arrayOf("image/*"))
        }

        binding.recyclerImages.apply {
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        return binding.root
    }

}