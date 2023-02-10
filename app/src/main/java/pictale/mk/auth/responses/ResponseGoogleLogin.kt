package pictale.mk.auth.responses

data class ResponseGoogleLogin (
        var expiresIn: String,
        var refreshToken: String,
        var token: String
        )