package pictale.mk.events



data class AddEventBody(
    var collaboration:String,
    var description:String,
    var isPublic:Boolean,
    var location:String,
    var name:String,
    var tags: List<String>
//    var tags :Array<String>
    )