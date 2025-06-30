package com.example.final_nextstop.ui.all_characters

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_nextstop.R
import com.example.final_nextstop.databinding.WeatherLayoutBinding
import com.example.final_nextstop.ui.WeatherViewModel
import com.example.final_nextstop.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: WeatherLayoutBinding by autoCleared()
    private val binding get() = _binding
    private val viewModel: WeatherViewModel by activityViewModels()
    private lateinit var adapter: WeatherItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.myToolbar)

        adapter = WeatherItemAdapter()
        binding.recyclerWeatherItems.adapter = adapter
        binding.recyclerWeatherItems.layoutManager = LinearLayoutManager(requireContext())

        viewModel.weatherItems.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.setWeatherItems(it)
                binding.recyclerWeatherItems.visibility = View.VISIBLE
                binding.textViewNoWeatherItems.visibility = View.GONE
                binding.textSwipeHint.visibility = View.VISIBLE
            } else {
                binding.recyclerWeatherItems.visibility = View.GONE
                binding.textViewNoWeatherItems.visibility = View.VISIBLE
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
                val weatherItem = (binding.recyclerWeatherItems.adapter as WeatherItemAdapter).weatherItemAt(viewHolder.adapterPosition)
                val dialog = DeleteWeatherItemsConfirmationDialogFragment.newInstance()
                dialog.onDeleteWeatherItemConfirmed = {
                    viewModel.deleteWeatherItem(weatherItem)
                    Toast.makeText(requireContext(),
                        "Weather item deleted", Toast.LENGTH_SHORT).show()
                }
                dialog.show(parentFragmentManager, "DeleteDialog")

                (binding.recyclerWeatherItems.adapter as WeatherItemAdapter).notifyItemChanged(viewHolder.adapterPosition)

            }

        }).attachToRecyclerView(binding.recyclerWeatherItems)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add -> {
                        findNavController().navigate(R.id.action_weatherFragment_to_addWeatherItemFragment)
                        true
                    }
                    R.id.action_delete -> {
                        val dialog = DeleteAllPostsConfirmationDialogFragment.newInstance()
                        dialog.onDeleteAllPostsConfirmed = {
                            viewModel.deleteAllWeatherItems()
                            Toast.makeText(requireContext(),
                                getString(R.string.all_posts_deleted), Toast.LENGTH_SHORT).show()
                        }
                        dialog.show(parentFragmentManager, "DeleteDialog")

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }
}