package pictale.mk.auth

data class TokenResponse (
    var expiresIn: String,
    var refreshToken: String,
    var token: String
        )