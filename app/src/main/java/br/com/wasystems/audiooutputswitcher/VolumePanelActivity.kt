package br.com.wasystems.audiooutputswitcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log

class VolumePanelActivity : Activity() {

    companion object {
        private const val TAG = "VolumePanelActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openVolumePanel()
        finish()
    }

    private fun openVolumePanel() {
        try {
            @Suppress("DEPRECATION")
            startActivity(Intent(Settings.Panel.ACTION_VOLUME))
            Log.d(TAG, "Volume Panel opened")
        } catch (e: Exception) {
            Log.w(TAG, "Volume Panel failed, trying Sound Settings: ${e.message}")
            try {
                startActivity(Intent(Settings.ACTION_SOUND_SETTINGS))
                Log.d(TAG, "Sound Settings opened as fallback")
            } catch (e2: Exception) {
                Log.e(TAG, "all fallbacks failed: ${e2.message}")
            }
        }
    }
}
