# MoneyApp

A Kotlin Multiplatform application for managing personal finances, with support for Android and desktop platforms.

## Overview

MoneyApp is a simple, cross-platform application designed to help you track your income and expenses. This project serves as a template for building Kotlin Multiplatform applications with a shared UI using Jetpack Compose.

## Features

*   Cross-platform: Runs on Android and desktop (Windows, macOS, Linux).
*   Shared UI: The user interface is built with Jetpack Compose and shared between all platforms.
*   Modern Toolkit: Built with the latest technologies like Kotlin, Coroutines, and Jetpack Compose.

## Getting Started

### Prerequisites

*   Java Development Kit (JDK) 11 or higher
*   Android Studio (for running the Android app)

### Building and Running

**Desktop:**

To run the desktop application, execute the following command in the project root:

```bash
./gradlew :desktopApp:run
```

**Android:**

To build the Android application, you can use Android Studio or run the following command:

```bash
./gradlew :androidApp:assembleDebug
```

This will generate an APK file in `androidApp/build/outputs/apk/debug/`.

## Project Structure

The project is divided into three modules:

*   `shared`: A Kotlin Multiplatform module containing the shared business logic and UI (Jetpack Compose).
*   `androidApp`: An Android application module that uses the `shared` module.
*   `desktopApp`: A desktop application module (using Compose for Desktop) that also uses the `shared` module.

## Technologies Used

*   [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
*   [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   [Gradle](https://gradle.org/)
