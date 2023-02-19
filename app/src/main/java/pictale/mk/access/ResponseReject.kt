package pictale.mk.access

import com.google.android.gms.common.api.CommonStatusCodes

data class ResponseReject(
//    var body:String,     --vidi
    var statusCodes: String,
    var statusCodeValue:Int
)
