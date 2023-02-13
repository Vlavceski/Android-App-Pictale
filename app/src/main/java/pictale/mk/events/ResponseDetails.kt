package pictale.mk.events

data class CreatedBy(
    val firstName: String,
    val id: String,
    val lastName: String,
    val pictureUrl: String
)

data class EventFile(
    val createdBy: CreatedBy,
    val dateCreated: Long,
    val fileName: String,
    val id: String,
    val urlLink: String
)

data class EventUser(
    val firstName: String,
    val id: String,
    val lastName: String,
    val pictureUrl: String
)

data class ResponseDetails(
    val collaboration: String,
    val createdBy: CreatedBy,
    val dateCreated: Long,
    val description: String,
    val eventFilesList: List<EventFile>,
    val eventId: String,
    val eventUsers: List<EventUser>,
    val isPublic: Boolean,
    val location: String,
    val name: String,
    val tags: List<String>,
    val thumbnailUrl: String
)
