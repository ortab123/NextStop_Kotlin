package com.example.final_nextstop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.final_nextstop.databinding.HomePageLayoutBinding
import com.example.final_nextstop.databinding.SettingsLayoutBinding
import com.example.final_nextstop.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment (){
    private var _binding: SettingsLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToProfileFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}