package pictale.mk.auth

data class ResponseBody (
    var expiresIn: String,
    var refreshToken: String,
    var token: String
    )