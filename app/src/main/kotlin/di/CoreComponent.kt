package com.bitwiserain.remindme.di

import android.app.Application

interface CoreComponent {
    val application: Application
}

class CoreComponentImpl(
    override val application: Application
) : CoreComponent
