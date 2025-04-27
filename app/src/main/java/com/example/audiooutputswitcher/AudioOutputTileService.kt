package com.example.audiooutputswitcher

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast

class AudioOutputTileService : TileService() {
    companion object {
        private const val TAG = "AudioOutputTileService"
        private const val ACTION_MEDIA_OUTPUT = "com.android.systemui.action.LAUNCH_SYSTEM_MEDIA_OUTPUT_DIALOG"
        private const val PACKAGE_SYSTEMUI = "com.android.systemui"
        private const val RECEIVER_CLASS = "com.android.systemui.media.dialog.MediaOutputDialogReceiver"
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()
        openMediaOutputDialog()
    }

    private fun openMediaOutputDialog() {
        try {
            val intent = Intent(ACTION_MEDIA_OUTPUT).apply {
                component = ComponentName(PACKAGE_SYSTEMUI, RECEIVER_CLASS)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )

            pendingIntent.send()
        } catch (e: Exception) {
            showToast("Erro: ${e.message}")
        }
    }

    private fun updateTile() {
        val tile = qsTile ?: return
        tile.label = "Audio Output"
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}