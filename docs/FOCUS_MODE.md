# Focus Mode - App Blocking Feature

## Overview

Focus Mode allows users to block distracting apps during timed habit sessions (e.g., "Read for 20 minutes"). This is an **advanced feature** for v1.2+.

---

## User Experience

### Enabling Focus Mode

1. User starts a timed habit
2. Optional prompt: "Block distracting apps while you focus?"
3. If yes, select apps to block (one-time setup, saved for future)
4. During habit timer, selected apps show a blocking screen

### Blocking Screen

```
┌─────────────────────────────────────────┐
│                                         │
│            🧘 Stay Focused              │
│                                         │
│      You're in the middle of:           │
│           "Read for 20 min"             │
│                                         │
│           12:34 remaining               │
│                                         │
│         [End Session Early]             │
│                                         │
└─────────────────────────────────────────┘
```

---

## iOS Implementation

### Frameworks Required

| Framework | Purpose |
|-----------|---------|
| **FamilyControls** | Authorization and app selection |
| **ManagedSettings** | Apply shields/blocks |
| **DeviceActivity** | Schedule restrictions |

### Entitlement Required

**Family Controls entitlement** - Must request from Apple (2-3 week approval)

### Key Code

```swift
// 1. Request Authorization
import FamilyControls

Task {
    do {
        try await AuthorizationCenter.shared
            .requestAuthorization(for: .individual)
    } catch {
        // Handle failure
    }
}

// 2. App Selection (SwiftUI)
struct SelectAppsView: View {
    @State private var selection = FamilyActivitySelection()
    @State private var pickerPresented = false

    var body: some View {
        Button("Select Apps to Block") {
            pickerPresented = true
        }
        .familyActivityPicker(
            isPresented: $pickerPresented,
            selection: $selection
        )
    }
}

// 3. Apply Block
import ManagedSettings

class FocusBlocker {
    let store = ManagedSettingsStore()

    func startBlocking(selection: FamilyActivitySelection) {
        store.shield.applications = selection.applicationTokens
        store.shield.applicationCategories = .specific(selection.categoryTokens)
    }

    func stopBlocking() {
        store.shield.applications = nil
        store.shield.applicationCategories = nil
    }
}
```

### iOS Extensions Needed

1. **DeviceActivityMonitor** - Responds to schedule events
2. **ShieldConfiguration** - Customizes blocking UI
3. **ShieldAction** - Handles user actions on shield

### iOS Limitations

- Cannot block Settings app
- No passcode protection (unlike native Screen Time)
- Safari-only for web filtering
- Token instability (iOS may randomly change app tokens)
- Requires iOS 15+ (better on iOS 16+)

---

## Android Implementation

### Recommended Approach: UsageStatsManager + Overlay

### Permissions Required

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
    tools:ignore="ProtectedPermissions" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
```

### Key Code

```kotlin
// 1. Check Permissions
class PermissionHelper(private val context: Context) {

    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }
}

// 2. Monitor Foreground App
class AppMonitorService : Service() {
    private val blockedPackages = mutableSetOf<String>()

    private fun getForegroundApp(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE)
            as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 60000 // Last minute

        val events = usageStatsManager.queryEvents(startTime, endTime)
        var lastApp: String? = null

        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                lastApp = event.packageName
            }
        }
        return lastApp
    }

    private fun checkAndBlock() {
        val foregroundApp = getForegroundApp()
        if (foregroundApp in blockedPackages) {
            showBlockingOverlay()
        } else {
            hideBlockingOverlay()
        }
    }
}

// 3. Blocking Overlay
class BlockingOverlay(private val context: Context) {
    private var overlayView: View? = null
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE)
        as WindowManager

    fun show(habitName: String, timeRemaining: String) {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        overlayView = LayoutInflater.from(context)
            .inflate(R.layout.blocking_overlay, null)

        windowManager.addView(overlayView, params)
    }

    fun hide() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
    }
}
```

### Android Limitations

- 500ms+ polling delay (not instant like iOS)
- Android 12+ overlay opacity restrictions (max 0.8)
- Cannot block system apps (Settings, Phone)
- Users can force-stop the service
- Battery optimization may kill service

---

## App Store Policy Considerations

### Apple App Store
- Family Controls entitlement requires explicit approval
- Must clearly explain why app needs these capabilities
- Cannot claim to "force" users - must be opt-in
- Privacy policy must disclose Screen Time data access

### Google Play Store
- `SYSTEM_ALERT_WINDOW` allowed but scrutinized
- Accessibility Service usage triggers manual review (avoid if possible)
- Must justify permissions in Data Safety form
- Cannot facilitate circumvention of other apps' security

---

## Implementation Priority

### MVP (Not for v1.0)
Focus Mode is an **advanced feature**. Prioritize core habit tracking first.

### v1.2 Implementation
1. Permission onboarding flow
2. App selection UI
3. Basic blocking (iOS shield / Android overlay)
4. Integration with habit timer

### v2.0 Enhancements
- Smart app suggestions (based on usage)
- Website blocking
- Break system (temporary unblock)
- Usage analytics

---

## Alternative: "Friction" Approach

Instead of hard blocking, consider the "One Sec" approach:

1. User opens blocked app
2. Show breathing exercise (4 seconds)
3. Then ask: "Still want to open [App]?"
4. User can proceed or go back

**Pros:**
- Less intrusive
- Doesn't require special permissions on Android
- Works with Shortcuts on iOS

**Cons:**
- Not true blocking
- Users can skip if determined

---

## Competitive Reference

| App | Approach | Platform |
|-----|----------|----------|
| **Opal** | Screen Time API | iOS |
| **Forest** | Gamification + blocking | iOS/Android |
| **One Sec** | Friction (breathing) | iOS |
| **Freedom** | VPN-based | Cross-platform |
| **ScreenZen** | Friction | Android |

---

## Technical Debt Warning

Focus Mode adds significant complexity:
- Multiple extensions (iOS)
- Foreground service (Android)
- Permission edge cases
- Platform version differences

**Recommendation:** Only implement if user research shows strong demand.
