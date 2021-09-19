package com.bitwiserain.remindme.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object CoroutineDispatcherModule {
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
}
