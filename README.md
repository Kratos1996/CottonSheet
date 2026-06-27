# Compose Multiplatform BottomSheet : CottonSheet

[![Maven Central](https://img.shields.io/maven-central/v/dev.ishant/compose-multiplatform-bottomsheet)](https://search.maven.org/artifact/dev.ishant/compose-multiplatform-bottomsheet)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.3-blue)](https://github.com/JetBrains/compose-multiplatform)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](LICENSE)

A zero-boilerplate, fully parameterized `ModalBottomSheet` for **Compose Multiplatform**. Manage your bottom sheets globally with a simple controller API, without polluting your UI logic with state management.

## 🚀 Features

- **Multiplatform Support**: Android, iOS, Desktop (JVM), and Web (Wasm/JS).
- **Global Hosting**: Define your `BottomSheetHost` once at the root; trigger sheets from anywhere.
- **Stacked Sheets**: Open multiple sheets on top of each other with built-in stack management.
- **Full Customization**: Control shapes, colors, elevations, and drag handles via `BottomSheetParams`.
- **Type-Safe**: Built with Kotlin and modern Compose Multiplatform practices.
- **Zero Boilerplate**: No need to manage `rememberModalBottomSheetState` or visibility flags manually.

---

## 📦 Installation

Add the dependency to your `commonMain` source set in `build.gradle.kts`:

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation("io.github.kratos1996:compose-multiplatform-bottomsheet:1.0.1")
    }
}
```

---

## 🛠️ Setup

1. **Configure Signing (for publishing)**:
   If you plan to publish to Maven Central, create a `local.properties` file based on `local.properties.template` and fill in your GPG signing keys and Maven Central credentials.

2. **Initialize BottomSheetHost**:
   Wrap your application's root content with `BottomSheetHost`. This should typically be done in your top-level Composable (e.g., inside your `MaterialTheme`).

```kotlin
@Composable
fun App() {
    MaterialTheme {
        BottomSheetHost {
            // Your app content goes here
            MainScreen()
        }
    }
}
```

---

## 💡 Usage

Access the `LocalBottomSheetController` to show or dismiss sheets. The content lambda provides a `ColumnScope` and a `dismiss` callback.

### 1. Basic Bottom Sheet
```kotlin
val controller = LocalBottomSheetController.current

Button(onClick = {
    controller.show { dismiss ->
        Text("Simple Bottom Sheet")
        Button(onClick = dismiss) { Text("Close") }
    }
}) {
    Text("Show Sheet")
}
```

### 2. Customizing Appearance
Use `BottomSheetParams` to tweak the behavior and look:

```kotlin
controller.show(
    params = BottomSheetParams(
        skipPartiallyExpanded = false,
        containerColor = Color.LightGray,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        showDragHandle = true
    )
) { dismiss ->
    Text("Custom Styled Sheet", Modifier.padding(16.dp))
}
```

### 3. Custom Drag Handle
You can replace the default drag handle with any Composable:

```kotlin
controller.show(
    params = BottomSheetParams(
        customDragHandle = {
            Box(
                Modifier
                    .padding(8.dp)
                    .size(40.dp, 4.dp)
                    .background(Color.Gray, CircleShape)
            )
        }
    )
) { 
    Text("Sheet with Custom Handle")
}
```

---

## ⚙️ API Reference: `BottomSheetParams`

| Parameter | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `skipPartiallyExpanded` | `Boolean` | `true` | Whether the sheet skips the partially expanded state. |
| `sheetMaxWidth` | `Dp` | `640.dp` | Maximum width of the sheet (useful for Tablets/Desktop). |
| `shape` | `Shape?` | `null` | The shape of the bottom sheet. |
| `containerColor` | `Color?` | `null` | Background color of the sheet. |
| `contentColor` | `Color?` | `null` | Color of the content inside the sheet. |
| `tonalElevation` | `Dp` | `1.dp` | Tonal elevation of the sheet. |
| `scrimColor` | `Color?` | `null` | Color of the background scrim. |
| `showDragHandle` | `Boolean` | `true` | Whether to show the default Material 3 drag handle. |
| `isFullScreen` | `Boolean` | `false` | If true, the sheet will fill the entire screen height. |
| `isDismissable` | `Boolean` | `true` | Whether the sheet can be dismissed by tapping outside or back press. |
| `dismissRequest` | `(() -> Unit)?` | `null` | Callback triggered when a dismiss is requested (if `isDismissable` is true). |
| `customDragHandle` | `@Composable () -> Unit` | `null` | A custom Composable to use as the drag handle. |

---

## 🌍 Platform Support

| Platform | Support |
| :--- | :---: |
| **Android** | ✅ |
| **iOS** | ✅ |
| **Desktop (JVM)** | ✅ |
| **Web (Wasm/JS)** | ✅ |

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an issue for bugs and feature requests.

## 📄 License

```text
Copyright 2026 Ishant Sharma

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```
