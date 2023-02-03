package pictale.mk.auth

data class UpdatePassword(
    var newPassword:String,
    var oldPassword:String
)
