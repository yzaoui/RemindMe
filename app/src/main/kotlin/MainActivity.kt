package com.bitwiserain.remindme

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ReminderListFragment.OnReminderItemInteractionListener {
    private lateinit var viewModel: ReminderListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startActivityForResult(CreateReminderActivity.newIntent(this), Request.CREATE_NEW_REMINDER.ordinal)
        }

        viewModel = ViewModelProviders.of(this).get(ReminderListViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, ReminderListFragment.newInstance())
                .commitNow()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Request.CREATE_NEW_REMINDER.ordinal -> when (resultCode) {
                RESULT_OK -> addNewReminder(data!!)
            }
        }
    }

    private fun addNewReminder(data: Intent) {
        val newReminder = data.getParcelableExtra<NewReminder>(Extra.NEW_REMINDER.key)

        viewModel.reminders.value = viewModel.reminders.value.orEmpty() + Reminder(Reminder.newId(), newReminder.title, newReminder.time)
        Snackbar.make(fab, "New reminder was created", Snackbar.LENGTH_LONG).show()
    }

    override fun onReminderItemInteraction(reminder: Reminder) {
        println("Clicked reminder \"${reminder.title}\"")
    }

    enum class Request {
        CREATE_NEW_REMINDER
    }

    enum class Extra(val key: String) {
        NEW_REMINDER("NEW_REMINDER")
    }
}
