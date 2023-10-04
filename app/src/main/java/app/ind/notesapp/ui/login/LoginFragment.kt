package app.ind.notesapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.ind.notesapp.R
import app.ind.notesapp.databinding.FragmentLoginBinding
import app.ind.notesapp.models.UserRequest
import app.ind.notesapp.utils.NetworkResult
import app.ind.notesapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
     lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        if (tokenManager.getToken()!=null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }



        return  binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.loginUser(getUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        bindObserver()

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(
            userRequest.username,
            userRequest.password,
            userRequest.email,
            true
        )
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(emailAddress, password, "")
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }




}