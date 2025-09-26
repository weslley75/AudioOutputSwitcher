package br.com.wasystems.audiooutputswitcher

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast

/**
 * Serviço de tile para configurações rápidas que permite acesso rápido ao
 * diálogo nativo de seleção de saída de áudio do Android.
 *
 * Este tile integra-se com o SystemUI do Android para abrir o diálogo
 * de saída de áudio que permite alternar entre dispositivos como
 * fones de ouvido, alto-falantes e dispositivos Bluetooth.
 *
 * @author WA Systems
 * @since 1.0.0
 */
class AudioOutputTileService : TileService() {
    companion object {
        private const val TAG = "AudioOutputTileService"
        private const val ACTION_MEDIA_OUTPUT =
            "com.android.systemui.action.LAUNCH_SYSTEM_MEDIA_OUTPUT_DIALOG"
        private const val PACKAGE_SYSTEMUI = "com.android.systemui"
        private const val RECEIVER_CLASS =
            "com.android.systemui.media.dialog.MediaOutputDialogReceiver"
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    /**
     * Chamado quando o usuário toca no tile.
     * Abre o diálogo de seleção de saída de áudio.
     */
    override fun onClick() {
        super.onClick()
        openMediaOutputDialog()
    }

    /**
     * Abre o diálogo nativo de seleção de saída de áudio do Android.
     *
     * Envia um broadcast para o SystemUI solicitando a abertura do
     * diálogo de saída de áudio. Em caso de erro, exibe uma mensagem
     * de toast para o usuário.
     */
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
            val errorMsg = "Failed to open audio output dialog: ${e.javaClass.simpleName}: ${e.message}"
            Log.e(TAG, errorMsg, e)
            showToast(errorMsg)
        }
    }

    /**
     * Atualiza as propriedades visuais do tile.
     *
     * Define o rótulo, ícone e estado do tile nas configurações rápidas.
     */
    private fun updateTile() {
        val tile = qsTile ?: return
        tile.label = getString(R.string.audio_output)
        tile.icon = Icon.createWithResource(this, R.drawable.ic_audio_output)
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }

    /**
     * Exibe uma mensagem toast para o usuário.
     *
     * @param message A mensagem a ser exibida
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}