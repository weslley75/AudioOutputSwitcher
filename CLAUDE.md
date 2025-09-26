# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android application that provides a Quick Settings tile for switching audio output devices. The app creates a system tile that opens the native Android media output dialog when tapped.

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
  - Location: `app/src/main/java/com/example/audiooutputswitcher/AudioOutputTileService.kt`
  - Integrates with Android System UI to open the media output dialog
  - Uses PendingIntent to send broadcast to SystemUI's MediaOutputDialogReceiver

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

- The app has no main Activity - it's purely a tile service
- Error handling includes logging and Toast messages for user feedback
- Uses modern Android development practices with Kotlin and AndroidX libraries
- Gradle configuration cache is enabled for improved build performance