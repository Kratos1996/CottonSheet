# CottonSheet 🌿

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kratos1996/cottonsheet)](https://search.maven.org/artifact/io.github.kratos1996/cottonsheet)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.3-blue)](https://github.com/JetBrains/compose-multiplatform)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](LICENSE)

**CottonSheet** is a zero-boilerplate, fully parameterized, and **stackable** `ModalBottomSheet` library for **Compose Multiplatform**. It allows you to manage bottom sheets globally via a simple controller API, keeping your UI logic clean and decoupled from state management.

---

## 🚀 Key Features

- **Multiplatform Support**: Android, iOS, Desktop (JVM), and Web (Wasm/JS).
- **Global Hosting**: Define your `CottonSheetHost` once at the root; trigger sheets from anywhere.
- **Stacked Sheets**: Open multiple sheets on top of each other. The library manages the stack automatically.
- **Zero Boilerplate**: No need to manually manage `rememberModalBottomSheetState` or visibility flags.
- **Customizable**: Full control over shapes, colors, elevation, and even custom drag handles.
- **Type-Safe**: Built with modern Kotlin and Compose Multiplatform best practices.

---

## 🧠 Understanding the Architecture

CottonSheet is built on three core pillars:

### 1. The `CottonSheetController`
The brain of the library. It maintains a `mutableStateListOf` of `CottonSheetEntry` objects. Every time you call `show()`, a new entry is pushed onto this stack. Each entry has a unique ID and its own set of `CottonSheetParams`.

### 2. The `CottonSheetHost`
The visual container. It should be placed at the root of your application. It observes the `CottonSheetController`'s stack and renders each entry using the standard Material 3 `ModalBottomSheet`. 
- **Isolated State**: Each sheet in the stack is wrapped in a `key(entry.id)`, ensuring that each has its own independent animation state and composition lifecycle.
- **Stacking Logic**: Sheets are rendered in the order they appear in the stack, creating a natural overlay effect.

### 3. `CottonSheetParams`
A comprehensive configuration object that lets you tweak everything from behavior (like `isDismissable` or `isFullScreen`) to aesthetics (like `containerColor` or `customDragHandle`).

---

## 📦 Installation

Add the dependency to your `commonMain` source set in `build.gradle.kts`:

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation("io.github.kratos1996:cottonsheet:1.0.1")
    }
}
```

---

## 🛠️ Setup

Wrap your application's root content with `CottonSheetHost`. This provides the `LocalCottonSheetController` to all children.

```kotlin
@Composable
fun App() {
    MaterialTheme {
        CottonSheetHost {
            // Your app content, e.g., Navigation Graph
            MainScreen()
        }
    }
}
```

---

## 💡 Usage

Access the controller via `LocalCottonSheetController.current`.

### Basic Sheet
```kotlin
val cottonSheet = LocalCottonSheetController.current

Button(onClick = {
    cottonSheet.show { dismiss ->
        Text("I am a CottonSheet!")
        Button(onClick = dismiss) { Text("Close Me") }
    }
}) {
    Text("Open Sheet")
}
```

### Stacked Sheets (The Power of CottonSheet)
Opening a sheet from within another sheet is seamless. The host handles the overlay automatically.

```kotlin
cottonSheet.show { dismiss1 ->
    Column {
        Text("Sheet 1")
        Button(onClick = {
            cottonSheet.show { dismiss2 ->
                Text("Sheet 2 (On top of Sheet 1)")
                Button(onClick = dismiss2) { Text("Close Sheet 2") }
                Button(onClick = { cottonSheet.dismissAll() }) { Text("Close Everything") }
            }
        }) {
            Text("Open Another Sheet")
        }
    }
}
```

---

## ⚙️ API Reference

### `CottonSheetParams`

| Parameter | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `skipPartiallyExpanded` | `Boolean` | `true` | If true, the sheet skips the half-expanded state. |
| `isFullScreen` | `Boolean` | `false` | If true, the sheet fills the entire screen height. |
| `isDismissable` | `Boolean` | `true` | Whether tapping outside or back press closes the sheet. |
| `showDragHandle` | `Boolean` | `true` | Whether to show the default M3 drag handle. |
| `customDragHandle` | `Composable?` | `null` | A custom Composable to replace the default handle. |
| `sheetMaxWidth` | `Dp` | `640.dp` | Maximum width (useful for tablets/desktop). |
| `containerColor` | `Color?` | `null` | Background color of the sheet. |
| `shape` | `Shape?` | `null` | Corner shape of the sheet. |

### `CottonSheetController`

- `show(params, content)`: Pushes a new sheet onto the stack.
- `dismiss()`: Dismisses the top-most sheet.
- `dismissAll()`: Clears the entire stack immediately.

---

## 🌍 Platform Support

| Platform | Support |
| :--- | :---: |
| **Android** | ✅ |
| **iOS** | ✅ |
| **Desktop (JVM)** | ✅ |
| **Web (Wasm/JS)** | ✅ |

---

## 📄 License

```text
Copyright 2026 Ishant Sharma

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```
