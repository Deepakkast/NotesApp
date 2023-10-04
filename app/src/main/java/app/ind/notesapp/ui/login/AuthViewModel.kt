package app.ind.notesapp.ui.login

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ind.notesapp.models.UserRequest
import app.ind.notesapp.models.UserResponse
import app.ind.notesapp.repository.UserRepository
import app.ind.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
        get () = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }


    fun validateCredentials(username:String,password:String,email:String,isLogin:Boolean):Pair<Boolean,String>{
        var result = Pair(true,"")
        if ((!isLogin &&  TextUtils.isEmpty(username)) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            result = Pair(false,"please provide credentials")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"please provide valid email")
        }else if (password.length<=5){
            result = Pair(false,"Password length should be grater than 5")
        }
        return  result
    }


}