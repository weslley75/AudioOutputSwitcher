package com.example.audiooutputswitcher

import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AudioDeviceSelectorActivity : AppCompatActivity() {

    private lateinit var audioManager: AudioManager
    private lateinit var deviceListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_device_selector)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        deviceListView = findViewById(R.id.deviceListView)

        setupDeviceList()
    }

    private fun setupDeviceList() {
        val devices = getAvailableAudioDevices()
        val deviceNames = devices.map { getDeviceName(it.type) }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames)
        deviceListView.adapter = adapter

        deviceListView.setOnItemClickListener { _, _, position, _ ->
            // Infelizmente, não podemos alterar o dispositivo diretamente
            // sem permissões do sistema
            Toast.makeText(this,
                "O Android não permite trocar dispositivos sem permissões do sistema. " +
                        "Abra as configurações de som para alterar.",
                Toast.LENGTH_LONG
            ).show()

            // Abre as configurações de som
            val intent = Intent(android.provider.Settings.ACTION_SOUND_SETTINGS)
            startActivity(intent)
            finish()
        }
    }

    private fun getAvailableAudioDevices(): List<AudioDeviceInfo> {
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        return devices.filter {
            it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER ||
                    it.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP ||
                    it.type == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
                    it.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
                    it.type == AudioDeviceInfo.TYPE_USB_DEVICE ||
                    it.type == AudioDeviceInfo.TYPE_USB_HEADSET
        }
    }

    private fun getDeviceName(type: Int): String = when (type) {
        AudioDeviceInfo.TYPE_BUILTIN_SPEAKER -> "Alto-falante"
        AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> "Bluetooth"
        AudioDeviceInfo.TYPE_WIRED_HEADSET -> "Fone de ouvido com fio"
        AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> "Fones de ouvido com fio"
        AudioDeviceInfo.TYPE_USB_DEVICE -> "Dispositivo USB"
        AudioDeviceInfo.TYPE_USB_HEADSET -> "Fone de ouvido USB"
        else -> "Dispositivo desconhecido"
    }
}