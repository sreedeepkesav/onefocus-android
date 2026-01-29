# Onward - Claude Code Project Guide

## Project Overview

**Onward** is a minimalist habit-building app based on the "Radical Constraints" philosophy. It deliberately limits users to ONE habit at a time (with a second habit unlocking after 21 days), using behavioral science principles to maximize success rates.

**Tagline:** *"Moving forward, one habit at a time."*

### Core Philosophy
- **One Focus**: Only 1-2 habits active at any time (prevents overwhelm)
- **Trigger-Based**: Every habit is anchored to an existing behavior (except repeating habits)
- **66-Day Journey**: Science-backed habit formation timeline (hide this initially in onboarding)
- **Flex Days**: 3 allowed misses to prevent all-or-nothing thinking (not "grace days")
- **Parallel Habits** (not stacking): Second habit is independent, not chained
- **$1/year**: Radical pricing to remove friction

### Documentation Index
- `docs/PRODUCT_DECISIONS.md` - UX/UI decisions, business strategy, pricing
- `docs/IOS_ARCHITECTURE.md` - Complete iOS implementation spec
- `docs/ANDROID_ARCHITECTURE.md` - Complete Android implementation spec
- `docs/ACCESSIBILITY.md` - VoiceOver, TalkBack, Dynamic Type requirements
- `docs/LOCALIZATION.md` - i18n/L10n implementation guide
- `docs/FOCUS_MODE.md` - App blocking feature (v1.2+)

## Tech Stack - Native Development

### iOS
```
Language: Swift 5.9+
UI Framework: SwiftUI
Architecture: MVVM with Observable
Local Storage: SwiftData (iOS 17+) or Core Data
Notifications: UserNotifications
Widgets: WidgetKit
Min Deployment: iOS 17.0
```

### Android
```
Language: Kotlin 1.9+
UI Framework: Jetpack Compose
Architecture: MVVM with StateFlow
Local Storage: Room + DataStore
Notifications: WorkManager + NotificationCompat
Widgets: Glance (Compose for Widgets)
Min SDK: API 26 (Android 8.0)
```

---

## iOS Project Structure

```
OneFocus/
├── App/
│   ├── OneFocusApp.swift           # App entry point
│   └── AppDelegate.swift           # App lifecycle (if needed)
│
├── Core/
│   ├── Design/
│   │   ├── Colors.swift            # Color tokens
│   │   ├── Typography.swift        # Text styles
│   │   └── Haptics.swift           # Haptic feedback manager
│   ├── Extensions/
│   │   ├── Date+Extensions.swift
│   │   └── View+Extensions.swift
│   └── Utilities/
│       └── DateHelper.swift
│
├── Features/
│   ├── Onboarding/
│   │   ├── Views/
│   │   │   ├── WelcomeView.swift
│   │   │   ├── HabitTypeView.swift
│   │   │   ├── HabitNameView.swift
│   │   │   ├── TriggerSetupView.swift
│   │   │   └── ReadyView.swift
│   │   ├── Components/
│   │   │   ├── ScienceCard.swift
│   │   │   ├── HabitTypeCard.swift
│   │   │   └── IntentionBuilder.swift
│   │   └── OnboardingViewModel.swift
│   │
│   ├── Home/
│   │   ├── Views/
│   │   │   └── HomeView.swift
│   │   ├── Components/
│   │   │   ├── JourneyCard.swift
│   │   │   ├── HabitCard.swift
│   │   │   ├── QuickLogButton.swift
│   │   │   ├── MoodTracker.swift
│   │   │   └── UnlockCard.swift
│   │   └── HomeViewModel.swift
│   │
│   ├── Focus/
│   │   ├── Views/
│   │   │   └── FocusView.swift
│   │   ├── Components/
│   │   │   ├── BreathingCircle.swift
│   │   │   ├── FocusTimer.swift
│   │   │   └── CelebrationView.swift
│   │   └── FocusViewModel.swift
│   │
│   ├── Analytics/
│   │   ├── Views/
│   │   │   └── AnalyticsView.swift
│   │   ├── Components/
│   │   │   ├── CalendarHeatmap.swift
│   │   │   ├── MoodChart.swift
│   │   │   └── StatsCard.swift
│   │   └── AnalyticsViewModel.swift
│   │
│   └── SecondHabit/
│       ├── Views/
│       │   └── AddSecondHabitView.swift
│       └── SecondHabitViewModel.swift
│
├── Models/
│   ├── Habit.swift                 # @Model for SwiftData
│   ├── Journey.swift
│   ├── MoodEntry.swift
│   └── Enums/
│       ├── HabitType.swift
│       └── TriggerType.swift
│
├── Services/
│   ├── DataService.swift           # SwiftData manager
│   ├── NotificationService.swift
│   └── WidgetService.swift         # Widget data sync
│
├── Widget/
│   ├── OneFocusWidget.swift
│   ├── WidgetEntry.swift
│   └── WidgetViews/
│       ├── SmallWidgetView.swift
│       └── MediumWidgetView.swift
│
└── Resources/
    ├── Assets.xcassets
    └── Localizable.strings
```

