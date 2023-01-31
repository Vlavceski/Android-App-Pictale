package pictale.mk.data



data class Events(
  val events: ArrayList<EventList>
)

data class EventList (
  val name: String,
  val location: String,

  var createdBy: createdBy


)
data class createdBy(
//  val creator_id: Int,
  val firstName: String
//  val creator_lastName: String,
//  val creator_picture: Image
)
