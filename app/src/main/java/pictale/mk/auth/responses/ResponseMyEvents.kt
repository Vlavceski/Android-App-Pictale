package pictale.mk.auth.responses

data class ResponseMyEvents(
val collaboration: String,
val createdBy: CreatedBy,
val dateCreated: Long,
val description: String,
val eventId: String,
val isPublic: Boolean,
val location: String,
val name: String,
val tags: List<String>,
val thumbnailUrl: String
)

//data class CreatedBy(
//    val firstName: String,
//    val id: String,
//    val lastName: String,
//    val pictureUrl: String
//)
