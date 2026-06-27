# CottonSheet Consumer ProGuard Rules
# These rules are automatically applied to any Android project that depends on this library.

# Preserve the library's public API to prevent R8 from stripping it in the app
-keep class dev.ishant.cottonsheet.** { *; }

# Preserve Compose annotations and metadata
-keepattributes Signature, InnerClasses, EnclosingMethod, AnnotationDefault
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
    @androidx.compose.runtime.ReadOnlyComposable *;
}

# Ensure the controller, parameters, and composition local are not obfuscated/stripped
-keep class dev.ishant.cottonsheet.CottonSheetController { *; }
-keep class dev.ishant.cottonsheet.CottonSheetControllerKt { *; }
-keep class dev.ishant.cottonsheet.CottonSheetParams { *; }
-keep class dev.ishant.cottonsheet.CottonSheetEntry { *; }
