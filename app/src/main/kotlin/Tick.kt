package com.bitwiserain.remindme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

object Tick {
    // TODO: Replace with Flow
    private val _timer: MutableLiveData<Instant> = MutableLiveData()
    val timer: LiveData<Instant>
        get() = _timer

    init {
        Timer().apply {
            val now = Instant.now()
            val delay = 1000L - Duration.between(now.truncatedTo(ChronoUnit.SECONDS), now).toMillis()

            scheduleAtFixedRate(delay, 16L) {
                _timer.postValue(Instant.now().truncatedTo(ChronoUnit.SECONDS))
            }
        }
    }
}
