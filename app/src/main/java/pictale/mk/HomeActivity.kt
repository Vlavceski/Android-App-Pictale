package pictale.mk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_all_events.*
import org.json.JSONException
import pictale.mk.Adapter.EventAdapter
import pictale.mk.Data.EventModelClass
import pictale.mk.Data.Events
import pictale.mk.fragments.AllEventsFragment
import pictale.mk.fragments.FavEventsFragment
import pictale.mk.fragments.HighlightsFragment
import pictale.mk.fragments.MyEventsFragment
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()

/*
//        val events= mutableListOf<EventModelClass>()
//        for(i in 0..100)
//        {
//            events.add(EventModelClass("Hello","No"))
//        }
//
//        rvEventsList.apply {
//            layoutManager = LinearLayoutManager(this@HomeActivity)
//            adapter= EventsAdapter(events)
//        }
*/


        try {
            val jsonString = getJSONFromAssets("events.json")
            val events = Gson().fromJson(jsonString, Events::class.java)
            rvEventsList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = EventAdapter(this, events.events)
            rvEventsList.adapter = itemAdapter


        } catch (e: JSONException) {
            e.printStackTrace()
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

    private fun getJSONFromAssets(fileName: String): String? {
        val jsonString: String
        try {
            jsonString=application.assets.open(fileName).use {
                inputStream -> inputStream.bufferedReader().use {
                    it.readText()
            }
            }
        }
        catch (e: IOException){
            e.printStackTrace()
            return null
        }
        return jsonString
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

    private fun logout(){
        mAuth.signOut()
        updateUI(mAuth.currentUser)
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
//        else{
//            usernameTV.text="Welcome ${mAuth.currentUser!!.email}"
//        }
    }
}



