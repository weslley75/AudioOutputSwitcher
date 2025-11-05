# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android application that provides a Quick Settings tile for switching audio output devices. The app creates a system tile that:
- **Tap**: Opens the native Android media output dialog for switching between audio devices (Bluetooth, speakers, etc.)
- **Long-press**: Opens the Sound Profiles activity for controlling individual volume streams (Media, Ring, Notification, Alarm, System)

## Build Commands

- **Build the project**: `./gradlew build`
- **Assemble debug APK**: `./gradlew assembleDebug`
- **Assemble release APK**: `./gradlew assembleRelease`
- **Install debug build on connected device**: `./gradlew installDebug`
- **Run unit tests**: `./gradlew test`
- **Run instrumented tests**: `./gradlew connectedAndroidTest`
- **Clean project**: `./gradlew clean`

## Architecture

### Core Components

- **AudioOutputTileService**: Main tile service that extends `TileService` to provide Quick Settings functionality
  - Location: `app/src/main/java/br/com/wasystems/audiooutputswitcher/AudioOutputTileService.kt`
  - Integrates with Android System UI to open the media output dialog
  - Uses PendingIntent to send broadcast to SystemUI's MediaOutputDialogReceiver

- **SoundProfilesActivity**: Activity for controlling volume streams
  - Location: `app/src/main/java/br/com/wasystems/audiooutputswitcher/SoundProfilesActivity.kt`
  - Launched when the user long-presses the Quick Settings tile
  - Provides SeekBar controls for Media, Ring, Notification, Alarm, and System volume streams
  - Uses AudioManager to read and set volume levels for each stream

### Key Configuration

- **Target SDK**: 35 (Android 15)
- **Min SDK**: 35 (Android 15 only)
- **Java/Kotlin**: Version 17
- **Namespace**: `br.com.wasystems.audiooutputswitcher`
- **Build features**: Gradle configuration cache enabled, code minification enabled for release builds

### Manifest Configuration

The app registers a Quick Settings tile service with:
- Permission: `android.permission.BIND_QUICK_SETTINGS_TILE`
- Intent filter: `android.service.quicksettings.action.QS_TILE`
- Custom icon: `@drawable/ic_audio_output`

### System Integration

The tile communicates with Android's SystemUI through:
- Action: `com.android.systemui.action.LAUNCH_SYSTEM_MEDIA_OUTPUT_DIALOG`
- Target: `com.android.systemui.media.dialog.MediaOutputDialogReceiver`

### Localization

Supports Portuguese (Brazil) localization in addition to English.

## Development Notes

- The app has no launcher Activity - it's purely a tile service with a preferences Activity
- The SoundProfilesActivity is accessed via long-press on the Quick Settings tile (uses QS_TILE_PREFERENCES intent filter)
- Error handling includes logging and Toast messages for user feedback
- Uses modern Android development practices with Kotlin
- Gradle configuration cache is enabled for improved build performance
- Minimal dependencies - uses only Android framework libraries