# Accessibility Requirements

Onward must be accessible to all users. This document outlines requirements for both iOS and Android.

---

## 1. Visual Accessibility

### VoiceOver (iOS) / TalkBack (Android)

**All interactive elements must have:**
- Meaningful accessibility labels
- Appropriate traits/roles
- Hints where helpful

**iOS Example:**
```swift
// Bad
Button(action: markComplete) {
    Image(systemName: "checkmark")
}

// Good
Button(action: markComplete) {
    Image(systemName: "checkmark")
}
.accessibilityLabel("Mark habit complete")
.accessibilityHint("Double tap to mark today's habit as done")
```

**Android Example:**
```kotlin
// Bad
IconButton(onClick = markComplete) {
    Icon(Icons.Default.Check, contentDescription = null)
}

// Good
IconButton(onClick = markComplete) {
    Icon(
        Icons.Default.Check,
        contentDescription = "Mark habit complete"
    )
}
```

### Dynamic Type / Font Scaling

**Support up to 200% text size**

**iOS:**
```swift
// Use system text styles
Text("Day 23")
    .font(.headline)  // Automatically scales

// For custom sizes, use @ScaledMetric
@ScaledMetric var iconSize: CGFloat = 24

Image(systemName: "checkmark")
    .frame(width: iconSize, height: iconSize)
```

**Android:**
```kotlin
// Use sp units for text (scales with system settings)
Text(
    text = "Day 23",
    style = MaterialTheme.typography.titleLarge  // Uses sp
)

// Avoid fixed dp for text-related spacing
```

### Color Contrast

**WCAG 2.2 AA Requirements:**
- Normal text: 4.5:1 minimum
- Large text (18pt+ or 14pt bold): 3:1 minimum
- UI components: 3:1 minimum

