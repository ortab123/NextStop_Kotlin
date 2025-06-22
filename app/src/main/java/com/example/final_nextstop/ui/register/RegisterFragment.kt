package com.example.final_nextstop.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.final_nextstop.databinding.LoginLayoutBinding
import com.example.final_nextstop.databinding.RegisterLayoutBinding
import il.co.syntax.fullarchitectureretrofithiltkotlin.utils.autoCleared

class RegisterFragment : Fragment() {
    private var binding: RegisterLayoutBinding by autoCleared()
    //private var binding get() = _binding!!
    //private val viewModel: PostsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegisterLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}