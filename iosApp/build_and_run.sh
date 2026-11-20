#!/bin/bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$DIR/.." && pwd)"

echo "==> Building SampleApp.framework for iOS Simulator..."
"$ROOT_DIR/gradlew" -p "$ROOT_DIR" :sample:linkDebugFrameworkIosSimulatorArm64

APP_BUNDLE="$DIR/build/CottonSheetSample.app"
mkdir -p "$APP_BUNDLE"
cp "$DIR/iosApp/Info.plist" "$APP_BUNDLE/Info.plist"

echo "==> Compiling Swift iOS App..."
SDK_PATH=$(xcrun --sdk iphonesimulator --show-sdk-path)
xcrun --sdk iphonesimulator swiftc \
  -target arm64-apple-ios17.0-simulator \
  -sdk "$SDK_PATH" \
  -F "$ROOT_DIR/sample/build/bin/iosSimulatorArm64/debugFramework" \
  -framework SampleApp \
  "$DIR/iosApp/iOSApp.swift" "$DIR/iosApp/ContentView.swift" \
  -o "$APP_BUNDLE/iosApp"

echo "==> Codesigning app bundle..."
codesign -s - --force --deep "$APP_BUNDLE"

DEVICE_ID=$(xcrun simctl list devices | grep -E "iPhone 1[78].*\(Booted\)" | head -n 1 | sed -E 's/.*\(([A-F0-9-]+)\).*/\1/')
if [ -z "$DEVICE_ID" ]; then
    DEVICE_ID=$(xcrun simctl list devices available | grep -E "iPhone 1[78]" | head -n 1 | sed -E 's/.*\(([A-F0-9-]+)\).*/\1/')
    echo "==> Booting Simulator ($DEVICE_ID)..."
    xcrun simctl boot "$DEVICE_ID" || true
fi

echo "==> Installing app on simulator ($DEVICE_ID)..."
xcrun simctl install "$DEVICE_ID" "$APP_BUNDLE"

echo "==> Launching CottonSheet sample app..."
xcrun simctl launch "$DEVICE_ID" dev.ishant.sample.iosApp

echo "==> Done! App is running on iOS Simulator."
