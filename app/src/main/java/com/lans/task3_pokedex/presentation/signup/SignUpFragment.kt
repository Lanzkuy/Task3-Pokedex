package com.lans.task3_pokedex.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lans.task3_pokedex.R
import com.lans.task3_pokedex.databinding.FragmentSignUpBinding
import com.lans.task3_pokedex.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponent()
        lifecycleScope.launch {
            observeSignUp()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnSignUp -> {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

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

                if (confirmPassword.isBlank()) {
                    Snackbar.make(
                        binding.root,
                        "Confirm password must be filled",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return
                }

                if (confirmPassword != password) {
                    Snackbar.make(
                        binding.root,
                        "Confirm password must be same as password",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return
                }

                viewModel.signup(username, password)
                requireActivity().hideKeyboard(binding.root)
            }

            R.id.tvSignIn -> {
                val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun initializeComponent() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        etUsername = binding.etUsername
        etPassword = binding.etPassword
        etConfirmPassword = binding.etConfirmPassword
        binding.btnSignUp.setOnClickListener(this)
        binding.tvSignIn.setOnClickListener(this)
    }

    private suspend fun observeSignUp() {
        viewModel.state.collect { result ->
            if (result.isSuccess) {
                Snackbar.make(
                    binding.root,
                    "User created successfully",
                    Snackbar.LENGTH_SHORT
                ).show()
                if(findNavController().currentDestination?.id != R.id.signInFragment) {
                    val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
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