---

## Android Project Structure

```
app/
├── src/main/
│   ├── java/com/onefocus/app/
│   │   ├── OneFocusApp.kt              # Application class
│   │   ├── MainActivity.kt
│   │   │
│   │   ├── core/
│   │   │   ├── design/
│   │   │   │   ├── Color.kt            # Color tokens
│   │   │   │   ├── Type.kt             # Typography
│   │   │   │   └── Theme.kt            # Material 3 theme
│   │   │   ├── navigation/
│   │   │   │   └── NavGraph.kt
│   │   │   └── util/
│   │   │       ├── DateHelper.kt
│   │   │       └── HapticManager.kt
│   │   │
│   │   ├── feature/
│   │   │   ├── onboarding/
│   │   │   │   ├── OnboardingScreen.kt
│   │   │   │   ├── HabitTypeScreen.kt
│   │   │   │   ├── TriggerSetupScreen.kt
│   │   │   │   ├── OnboardingViewModel.kt
│   │   │   │   └── components/
│   │   │   │       ├── ScienceCard.kt
│   │   │   │       └── HabitTypeCard.kt
│   │   │   │
│   │   │   ├── home/
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   ├── HomeViewModel.kt
│   │   │   │   └── components/
│   │   │   │       ├── JourneyCard.kt
│   │   │   │       ├── HabitCard.kt
│   │   │   │       └── MoodTracker.kt
│   │   │   │
│   │   │   ├── focus/
│   │   │   │   ├── FocusScreen.kt
│   │   │   │   ├── FocusViewModel.kt
│   │   │   │   └── components/
│   │   │   │       ├── BreathingCircle.kt
│   │   │   │       └── CelebrationAnimation.kt
│   │   │   │
│   │   │   └── analytics/
│   │   │       ├── AnalyticsScreen.kt
│   │   │       └── components/
│   │   │           └── CalendarHeatmap.kt
│   │   │
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── AppDatabase.kt      # Room database
│   │   │   │   ├── HabitDao.kt
│   │   │   │   ├── JourneyDao.kt
│   │   │   │   └── Converters.kt
│   │   │   ├── model/
│   │   │   │   ├── Habit.kt            # @Entity
│   │   │   │   ├── Journey.kt
│   │   │   │   ├── MoodEntry.kt
│   │   │   │   └── enums/
│   │   │   │       ├── HabitType.kt
│   │   │   │       └── TriggerType.kt
│   │   │   └── repository/
│   │   │       └── HabitRepository.kt
│   │   │
│   │   ├── service/
│   │   │   ├── NotificationService.kt
│   │   │   └── WidgetUpdateService.kt
│   │   │
│   │   └── widget/
│   │       ├── OneFocusWidget.kt       # GlanceAppWidget
│   │       └── WidgetContent.kt
│   │
│   └── res/
│       ├── values/
│       │   ├── colors.xml
│       │   ├── strings.xml
│       │   └── themes.xml
│       └── drawable/
│
└── build.gradle.kts
```

---

## Data Models

