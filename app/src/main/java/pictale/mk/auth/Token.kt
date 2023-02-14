package pictale.mk.auth

import android.content.Context

class AuthToken {
    companion object {
        fun get(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
//           val refreshToken=  sharedPreferences.getString("refreshToken", null)
//            val expiresIn=sharedPreferences.getString("expiresIn", null)
            return token
        }
    }
}
