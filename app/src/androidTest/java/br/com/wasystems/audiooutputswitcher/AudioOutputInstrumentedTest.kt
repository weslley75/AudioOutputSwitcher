package br.com.wasystems.audiooutputswitcher

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioOutputInstrumentedTest {

    @Test
    fun packageName_isCorrect() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("br.com.wasystems.audiooutputswitcher", context.packageName)
    }

    @Test
    fun tileService_isRegisteredInManifest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val pm = context.packageManager
        val intent = Intent("android.service.quicksettings.action.QS_TILE")
        val services = pm.queryIntentServices(intent, 0)
        assertTrue(
            "AudioOutputTileService should be registered for QS_TILE action",
            services.any { it.serviceInfo.name == AudioOutputTileService::class.java.name }
        )
    }
}
