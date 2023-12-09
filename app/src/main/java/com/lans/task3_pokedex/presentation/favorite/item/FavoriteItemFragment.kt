package com.lans.task3_pokedex.presentation.favorite.item

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
import com.lans.task3_pokedex.databinding.FragmentFavoriteItemBinding
import com.lans.task3_pokedex.domain.model.Item
import com.lans.task3_pokedex.presentation.MainActivity
import com.lans.task3_pokedex.presentation.adapter.ItemAdapter
import com.lans.task3_pokedex.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteItemFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteItemBinding
    private val viewModel: FavoriteItemViewModel by viewModels()

    private var favoriteItemData = emptyList<Item>()
    private lateinit var rvFavoriteItem: RecyclerView
    private lateinit var favoriteItemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponent()
        lifecycleScope.launch {
            observeFavoriteItem()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (!text.isNullOrBlank()) {
                    filterItem(text)
                } else {
                    favoriteItemAdapter.filter(favoriteItemData)
                }
                return false
            }
        })
    }

    private fun initializeComponent() {
        rvFavoriteItem = binding.rvFavoriteItem
        rvFavoriteItem.layoutManager = GridLayoutManager(requireActivity(), 2)
        rvFavoriteItem.addItemDecoration(SpacesItemDecoration(20))
    }

    private suspend fun observeFavoriteItem() {
        viewModel.state.collect { result ->
            (requireActivity() as MainActivity).showLoading(result.isLoading)

            if (result.data.isNotEmpty()) {
                favoriteItemData = result.data
                favoriteItemAdapter = ItemAdapter(result.data)
                rvFavoriteItem.adapter = favoriteItemAdapter
            }

            if (result.error.isNotBlank()) {
                Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                result.error = ""
            }
        }
    }

    private fun filterItem(item: String) {
        val filter = favoriteItemData.filter { it.itemName.contains(item) }
        favoriteItemAdapter.filter(filter)
    }
}