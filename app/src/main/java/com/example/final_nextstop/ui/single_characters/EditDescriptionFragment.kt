package com.example.final_nextstop.ui.single_characters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.final_nextstop.R
import com.example.final_nextstop.data.model.Post
import com.example.final_nextstop.databinding.EditDescriptionLayoutBinding
import com.example.final_nextstop.ui.PostsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditDescriptionFragment : Fragment() {
    private var _binding : EditDescriptionLayoutBinding? = null

    val viewModel : PostsViewModel by activityViewModels()

    private val binding get() = _binding!!

    private var editableImages = mutableListOf<String>()

    // === שינוי: שמירת המיקום שנבחר להחלפת תמונה ===
    private var currentSelectedPosition = -1

    // === שינוי: ה-Adapter עם תמיכה בעריכה ===
    private lateinit var editImagesAdapter: EditableImagesAdapter

    // === שינוי: launcher לבחירת תמונה חדשה ===
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uriStr = uri.toString()

            val wasEmpty = editableImages.isEmpty() // <- בדיקה לפני ההוספה
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
                Toast.makeText(requireContext(), "Maximum 10 images", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            pickImageLauncher.launch(arrayOf("image/*"))
        }


        binding.mbSavePost.setOnClickListener {

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
                date = originalPost.date
            )

            viewModel.setPost(updatedPost)
            viewModel.updatePost(updatedPost)


            findNavController().navigate(R.id.action_editDescriptionFragment_to_descriptionFragment)
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
//
//            editImagesAdapter = EditableImagesAdapter(
//                editableImages,
//                onRemoveClick = { pos ->
//                    editableImages.removeAt(pos)
//                    editImagesAdapter.notifyDataSetChanged()
//                },
//
//            )
//
//
//            binding.editViewPagerImages.adapter = editImagesAdapter
////            binding.editViewPagerImages.visibility = View.VISIBLE
//            binding.editIndicator.setViewPager(binding.editViewPagerImages)

//            if (!it.images.isNullOrEmpty()) {
//                editImagesAdapter = EditableImagesAdapter(it.images.orEmpty())
//                binding.editViewPagerImages.adapter = editImagesAdapter
//                binding.editIndicator.setViewPager(binding.editViewPagerImages)
//
//                binding.editViewPagerImages.offscreenPageLimit = 1
//                binding.editViewPagerImages.clipToPadding = false
//                binding.editViewPagerImages.clipChildren = false
//                binding.editViewPagerImages.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//
//                val transformer = androidx.viewpager2.widget.CompositePageTransformer().apply {
//                    addTransformer(androidx.viewpager2.widget.MarginPageTransformer(1))
//                }
//
//                binding.editViewPagerImages.setPageTransformer(transformer)
//
//
//            } else {
//                binding.editViewPagerImages.visibility = View.GONE
//            }
//                if (!it.images.isNullOrEmpty()) {
//                    val adapter = DescriptionImagesAdapter(it.images)
//                    binding.viewPagerImages.adapter = adapter
//                } else {
//                    binding.viewPagerImages.visibility = View.GONE
//                }


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
//            binding.editViewPagerImages.offscreenPageLimit = 1
//            binding.editViewPagerImages.clipToPadding = false
//            binding.editViewPagerImages.clipChildren = false
//            binding.editViewPagerImages.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//
//            val transformer = androidx.viewpager2.widget.CompositePageTransformer().apply {
//                addTransformer(androidx.viewpager2.widget.MarginPageTransformer(1))
//            }
//            binding.editViewPagerImages.setPageTransformer(transformer)

//            binding.editViewPagerImages.visibility =
//                if (editableImages.isNotEmpty()) View.VISIBLE else View.GONE


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}