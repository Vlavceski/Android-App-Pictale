package pictale.mk.Data


data class Events(
    val events: ArrayList<EventModelClass>
)
data class EventModelClass(
    val tittle: String,
    val location: String
)
