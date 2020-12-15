package itu.proj.wilo

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import itu.proj.wilo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    lateinit var globalCookie : String

    override fun onCreate(savedInstanceState: Bundle?) {
        val cookie = intent.getStringExtra("EXTRA_SESSION_ID")
        if (cookie != null) {
            globalCookie = cookie
        }
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, R.string.welcome, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Setting up drawer menu. IDs must correspond with IDs in XML navigation file,
        // because of automatic 1:1 mapping.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_account, R.id.nav_settings, R.id.nav_reservations,R.id.nav_home,
                R.id.nav_babysitting_search
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu. This adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}