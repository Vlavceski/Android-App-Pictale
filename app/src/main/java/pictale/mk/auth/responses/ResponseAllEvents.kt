package pictale.mk.auth.responses

import pictale.mk.auth.Role
import java.util.*

data class ResponseAllEvents (
    var eventId:String,
    val name: String,
    val description: String,
    val location: String,
    val tags: String,
    val isPublic: Boolean,
    val dateCreated: Date,
    val pictureUrl: String,
    val collaboration:Role,
    val createdBy:CreateBy
        )


data class  CreateBy(
    val id: String,
    val firstName:String,
    val lastName: String,
    val pictureUrl:String
)