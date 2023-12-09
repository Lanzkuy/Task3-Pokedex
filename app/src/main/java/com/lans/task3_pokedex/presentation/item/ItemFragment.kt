package com.lans.task3_pokedex.presentation.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lans.task3_pokedex.databinding.FragmentItemBinding
import com.lans.task3_pokedex.domain.model.Item
import com.lans.task3_pokedex.presentation.MainActivity
import com.lans.task3_pokedex.presentation.adapter.ItemAdapter
import com.lans.task3_pokedex.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemFragment : Fragment() {
    private lateinit var binding: FragmentItemBinding
    private val viewModel: ItemViewModel by viewModels()

    private var itemData = emptyList<Item>()
    private lateinit var rvItem: RecyclerView
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponent()
        lifecycleScope.launch {
            observeItem()
        }
    }

    private fun initializeComponent() {
        rvItem = binding.rvItem
        rvItem.layoutManager = GridLayoutManager(requireActivity(), 2)
        rvItem.addItemDecoration(SpacesItemDecoration(20))
        (requireActivity() as MainActivity).searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (!text.isNullOrBlank()) {
                    filterItem(text)
                } else {
                    itemAdapter.filter(itemData)
                }
                return false
            }
        })
    }

    private suspend fun observeItem() {
        viewModel.state.collect { result ->
            (requireActivity() as MainActivity).showLoading(result.isLoading)

            if (result.data.isNotEmpty()) {
                result.data.map { item ->
                    item.isLiked = result.favorite.contains(item.itemName)
                }
                itemData = result.data
                itemAdapter = ItemAdapter(result.data, onClick = { name ->
                    viewModel.createFavorite(2, name)
                })
                rvItem.adapter = itemAdapter
            }

            if (result.error.isNotBlank()) {
                Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                result.error = ""
            }
        }
    }

    private fun filterItem(item: String) {
        val filter = itemData.filter { it.itemName.contains(item) }
        itemAdapter.filter(filter)
    }
}