package com.example.final_nextstop.ui.all_characters

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.final_nextstop.R
import com.example.final_nextstop.data.local_db.PostDao
import com.example.final_nextstop.databinding.HomePageLayoutBinding
import com.example.final_nextstop.ui.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(){


    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostsViewModel by activityViewModels()

    val recognizeSpeechLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it: ActivityResult ->
            if (it.resultCode == RESULT_OK) {
                val results = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (!results.isNullOrEmpty()) {
                    binding.searchInput.setText(results[0])
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = HomePageLayoutBinding.inflate(inflater, container, false)

        binding.txtinputSearch.setEndIconOnClickListener {
            val intent  = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            }
            recognizeSpeechLauncher.launch(intent)
        }

        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_sign_out){

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.login_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}