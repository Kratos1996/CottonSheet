# CottonSheet Library ProGuard Rules

# Keep public library API
-keep class dev.ishant.cottonsheet.** { *; }

# Compose Multiplatform specific rules
# Preserves Composables and their generated code
-keepattributes Signature, InnerClasses, EnclosingMethod, AnnotationDefault
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
    @androidx.compose.runtime.ReadOnlyComposable *;
}

# Keep the CompositionLocal and Controller
-keep class dev.ishant.cottonsheet.CottonSheetController { *; }
-keep class dev.ishant.cottonsheet.CottonSheetControllerKt { *; }
-keep class dev.ishant.cottonsheet.CottonSheetParams { *; }
-keep class dev.ishant.cottonsheet.CottonSheetEntry { *; }

# Allow R8 to optimize but keep the names of these for stability in public API
-keepnames class dev.ishant.cottonsheet.**
