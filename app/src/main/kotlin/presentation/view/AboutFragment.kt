package com.bitwiserain.remindme.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bitwiserain.remindme.BuildConfig
import com.bitwiserain.remindme.R
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        view.about_version.text = "App version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

        return view
    }
}
