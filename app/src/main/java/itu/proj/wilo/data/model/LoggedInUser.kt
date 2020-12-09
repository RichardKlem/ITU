package itu.proj.wilo.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val email: String,
    val displayName: String,
    val cookie: String
)