package pictale.mk.events

data class ResponseAllEventsPages(
        val content: List<Eventt>,
        val empty: Boolean,
        val first: Boolean,
        val last: Boolean,
        val number: Int,
        val numberOfElements: Int,
        val pageable: Any, // or use a more specific class if available
        val size: Int,
        val sort: Any, // or use a more specific class if available
        val totalElements: Int,
        val totalPages: Int
    )

    data class Eventt(
        val collaboration: String,
        val createdBy: CreatedByy,
        val dateCreated: Long,
        val description: String,
        val eventId: String,
        val isPublic: Boolean,
        val location: String,
        val name: String,
        val tags: List<String>,
        val thumbnailUrl: String
    )

    data class CreatedByy(
        val firstName: String,
        val id: String,
        val lastName: String,
        val pictureUrl: String
    )