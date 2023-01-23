package pictale.mk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.view.*

class HomeActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mytoolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()

        mytoolbar=findViewById(R.id.toolbar)
        mytoolbar.setting_click.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))

        }
        setSupportActionBar(mytoolbar)


        btn_logout.setOnClickListener {
            mAuth.signOut()
            updateUI(mAuth.currentUser)
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser=mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser==null){
            finish()
            startActivity(Intent(this,LoginActivity::class.java))
        }
        else{
            usernameTV.text="Welcome ${mAuth.currentUser!!.email}"
        }
    }
}