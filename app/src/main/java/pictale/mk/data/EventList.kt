package pictale.mk.data

import android.media.Image
import java.util.Date

data class Events(
  val events: ArrayList<EventList>
)

data class EventList (
//  val id: Int,
  val name: String,
//  val description: String,
  val location: String,

  var createdBy: createdBy
//  val tags: String,
//  val isPublic: Boolean,
//  val dateCreated: Date,
//  val collection: Role,
//  //usersList
//  val users_list: usersList,
//  //creator za eventot
//  val creator: createdBy

)
data class usersList(
  val user_id: Int,
  val user_firstName: String,
  val user_lastName: String,
  val user_picture: Image
)
data class createdBy(
//  val creator_id: Int,
  val firstName: String
//  val creator_lastName: String,
//  val creator_picture: Image
)
