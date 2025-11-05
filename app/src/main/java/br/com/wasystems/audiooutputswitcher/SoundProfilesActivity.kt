package br.com.wasystems.audiooutputswitcher

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.SeekBar

/**
 * Activity that provides volume controls for different audio streams.
 * Launched when the user long-presses the Quick Settings tile.
 *
 * @author WA Systems
 * @since 1.1.0
 */
class SoundProfilesActivity : Activity() {
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_profiles)

        // Enable dismissing dialog when touching outside
        setFinishOnTouchOutside(true)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        setupVolumeControl(R.id.seekBarMedia, AudioManager.STREAM_MUSIC)
        setupVolumeControl(R.id.seekBarRing, AudioManager.STREAM_RING)
        setupVolumeControl(R.id.seekBarNotification, AudioManager.STREAM_NOTIFICATION)
        setupVolumeControl(R.id.seekBarAlarm, AudioManager.STREAM_ALARM)
        setupVolumeControl(R.id.seekBarSystem, AudioManager.STREAM_SYSTEM)
    }

    /**
     * Sets up a volume control SeekBar for a specific audio stream.
     *
     * @param seekBarId The resource ID of the SeekBar
     * @param streamType The audio stream type (e.g., STREAM_MUSIC, STREAM_RING)
     */
    private fun setupVolumeControl(seekBarId: Int, streamType: Int) {
        val seekBar = findViewById<SeekBar>(seekBarId)
        val maxVolume = audioManager.getStreamMaxVolume(streamType)
        val currentVolume = audioManager.getStreamVolume(streamType)

        seekBar.max = maxVolume
        seekBar.progress = currentVolume

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audioManager.setStreamVolume(streamType, progress, 0)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