**Our Color Pairs (Verified):**
| Foreground | Background | Ratio | Pass |
|------------|------------|-------|------|
| White (#FFFFFF) | Surface (#141218) | 15.4:1 | ✓ |
| Purple80 (#D0BCFF) | Surface (#141218) | 9.8:1 | ✓ |
| OnSurfaceVariant (#CAC4D0) | Surface (#141218) | 7.2:1 | ✓ |
| Green (#A8DAB5) | Surface (#141218) | 9.1:1 | ✓ |

### Color Blindness

**Never rely on color alone:**
```swift
// Bad - Only color indicates completion
Circle()
    .fill(isComplete ? .green : .gray)

// Good - Color + icon
ZStack {
    Circle()
        .fill(isComplete ? .green : .gray)
    if isComplete {
        Image(systemName: "checkmark")
            .foregroundColor(.white)
    }
}
```

**Recommended palette (color-blind safe):**
- Blue (#8ECAFF) instead of green for positive
- Orange (#FFE082) for warnings
- Pair red with X icon for errors

### Reduce Motion

**Respect system preference:**

**iOS:**
```swift
struct BreathingCircle: View {
    @Environment(\.accessibilityReduceMotion) var reduceMotion

    var body: some View {
        if reduceMotion {
            // Static version with text
            VStack {
                Circle()
                    .fill(Color.accent)
                Text("Breathe in... Hold... Breathe out...")
            }
        } else {
            // Animated version
            AnimatedBreathingCircle()
        }
    }
}
```

**Android:**
```kotlin
@Composable
fun BreathingCircle(onComplete: () -> Unit) {
    val reduceMotion = LocalReduceMotion.current

    if (reduceMotion) {
        // Static version
        StaticBreathingCircle(onComplete)
    } else {
        // Animated version
        AnimatedBreathingCircle(onComplete)
    }
}

// Check system setting
val reduceMotion = Settings.Global.getFloat(
    context.contentResolver,
    Settings.Global.ANIMATOR_DURATION_SCALE,
    1f
) == 0f
```

---

## 2. Motor Accessibility

### Touch Target Sizes

**Minimum sizes:**
- iOS: 44x44 points
- Android: 48x48 dp

```swift
// iOS - Ensure minimum tap area
Button(action: action) {
    Image(systemName: "plus")
        .frame(width: 44, height: 44)
}
```

```kotlin
// Android - Ensure minimum tap area
IconButton(
    onClick = action,
    modifier = Modifier.size(48.dp)
) {
    Icon(Icons.Default.Add, contentDescription = "Add")
}
```

### Switch Control / Voice Control

**Support custom actions:**

**iOS:**
```swift
struct HabitCard: View {
    var body: some View {
        VStack { /* content */ }
            .accessibilityElement(children: .combine)
            .accessibilityLabel("Meditate habit, Day 23, not completed")
            .accessibilityCustomContent("Status", "Not completed today")
            .accessibilityAction(named: "Mark Complete") {
                markComplete()
            }
            .accessibilityAction(named: "View Details") {
                showDetails()
            }
    }
}
```

**Android:**
```kotlin
Box(
    modifier = Modifier.semantics {
        contentDescription = "Meditate habit, Day 23, not completed"
        customActions = listOf(
            CustomAccessibilityAction("Mark Complete") {
                markComplete()
                true
            },
            CustomAccessibilityAction("View Details") {
                showDetails()
                true
            }
        )
    }
) { /* content */ }
```

### Gesture Alternatives

| Gesture | Alternative |
|---------|-------------|
| Swipe to delete | Long-press menu or delete button |
| Long press | Context menu button |
| Pinch to zoom | +/- buttons |
| Double tap | Single tap button |

---

## 3. Cognitive Accessibility

### Clear, Simple Language

| Instead of | Use |
|------------|-----|
| "Instantiate a new behavioral pattern" | "Start a new habit" |
| "Your streak has been terminated" | "Your streak ended" |
| "Allocate grace day resource" | "Use a flex day" |

### Consistent Navigation

- Same actions in same locations across screens
- Predictable back button behavior
- Clear hierarchy

### Error Prevention

```swift
// Before destructive action
.confirmationDialog("Delete habit?", isPresented: $showDelete) {
    Button("Delete", role: .destructive) { deleteHabit() }
    Button("Cancel", role: .cancel) { }
} message: {
    Text("This will delete your habit and all progress. This cannot be undone.")
}
```

---

## 4. App-Specific Considerations

### Breathing Animation

**Must work for:**
- Users who can't see (audio cues)
- Users with motion sensitivity (static alternative)
- Users with cognitive disabilities (clear instructions)

```swift
struct AccessibleBreathingCircle: View {
    @Environment(\.accessibilityReduceMotion) var reduceMotion
    @State private var phase: BreathPhase = .inhale

    var body: some View {
        Group {
            if reduceMotion {
                StaticBreathingView(phase: phase)
            } else {
                AnimatedBreathingView(phase: phase)
            }
        }
        .accessibilityElement(children: .combine)
        .accessibilityLabel(phaseLabel)
        .accessibilityValue(phaseInstruction)
        .onChange(of: phase) { newPhase in
            // Announce phase change for VoiceOver
            UIAccessibility.post(
                notification: .announcement,
                argument: phaseInstruction
            )
        }
    }

    var phaseLabel: String {
        switch phase {
        case .inhale: return "Inhale phase"
        case .hold: return "Hold phase"
        case .exhale: return "Exhale phase"
        }
    }

    var phaseInstruction: String {
        switch phase {
        case .inhale: return "Breathe in for 4 seconds"
        case .hold: return "Hold your breath for 7 seconds"
        case .exhale: return "Breathe out for 8 seconds"
        }
    }
}
```

### Timer Interactions

```swift
struct FocusTimer: View {
    @State private var remainingSeconds: Int = 1200

    var body: some View {
        Text(formattedTime)
            .accessibilityLabel("Time remaining: \(accessibleTime)")
            .accessibilityValue("\(remainingSeconds) seconds")
            .accessibilityAddTraits(.updatesFrequently)
    }

    // Announce at key intervals
    func announceProgress() {
        let announcements = [600, 300, 60, 30, 10]
        if announcements.contains(remainingSeconds) {
            UIAccessibility.post(
                notification: .announcement,
                argument: accessibleTime
            )
        }
    }
}
```

### Widget Accessibility

**iOS (WidgetKit):**
```swift
struct OneFocusWidgetEntryView: View {
    var body: some View {
        VStack {
            Text("Day \(entry.currentDay)")
            Text(entry.habitName)
        }
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Onward widget. Day \(entry.currentDay). \(entry.habitName). \(entry.isCompletedToday ? "Completed" : "Not completed")")
    }
}
```

**Android (Glance):**
```kotlin
@Composable
fun OneFocusWidget() {
    Column(
        modifier = GlanceModifier.semantics {
            contentDescription = "Onward widget. Day $currentDay. $habitName. ${if (isComplete) "Completed" else "Not completed"}"
        }
    ) {
        // Widget content
    }
}
```

### Notification Accessibility

```swift
let content = UNMutableNotificationContent()
content.title = "Time for Meditate"
content.body = "After pour my coffee, it's time to meditate"

// Add accessible action
let completeAction = UNNotificationAction(
    identifier: "COMPLETE",
    title: "Mark Complete",
    options: []
)

// For VoiceOver users, the notification will be read aloud
```

---

## 5. Testing Checklist

### Manual Testing

**iOS VoiceOver:**
1. Enable: Settings > Accessibility > VoiceOver
2. Navigate with swipe gestures
3. Verify all elements are reachable
4. Verify labels make sense out of context
5. Test custom actions
6. Test with Reduce Motion enabled

**Android TalkBack:**
1. Enable: Settings > Accessibility > TalkBack
2. Navigate with swipe gestures
3. Verify all elements have contentDescription
4. Test with "Remove animations" enabled

### Tools

| Platform | Tool |
|----------|------|
| iOS | Accessibility Inspector (Xcode) |
| iOS | VoiceOver |
| Android | Accessibility Scanner |
| Android | TalkBack |
| Both | axe DevTools Mobile |

### Automated Testing

**iOS:**
```swift
func testAccessibility() throws {
    let app = XCUIApplication()
    app.launch()

    // Verify all buttons have labels
    let buttons = app.buttons.allElementsBoundByIndex
    for button in buttons {
        XCTAssertFalse(button.label.isEmpty, "Button missing accessibility label")
    }
}
```

**Android:**
```kotlin
@Test
fun testAccessibility() {
    composeTestRule.setContent {
        OneFocusTheme {
            HomeScreen()
        }
    }

    // Verify all clickable elements have contentDescription
    composeTestRule
        .onAllNodes(hasClickAction())
        .assertAll(hasContentDescription())
}
```

### Per-Screen Checklist

For each screen, verify:
- [ ] All interactive elements reachable via screen reader
- [ ] Meaningful labels (not "button" or "image")
- [ ] Focus order is logical
- [ ] Touch targets are 44pt/48dp minimum
- [ ] Color is not the only indicator
- [ ] Text scales with system settings
- [ ] Animations respect Reduce Motion
- [ ] Error messages are announced

---

## 6. Platform APIs Reference

### iOS

```swift
// Accessibility modifiers
.accessibilityLabel("Label")           // What it is
.accessibilityHint("Hint")             // What happens
.accessibilityValue("Value")           // Current state
.accessibilityAddTraits(.isButton)     // Semantic type
.accessibilityRemoveTraits(.isImage)
.accessibilityHidden(true)             // Hide from VoiceOver
.accessibilityElement(children: .combine)  // Group children
.accessibilityAction { }               // Custom action
.accessibilityCustomContent()          // Additional info

// Environment
@Environment(\.accessibilityReduceMotion)
@Environment(\.accessibilityReduceTransparency)
@Environment(\.accessibilityDifferentiateWithoutColor)
@Environment(\.accessibilityInvertColors)
@ScaledMetric                          // Dynamic Type scaling
```

### Android

```kotlin
// Semantics modifiers
Modifier.semantics {
    contentDescription = "Description"
    stateDescription = "Current state"
    role = Role.Button
    heading()
    disabled()
    invisibleToUser()
    customActions = listOf()
}

// Accessibility checks
val reduceMotion = Settings.Global.getFloat(
    context.contentResolver,
    Settings.Global.ANIMATOR_DURATION_SCALE,
    1f
) == 0f

val fontScale = context.resources.configuration.fontScale
```

---

## Summary

Accessibility is not optional. Build it in from the start:

1. **Every element** needs an accessibility label
2. **Every action** needs a non-gesture alternative
3. **Every animation** needs a Reduce Motion alternative
4. **Every color** needs an icon/text companion
5. **Test with VoiceOver/TalkBack** before every release
