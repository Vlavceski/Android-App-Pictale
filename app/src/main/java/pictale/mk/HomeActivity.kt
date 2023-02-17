package pictale.mk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*
import pictale.mk.auth.AuthToken
import pictale.mk.fragments.AllEventsFragment
import pictale.mk.fragments.FavEventsFragment
import pictale.mk.fragments.HighlightsFragment
import pictale.mk.fragments.MyEventsFragment
import pictale.mk.HomeActivity as HomeActivity


class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        add_event.setOnClickListener {
            startActivity(Intent(this,AddEventActivity::class.java))
        }

        toolbar_click.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.profile_menu -> startActivity(Intent(this,SettingActivity::class.java))
                R.id.logout_menu -> logout()
                else -> {true}
            }
            true
        }


        replaceFragment(AllEventsFragment())
        nav_click.setOnItemSelectedListener {

            when(it.itemId){
                R.id.all_events -> replaceFragment(AllEventsFragment())
                R.id.my_events -> replaceFragment(MyEventsFragment())
                R.id.fav_events -> replaceFragment(FavEventsFragment())
                R.id.highlights -> replaceFragment(HighlightsFragment())
                else -> {true}
            }
            true
        }

    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", null).apply()
        updateUI()
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private fun updateUI() {
        val token=AuthToken.get(this)
        d("token","$token")
        if (token==null){
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

}



