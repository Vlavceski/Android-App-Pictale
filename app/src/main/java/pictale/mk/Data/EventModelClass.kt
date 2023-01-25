package pictale.mk.Data

data class Events(
    val users: ArrayList<EventModelClass>
)

data class EventModelClass (
    var id: Int,
    var name :String,
    var location: String,
//    var isPublic :Boolean,
//    var collaboration: String,
    var firstName: String,
//    var lastName: String
    )


