package pictale.mk.auth

data class LoggedResponse(
    var email: String,
    var firstName: String,
    var id: Int,
    var lastName: String,
    var pictureUrl:String
)
