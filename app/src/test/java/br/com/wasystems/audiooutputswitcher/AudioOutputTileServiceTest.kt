package br.com.wasystems.audiooutputswitcher

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.provider.Settings
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class AudioOutputTileServiceTest {

    private lateinit var service: AudioOutputTileService

    @Before
    fun setUp() {
        service = Robolectric.setupService(AudioOutputTileService::class.java)
    }

    private fun registerSystemUiReceiver(svc: AudioOutputTileService) {
        val shadowPm = Shadows.shadowOf(svc.packageManager)
        val info = ResolveInfo().apply {
            activityInfo = ActivityInfo().apply {
                packageName = "com.android.systemui"
                name = "com.android.systemui.media.dialog.MediaOutputDialogReceiver"
            }
        }
        @Suppress("DEPRECATION")
        shadowPm.addResolveInfoForIntent(svc.makeMediaOutputIntent(), info)
    }

    // T4 — makeMediaOutputIntent() construction

    @Test
    fun makeMediaOutputIntent_hasCorrectAction() {
        val intent = service.makeMediaOutputIntent()
        assertEquals(
            "com.android.systemui.action.LAUNCH_SYSTEM_MEDIA_OUTPUT_DIALOG",
            intent.action
        )
    }

    @Test
    fun makeMediaOutputIntent_hasCorrectComponent() {
        val intent = service.makeMediaOutputIntent()
        assertEquals("com.android.systemui", intent.component?.packageName)
        assertEquals(
            "com.android.systemui.media.dialog.MediaOutputDialogReceiver",
            intent.component?.className
        )
    }

    // T5 — broadcast path and SecurityException fallback

    @Test
    fun onClick_sendsBroadcast() {
        registerSystemUiReceiver(service)
        val spy = spyk(service)
        every { spy.sendBroadcast(any()) } just runs
        spy.onClick()
        verify {
            spy.sendBroadcast(match {
                it.action == "com.android.systemui.action.LAUNCH_SYSTEM_MEDIA_OUTPUT_DIALOG"
            })
        }
    }

    @Test
    fun onClick_securityException_triggersFallback() {
        registerSystemUiReceiver(service)
        val spy = spyk(service)
        every { spy.sendBroadcast(any()) } throws SecurityException("blocked")
        every { spy.openFallback() } just runs
        spy.onClick()
        verify { spy.openFallback() }
    }

    @Test
    fun openFallback_volumePanelSucceeds_doesNotTrySoundSettings() {
        val spy = spyk(service)
        @Suppress("DEPRECATION")
        every {
            spy.startActivityAndCollapse(match<Intent> { it.action == Settings.Panel.ACTION_VOLUME })
        } just runs
        spy.openFallback()
        @Suppress("DEPRECATION")
        verify(exactly = 0) {
            spy.startActivityAndCollapse(match<Intent> { it.action == Settings.ACTION_SOUND_SETTINGS })
        }
    }

    @Test
    fun openFallback_allFallbacksFail_showsToast() {
        val spy = spyk(service)
        @Suppress("DEPRECATION")
        every { spy.startActivityAndCollapse(any<Intent>()) } throws Exception("unavailable")
        spy.openFallback()
        assertNotNull("Expected a Toast to be shown when all fallbacks fail", ShadowToast.getLatestToast())
    }

    // T6 — isReceiverAvailable()

    @Test
    fun isReceiverAvailable_withMatches_returnsTrue() {
        registerSystemUiReceiver(service)
        assertTrue(service.isReceiverAvailable())
    }

    @Test
    fun isReceiverAvailable_noMatches_returnsFalse() {
        assertFalse(service.isReceiverAvailable())
    }

    @Test
    fun isReceiverAvailable_queryThrows_returnsFalse() {
        val spy = spyk(service)
        every { spy.packageManager } throws RuntimeException("pm unavailable")
        assertFalse(spy.isReceiverAvailable())
    }
}
