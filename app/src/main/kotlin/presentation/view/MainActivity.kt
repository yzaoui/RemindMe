package com.bitwiserain.remindme.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bitwiserain.remindme.NewReminder
import com.bitwiserain.remindme.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reminder_list.*

class MainActivity : AppCompatActivity(), ReminderListFragment.OnReminderItemInteractionListener, EditReminderDialogFragment.OnReminderSaveListener {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.main_nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_reminder_list), main_drawer_layout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateReminderClick() {
        EditReminderDialogFragment().show(supportFragmentManager, null)
    }

    override fun onReminderSave() {
        Snackbar.make(reminder_list_container, getString(R.string.main_created_reminder_snackbar), Snackbar.LENGTH_LONG).show()
    }

    override fun onReminderDelete() {
        Snackbar.make(reminder_list_container, getString(R.string.main_deleted_reminder_snackbar), Snackbar.LENGTH_LONG).show()
    }
}
