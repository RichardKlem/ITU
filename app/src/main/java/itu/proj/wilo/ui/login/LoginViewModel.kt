package itu.proj.wilo.ui.login


import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import itu.proj.wilo.R

class LoginViewModel : ViewModel() {
    // Live data LoginFormState object to enable observing
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailNameValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }
    private fun isEmailNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }
}
