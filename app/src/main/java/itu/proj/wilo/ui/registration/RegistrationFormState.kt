package itu.proj.wilo.ui.registration

/**
 * Data validation state of the login form.
 */
data class RegistrationFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val passwordReenterError: Int? = null,
    val nameError: Int? = null,
    val isDataValid: Boolean = false
)