package itu.proj.wilo.ui.registration


import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import itu.proj.wilo.R

class RegistrationViewModel : ViewModel() {
    // Live data LoginFormState object to enable observing.
    private val _loginForm = MutableLiveData<RegistrationFormState>()
    val registrationFormState: LiveData<RegistrationFormState> = _loginForm

    fun loginDataChanged(email: String, name: String, password: String, reEnteredPassword: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = RegistrationFormState(emailError = R.string.invalid_email)
        } else if (!isNameValid(name)) {
            _loginForm.value = RegistrationFormState(nameError = R.string.invalid_name)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = RegistrationFormState(passwordError = R.string.invalid_password)
        } else if (!arePasswordsSame(password, reEnteredPassword)) {
            _loginForm.value = RegistrationFormState(passwordReenterError = R.string.invalid_password_reenter)
        } else {
            _loginForm.value = RegistrationFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }
    // Full name must be at least 5 characters with at least one space.
    private fun isNameValid(name: String): Boolean {
        return name.length > 4 &&  name.contains(' ')

    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }

    private fun arePasswordsSame(password: String, reEnteredPassword: String): Boolean {
        return password == reEnteredPassword
    }
}