# OneFocus Android

**One habit. 66 days. Life changed.**

A minimalist habit-building app based on radical constraints philosophy. Build ONE habit at a time using behavioral science principles.

## Phase 1: Complete ✅

Foundation and onboarding flow fully implemented.

### What's Built
- Complete onboarding flow (6 screens)
- 5 habit types (Binary, Timed, Increment, Reduction, Repeating)
- Trigger-based system (required for 4 types, optional for repeating)
- Room database with persistence
- Material 3 dark theme
- Hilt dependency injection
- Type-safe navigation

### Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM with StateFlow
- **Database**: Room + DataStore
- **DI**: Hilt
- **Min SDK**: API 26 (Android 8.0)

## Project Structure

```
app/src/main/java/com/onefocus/app/
├── OneFocusApp.kt              # Application class
├── MainActivity.kt             # Entry point
├── core/
│   ├── design/                 # Material 3 theme
│   ├── navigation/             # Navigation graph
│   └── util/                   # Haptic manager
├── data/
│   ├── local/                  # Room database + DAOs
│   ├── model/                  # Entities + enums
│   └── repository/             # Data layer
├── di/                         # Hilt modules
└── feature/
    └── onboarding/             # 6-screen onboarding
```

## Build Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Commands
```bash
# Initialize Gradle wrapper (if needed)
gradle wrapper

# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Run tests
./gradlew test
```

## Features

### Onboarding Flow
1. **Welcome** - Science-backed introduction
2. **Habit Type** - Choose from 5 types
3. **Habit Name** - Name your habit
4. **Trigger Setup** - Set "After I ___" intention (required for 4 types)
5. **Trigger Repeating** - Optional trigger selection (repeating habits only)
6. **Ready** - Journey summary with 66 days + 3 flex days

### Habit Types
- **Daily** (✓) - Yes/no each day
- **Timed** (⏱) - Duration-based
- **Build Up** (📈) - Increase over time
- **Cut Down** (📉) - Reduce behavior
- **Repeating** (🔄) - Multiple times daily

### Trigger Types
- **Anchor** - "After I ___" (required for most types)
- **Throughout** - No specific trigger (default for repeating)
- **Context** - Specific moments/cues

## What's Next (Phase 2)

- Home screen with journey progress
- Focus mode with 4-7-8 breathing animation
- Day completion logic
- Home screen widgets (Glance)
- Daily notifications (WorkManager)

## Key Decisions

- Dark theme by default (Material 3)
- Optional triggers for repeating habits (UX improvement)
- "Flex days" instead of "grace days"
- Hilt for DI (industry standard)
- StateFlow over LiveData (modern Kotlin)

## Accessibility

- 48dp minimum touch targets
- WCAG AA color contrast
- Material 3 text scaling support
- Haptic feedback for interactions
- TalkBack-ready (semantic descriptions)

## Documentation

See `BUILD_SUMMARY.md` for complete build details and `docs/` for architecture specs.

## License

Proprietary - OneFocus App

---

**Built with Kotlin + Compose + Material 3**
