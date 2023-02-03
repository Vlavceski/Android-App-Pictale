package pictale.mk.auth.responses

data class ResponseBody (
    var expiresIn: String,
    var refreshToken: String,
    var token: String
    )