### Habit Types (SIMPLIFIED to 3)

Based on PM/Design review, consolidate 5 types to 3 for better UX:

```swift
// iOS (Swift)
enum HabitType: String, Codable, CaseIterable {
    case yesNo      // ✓ Yes/No - "Did you do it?"
    case duration   // ⏱ Duration - "For how long?"
    case count      // 🔢 Count - "How many times?"
}

// Count direction (for count type only)
enum CountDirection: String, Codable {
    case buildUp    // 5 → 50 pushups
    case cutDown    // 10 → 0 cigarettes
    case daily      // 8 glasses of water (repeating)
}

// Trigger types - determines how the habit is cued
enum TriggerType: String, Codable {
    case anchor      // "After I ___" - required for non-repeating habits
    case throughout  // No specific trigger - default for repeating habits
    case context     // Context cues like "with meals", "during work"
}
```

```kotlin
// Android (Kotlin)
enum class HabitType {
    BINARY,     // ✓ Yes/No
    TIMED,      // ⏱ Duration-based
    INCREMENT,  // 📈 Build up
    REDUCTION,  // 📉 Reduce
    REPEATING   // 🔄 Multi-daily
}

enum class TriggerType {
    ANCHOR,      // "After I ___"
    THROUGHOUT,  // No specific trigger
    CONTEXT      // Context cues
}
```

### Core Models

#### iOS (SwiftData)
```swift
import SwiftData

@Model
final class Habit {
    var id: UUID
    var name: String
    var type: HabitType
    var trigger: String
    var triggerType: TriggerType
    var startValue: Int?
    var targetValue: Int?
    var currentValue: Int?
    var dailyTarget: Int?
    var timedMinutes: Int?
    var createdAt: Date

    init(
        name: String,
        type: HabitType,
        trigger: String,
        triggerType: TriggerType = .anchor
    ) {
        self.id = UUID()
        self.name = name
        self.type = type
        self.trigger = trigger
        self.triggerType = triggerType
        self.createdAt = Date()
    }
}

@Model
final class Journey {
    var id: UUID
    var startDate: Date
    var completedDays: [String]  // ISO date strings
    var graceDaysUsed: Int
    var totalFocusTimeMinutes: Int

    var currentDay: Int {
        Calendar.current.dateComponents([.day], from: startDate, to: Date()).day! + 1
    }
}

@Model
final class MoodEntry {
    var id: UUID
    var timestamp: Date
    var mood: Int  // 1-5
    var habitId: UUID?
}
```

#### Android (Room)
```kotlin
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: HabitType,
    val trigger: String,
    val triggerType: TriggerType = TriggerType.ANCHOR,
    val startValue: Int? = null,
    val targetValue: Int? = null,
    val currentValue: Int? = null,
    val dailyTarget: Int? = null,
    val timedMinutes: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "journey")
data class Journey(
    @PrimaryKey val id: String = "main_journey",
    val startDate: Long,
    val completedDays: List<String> = emptyList(),
    val graceDaysUsed: Int = 0,
    val totalFocusTimeMinutes: Int = 0
) {
    val currentDay: Int
        get() = ((System.currentTimeMillis() - startDate) / 86400000).toInt() + 1
}

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val mood: Int,  // 1-5
    val habitId: String? = null
)
```

---

## Key Features

### 1. Onboarding Flow
- Welcome with science cards (66-day stat, trigger power, focus benefit)
- Habit type selection (2x2 grid + full-width repeating card)
- Habit name input with suggestions
- Trigger setup:
  - **Non-repeating habits**: Required "After I ___, I will ___" intention builder
  - **Repeating habits**: Optional trigger with 3 choices:
    - "Throughout the day" (recommended, no specific trigger)
    - "At specific moments" (context cues like "with meals", "during work")
    - "After a specific action" (traditional anchor trigger)
- Ready screen with journey visualization

### 2. Home Screen
- Journey progress card (Day X/66, streak, grace days remaining)
- Habit card(s) with type-specific UI
- Mood tracker (5-point scale)
- Second habit unlock prompt (at day 21)

