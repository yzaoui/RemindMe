package com.bitwiserain.remindme

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bitwiserain.remindme.util.InjectorUtils
import com.bitwiserain.remindme.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reminder_list.*

class MainActivity : AppCompatActivity(), ReminderListFragment.OnReminderItemInteractionListener, EditReminderDialogFragment.OnReminderSaveListener {
    private val viewModel: MainViewModel by viewModels {
        InjectorUtils.provideMainViewModelFactory(this)
    }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateReminderClick() {
        EditReminderDialogFragment().show(supportFragmentManager, null)
    }

    override fun onReminderSave(reminder: NewReminder) {
        viewModel.insertReminder(reminder)
        Snackbar.make(reminder_list_container, "New reminder was created", Snackbar.LENGTH_LONG).show()
    }

    override fun onReminderDelete(reminder: Reminder) {
        viewModel.deleteReminder(reminder)
        Snackbar.make(reminder_list_container, "Reminder was deleted", Snackbar.LENGTH_LONG).show()
    }
}
