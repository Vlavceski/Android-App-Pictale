package pictale.mk.auth.responses

import pictale.mk.events.Role
import java.util.*
//data class API_events(
//    val data: List<ResponseAllEvents>
//    )

data class ResponseAllEvents(
//    val collaboration: Role,
//    val createdBy:CreateBy,
//    val dateCreated: Date,
//    val description: String,
    var eventId:String,
//    val isPublic: Boolean,
    val location: String,
    val name: String
//    val tags: List<String>,
//    val thumbnailUrl: String
    )

//data class  CreateBy(
//    val firstName:String,
//    val id: String,
//    val lastName: String,
//    val pictureUrl:String
//)
