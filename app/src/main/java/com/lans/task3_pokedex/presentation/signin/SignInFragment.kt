package com.lans.task3_pokedex.presentation.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lans.task3_pokedex.R
import com.lans.task3_pokedex.databinding.FragmentSignInBinding
import com.lans.task3_pokedex.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponent()
        lifecycleScope.launch {
            observeSignIn()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnSignIn -> {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                if (username.isBlank()) {
                    Snackbar.make(binding.root, "Username must be filled", Snackbar.LENGTH_SHORT)
                        .show()
                    return
                }

                if (password.isBlank()) {
                    Snackbar.make(binding.root, "Password must be filled", Snackbar.LENGTH_SHORT)
                        .show()
                    return
                }

                viewModel.signin(username, password)
                requireActivity().hideKeyboard(binding.root)
            }

            R.id.tvSignUp -> {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun initializeComponent() {
        etUsername = binding.etUsername
        etPassword = binding.etPassword
        binding.btnSignIn.setOnClickListener(this)
        binding.tvSignUp.setOnClickListener(this)
    }

    private suspend fun observeSignIn() {
        viewModel.state.collect { result ->
            if (result.isLoggedIn) {
                if (findNavController().currentDestination?.id != R.id.pokemonFragment) {
                    val action = SignInFragmentDirections.actionSignInFragmentToPokemonFragment()
                    findNavController().navigate(action)
                }
            }

            if (result.error.isNotBlank()) {
                Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                result.error = ""
            }
        }
    }
}