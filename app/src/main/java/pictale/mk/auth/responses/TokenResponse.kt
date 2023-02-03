package pictale.mk.auth.responses

data class TokenResponse (
    var expiresIn: String,
    var refreshToken: String,
    var token: String
        )