### 3. Focus Mode
- 4-7-8 breathing animation
- Type-specific completion UI
- Celebration animation
- Mood check after completion

### 4. Second Habit System
- Unlocks at day 21
- Independent trigger (NOT chained)
- Conflict detection warning

---

## Design Tokens

### iOS (SwiftUI)
```swift
extension Color {
    static let accent = Color(hex: "BF5AF2")
    static let accentSecondary = Color(hex: "5E5CE6")
    static let accentDim = Color(hex: "BF5AF2").opacity(0.15)

    static let bgPrimary = Color.black
    static let bgSecondary = Color(hex: "1C1C1E")
    static let bgTertiary = Color(hex: "2C2C2E")

    static let success = Color(hex: "30D158")
    static let warning = Color(hex: "FFD60A")
    static let error = Color(hex: "FF453A")
}

extension Font {
    static let largeTitle = Font.system(size: 34, weight: .bold)
    static let title1 = Font.system(size: 28, weight: .semibold)
    static let title2 = Font.system(size: 22, weight: .semibold)
    static let body = Font.system(size: 17, weight: .regular)
    static let footnote = Font.system(size: 13, weight: .regular)
}
```

### Android (Compose)
```kotlin
val Purple80 = Color(0xFFD0BCFF)
val Purple40 = Color(0xFF4F378B)
val PurpleGrey80 = Color(0xFFCCC2DC)

val Surface = Color(0xFF141218)
val SurfaceContainer = Color(0xFF211F26)
val SurfaceContainerHigh = Color(0xFF2B2930)

val Green = Color(0xFFA8DAB5)
val Yellow = Color(0xFFFFE082)
val Red = Color(0xFFF2B8B5)

val Typography = Typography(
    displayLarge = TextStyle(fontSize = 45.sp, fontWeight = FontWeight.Normal),
    headlineLarge = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal),
    titleLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    labelMedium = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
)
```

---

## Animation Specs

### Breathing Circle
```swift
// iOS
struct BreathingCircle: View {
    @State private var scale: CGFloat = 1.0
    @State private var phase: BreathPhase = .inhale

    let inhaleSeconds = 4.0
    let holdSeconds = 7.0
    let exhaleSeconds = 8.0

    var body: some View {
        Circle()
            .fill(Color.accent.gradient)
            .frame(width: 200, height: 200)
            .scaleEffect(scale)
            .shadow(color: .accent.opacity(phase == .hold ? 0.6 : 0.3),
                    radius: phase == .hold ? 40 : 20)
            .onAppear { startBreathingCycle() }
    }
}
```

```kotlin
// Android
@Composable
fun BreathingCircle(onComplete: () -> Unit) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Inhale: 1.0 → 1.4 over 4 seconds
        scale.animateTo(1.4f, animationSpec = tween(4000))
        // Hold: 7 seconds
        delay(7000)
        // Exhale: 1.4 → 1.0 over 8 seconds
        scale.animateTo(1.0f, animationSpec = tween(8000))
        onComplete()
    }

    Box(
        modifier = Modifier
            .size(200.dp)
            .scale(scale.value)
            .background(Purple80, CircleShape)
    )
}
```

---

## Testing Strategy

### iOS (XCTest + Swift Testing)
```swift
// Unit Tests
@Test func streakCalculation() {
    let journey = Journey(startDate: Date().addingDays(-5))
    journey.completedDays = ["2024-01-24", "2024-01-25", "2024-01-26"]
    #expect(journey.currentStreak == 3)
}

// UI Tests
@MainActor
func testOnboardingFlow() async {
    let app = XCUIApplication()
    app.launch()

    app.buttons["Begin Your Journey"].tap()
    app.buttons["Binary"].tap()
    // ...
}
```

### Android (JUnit + Compose Testing)
```kotlin
// Unit Tests
@Test
fun `streak calculation returns correct count`() {
    val journey = Journey(
        startDate = System.currentTimeMillis() - 5.days,
        completedDays = listOf("2024-01-24", "2024-01-25", "2024-01-26")
    )
    assertEquals(3, journey.currentStreak)
}

// UI Tests
@Test
fun onboardingFlow_completesSuccessfully() {
    composeTestRule.setContent { OneFocusApp() }

    composeTestRule.onNodeWithText("Begin Your Journey").performClick()
    composeTestRule.onNodeWithText("Binary").performClick()
    // ...
}
```

