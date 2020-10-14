package com.bitwiserain.remindme.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bitwiserain.remindme.BuildConfig
import com.bitwiserain.remindme.R
import com.bitwiserain.remindme.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.aboutVersion.text = getString(R.string.about_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)

        return binding.root
    }
}
