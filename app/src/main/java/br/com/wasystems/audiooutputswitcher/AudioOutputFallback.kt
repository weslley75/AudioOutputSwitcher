package br.com.wasystems.audiooutputswitcher

import android.content.Intent
import android.provider.Settings
import androidx.annotation.VisibleForTesting

internal object AudioOutputFallback {

    @VisibleForTesting
    internal val FALLBACK_ACTIONS = listOf(
        Settings.Panel.ACTION_VOLUME,
        Settings.ACTION_SOUND_SETTINGS
    )

    fun tryEach(launch: (Intent) -> Boolean): Boolean =
        FALLBACK_ACTIONS.any { action -> launch(Intent(action)) }
}
