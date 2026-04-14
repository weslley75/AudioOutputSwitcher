package br.com.wasystems.audiooutputswitcher

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import androidx.annotation.VisibleForTesting

/**
 * Serviço de tile para configurações rápidas que permite acesso rápido ao
 * diálogo nativo de seleção de saída de áudio do Android.
 *
 * Este tile integra-se com o SystemUI do Android para abrir o diálogo
 * de saída de áudio que permite alternar entre dispositivos como
 * fones de ouvido, alto-falantes e dispositivos Bluetooth.
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
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onStartListening — device=${Build.MANUFACTURER} ${Build.MODEL}, SDK=${Build.VERSION.SDK_INT}, release=${Build.VERSION.RELEASE}")
        }
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
        Log.d(TAG, "onClick — attempting broadcast to $RECEIVER_CLASS")

        if (!isReceiverAvailable()) {
            Log.w(TAG, "receiver not found in PackageManager, skipping broadcast")
            openFallback()
            return
        }

        try {
            val intent = makeMediaOutputIntent()
            Log.d(TAG, "sending broadcast: action=$ACTION_MEDIA_OUTPUT")
            sendBroadcast(intent)
            Log.d(TAG, "broadcast sent successfully")
        } catch (e: SecurityException) {
            Log.w(TAG, "broadcast blocked by system restriction, trying fallbacks", e)
            openFallback()
        } catch (e: Exception) {
            Log.e(TAG, "broadcast failed: ${e::class.simpleName}: ${e.message}", e)
            showToast(getString(R.string.error_opening_dialog))
        }
    }

    /**
     * Fallback em cascata quando o broadcast é bloqueado pelo sistema.
     * Nível 1: Volume Panel (inclui botão de output no Pixel/Android moderno)
     * Nível 2: Sound Settings
     */
    @VisibleForTesting internal fun openFallback() {
        val opened = AudioOutputFallback.tryEach { intent -> tryOpen(intent) }
        if (!opened) {
            Log.e(TAG, "all fallbacks failed")
            showToast(getString(R.string.error_opening_dialog))
        }
    }

    @SuppressLint("StartActivityAndCollapseDeprecated")
    private fun tryOpen(intent: Intent): Boolean {
        val action = intent.action ?: "Unknown"
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startActivityAndCollapse(
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                )
            } else {
                @Suppress("DEPRECATION")
                startActivityAndCollapse(intent)
            }
            Log.d(TAG, "opened $action as fallback")
            true
        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "$action activity not found: ${e.message}")
            false
        } catch (e: Exception) {
            Log.w(TAG, "$action fallback failed: ${e.message}")
            false
        }
    }

    @VisibleForTesting internal fun makeMediaOutputIntent(): Intent =
        Intent(ACTION_MEDIA_OUTPUT).apply {
            component = ComponentName(PACKAGE_SYSTEMUI, RECEIVER_CLASS)
        }

    @VisibleForTesting internal fun isReceiverAvailable(): Boolean {
        return try {
            val intent = makeMediaOutputIntent()
            val matches = packageManager.queryBroadcastReceivers(intent, PackageManager.MATCH_ALL)
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "queryBroadcastReceivers returned ${matches.size} match(es): ${matches.map { it.activityInfo.name }}")
            }
            matches.isNotEmpty()
        } catch (e: Exception) {
            Log.w(TAG, "queryBroadcastReceivers failed: ${e.message}")
            false
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
        tile.subtitle = getString(R.string.tile_subtitle)
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