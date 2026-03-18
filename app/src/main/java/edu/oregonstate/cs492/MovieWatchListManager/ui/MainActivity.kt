package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.oregonstate.cs492.MovieWatchListManager.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home, R.id.search, R.id.saved, R.id.history)
        )

        val toolbar = findViewById<MaterialToolbar>(R.id.top_app_bar)
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.home) {
                toolbar.setLogo(R.drawable.movie_icon)
                toolbar.titleMarginStart = resources.getDimensionPixelSize(R.dimen.toolbar_title_margin_home)
                toolbar.navigationIcon = null
            } else {
                toolbar.logo = null
                toolbar.titleMarginStart = resources.getDimensionPixelSize(R.dimen.toolbar_title_margin_detail)
            }

            if (destination.id == R.id.category_details || destination.id == R.id.movie_details) {
                toolbar.setNavigationIcon(R.drawable.back_arrow_icon)
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
        bottomNav.setOnItemSelectedListener { item ->
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(navController.graph.startDestinationId, false)
                .build()
            navController.navigate(item.itemId, null, navOptions)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}