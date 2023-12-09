package com.lans.task3_pokedex.presentation

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lans.task3_pokedex.R
import com.lans.task3_pokedex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var controller: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        hideBottomNavigation()
        setContentView(binding.root)
        setSupportActionBar(binding.appBar)

        setupProgressBar()
        setupNavController()
        setupNavigationDrawer()
        setupBottomNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar, menu)

        val actionSearch = menu.findItem(R.id.actionSearch)
        searchView = actionSearch.actionView as SearchView
        searchView.queryHint = "Search..."

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupProgressBar() {
        progressBar = binding.progressBar
    }

    private fun setupNavController() {
        controller =
            (supportFragmentManager.findFragmentById(binding.navHost.id) as NavHostFragment).navController

        val navGraph = controller.navInflater.inflate(R.navigation.nav_graph)

        lifecycleScope.launch {
            viewModel.session.collect { result ->
                showLoading(viewModel.loading.first())

                if (result != null) {
                    if (result != false) {
                        navGraph.setStartDestination(R.id.pokemonFragment)
                        controller.graph = navGraph
                    } else {
                        navGraph.setStartDestination(R.id.signInFragment)
                        controller.graph = navGraph
                    }
                }
            }

            viewModel.error.collect { result ->
                if (!result.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root,
                        result.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupNavigationDrawer() {
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, binding.appBar, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pokemonFragment -> {
                    controller.navigate(R.id.pokemonFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.itemFragment -> {
                    controller.navigate(R.id.itemFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.actionLogout -> {
                    viewModel.clearSession()
                    controller.navigate(R.id.signInFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                else -> true
            }
        }
    }

    private fun setupBottomNavigation() {
        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.pokemonFragment -> {
                    showAppBar()
                    showBottomNavigation()
                }

                R.id.itemFragment -> {
                    showAppBar()
                    showBottomNavigation()
                }

                R.id.favoriteFragment -> {
                    showAppBar()
                    showBottomNavigation()
                }

                else -> {
                    hideAppBar()
                    hideBottomNavigation()
                }
            }
        }

        binding.bottomNavigation.setupWithNavController(controller)
    }

    private fun showAppBar() {
        binding.appBar.visibility = View.VISIBLE
    }

    private fun hideAppBar() {
        binding.appBar.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.root.backgroundTintMode = if (isLoading) {
            PorterDuff.Mode.MULTIPLY
        } else {
            PorterDuff.Mode.SRC_IN
        }

        binding.root.backgroundTintList = if (isLoading) {
            ContextCompat.getColorStateList(this, R.color.gray)
        } else {
            ContextCompat.getColorStateList(this, R.color.white)
        }
    }
}