package pictale.mk.auth

data class UserBody(
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String
    )
