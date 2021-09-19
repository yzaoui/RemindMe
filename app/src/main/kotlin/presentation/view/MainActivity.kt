package com.bitwiserain.remindme.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bitwiserain.remindme.R
import com.bitwiserain.remindme.databinding.ActivityMainBinding
import com.bitwiserain.remindme.util.PACKAGE_PREFIX
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), ReminderListFragment.OnReminderItemInteractionListener, EditReminderDialogFragment.OnReminderSaveListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Workaround for https://issuetracker.google.com/issues/142847973 instead of findNavController()
        val navController = (supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment).navController

        var startArgs = ReminderListFragmentArgs()
        if (intent?.action == Action.GO_TO_REMINDER.key) {
            startArgs = startArgs.copy(scrollToReminderId = intent.data!!.pathSegments[1].toInt())
        }

        navController.setGraph(R.navigation.main_navigation, startArgs.toBundle())

        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_reminder_list), binding.mainDrawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        if (intent?.action == Action.CREATE_REMINDER.key) {
            showCreateDialogFragment()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showCreateDialogFragment() {
        EditReminderDialogFragment().show(supportFragmentManager, null)
    }

    override fun onCreateReminderClick() {
        showCreateDialogFragment()
    }

    override fun onReminderSave() {
        Snackbar.make(findViewById<CoordinatorLayout>(R.id.reminder_list_container), getString(R.string.main_created_reminder_snackbar), Snackbar.LENGTH_LONG).show()
    }

    override fun onReminderDelete() {
        Snackbar.make(findViewById<CoordinatorLayout>(R.id.reminder_list_container), getString(R.string.main_deleted_reminder_snackbar), Snackbar.LENGTH_LONG).show()
    }

    enum class Action(val key: String) {
        CREATE_REMINDER(PACKAGE_PREFIX + "CREATE_REMINDER"),
        GO_TO_REMINDER(PACKAGE_PREFIX + "GO_TO_REMINDER")
    }
}
