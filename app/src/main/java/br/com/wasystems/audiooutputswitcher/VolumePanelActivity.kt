package br.com.wasystems.audiooutputswitcher

import android.app.Activity
import android.content.ActivityNotFoundException
import android.os.Bundle
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
        val opened = AudioOutputFallback.tryEach { intent ->
            try {
                startActivity(intent)
                Log.d(TAG, "opened: ${intent.action}")
                true
            } catch (e: ActivityNotFoundException) {
                Log.w(TAG, "activity not found: ${intent.action}: ${e.message}")
                false
            } catch (e: Exception) {
                Log.w(TAG, "failed to open ${intent.action}: ${e.message}")
                false
            }
        }
        if (!opened) Log.e(TAG, "all fallbacks failed")
    }
}
