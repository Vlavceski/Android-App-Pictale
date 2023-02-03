package pictale.mk.auth.responses

data class LoggedResponse(
    var email: String,
    var firstName: String,
    var id: String,
    var lastName: String,
    var pictureUrl:String
)
