package pictale.mk.events


data class AddEventBody (
    var name :String,
    var collaboration :String,
    var description:String,
    var tags :String,
    var public :Boolean,
    var location :String

    )