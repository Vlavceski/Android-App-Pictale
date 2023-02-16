package pictale.mk.pages

data class ResponsePages(
    val content: List<Event>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort,
    val totalElements: Int,
    val totalPages: Int
)

data class Event(
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

data class CreatedBy(
    val firstName: String,
    val id: String,
    val lastName: String,
    val pictureUrl: String
)

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)
