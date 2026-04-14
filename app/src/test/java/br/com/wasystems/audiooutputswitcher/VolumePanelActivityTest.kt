package br.com.wasystems.audiooutputswitcher

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30, 33, 36])
class VolumePanelActivityTest {

    @Test
    fun onCreate_activityFinishes() {
        val controller = Robolectric.buildActivity(VolumePanelActivity::class.java).create()
        assertTrue(controller.get().isFinishing)
    }

    @Test
    fun onCreate_noException() {
        // ActivityNotFoundException from unresolved intents is silenced by tryEach
        Robolectric.buildActivity(VolumePanelActivity::class.java).create()
    }
}