---

## Commands Reference

### iOS
```bash
# Open in Xcode
open OneFocus.xcodeproj

# Build from command line
xcodebuild -scheme OneFocus -configuration Debug build

# Run tests
xcodebuild test -scheme OneFocus -destination 'platform=iOS Simulator,name=iPhone 15 Pro'

# Archive for release
xcodebuild archive -scheme OneFocus -archivePath build/OneFocus.xcarchive
```

### Android
```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Build release bundle
./gradlew bundleRelease

# Install on device
./gradlew installDebug
```

---

## Current Status

**Prototype Phase Complete:**
- ✅ iOS HTML prototype (v5) with optional triggers for repeating habits
- ✅ Android HTML prototype (v5) with optional triggers for repeating habits
- ✅ Widget mockups for both platforms

**Documentation Complete:**
- ✅ Product decisions (UX, pricing, business model)
- ✅ iOS architecture specification
- ✅ Android architecture specification
- ✅ Accessibility requirements
- ✅ Localization guide
- ✅ Focus Mode (app blocking) specification

**Native Development Phase:**
- [ ] iOS: Xcode project setup
- [ ] iOS: SwiftData models (use simplified 3-type system)
- [ ] iOS: 5-screen onboarding flow
- [ ] iOS: Home screen with habit cards
- [ ] iOS: Focus mode with breathing animation (respect Reduce Motion)
- [ ] iOS: WidgetKit (small + medium)
- [ ] iOS: Push notifications with trigger-time awareness
- [ ] iOS: Settings page
- [ ] iOS: Payment page (show after first journey)
- [ ] Android: Project setup with Compose
- [ ] Android: Room database (use simplified 3-type system)
- [ ] Android: 5-screen onboarding flow
- [ ] Android: Home screen with habit cards
- [ ] Android: Focus mode with breathing animation
- [ ] Android: Glance widget (small + medium)
- [ ] Android: Notifications with WorkManager
- [ ] Android: Settings page
- [ ] Android: Payment page

---

## Important Notes for Claude Code

1. **Separate codebases** - iOS and Android are completely independent projects. Don't try to share code.

2. **Platform conventions matter** - Use native patterns:
   - iOS: SF Symbols, haptics via UIImpactFeedbackGenerator, SwiftUI animations
   - Android: Material Icons, HapticFeedback, Compose animations

3. **Dark mode is default** - Design for dark first, light mode is secondary

4. **Haptic feedback** on all significant interactions

5. **Offline-first** - App must work without network, all data local

6. **No accounts/auth** - Purely local app, no backend needed

7. **Respect the constraint** - Never allow more than 2 habits

8. **Triggers are type-dependent** - Non-repeating habits REQUIRE an anchor trigger. Count (daily) habits have OPTIONAL triggers with 3 modes.

9. **Widget data sync** - Use App Groups (iOS) or ContentProvider (Android) to share data with widgets

10. **Accessibility REQUIRED** - See docs/ACCESSIBILITY.md
    - Support VoiceOver/TalkBack
    - Dynamic Type/Font scaling
    - Reduce Motion support for breathing animation
    - Color never as sole indicator
    - 44pt/48dp minimum touch targets

11. **Localization ready** - See docs/LOCALIZATION.md
    - Use String Catalogs (iOS) / strings.xml (Android)
    - Never concatenate translated strings
    - Use system formatters for dates/numbers
    - Plan for RTL languages

12. **Simplified habit types** - Use 3 types, not 5:
    - Yes/No (binary)
    - Duration (timed)
    - Count (covers increment, reduction, repeating)

13. **Terminology** - Use "flex days" not "grace days"

14. **Onboarding** - 5 screens max, don't overwhelm with 66-day info upfront

15. **Payment** - Show AFTER first 66-day journey completes (free to start)
