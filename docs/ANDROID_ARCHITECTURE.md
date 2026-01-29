# OneFocus Android Architecture

**Platform:** Android 8.0+ (API 26)
**Language:** Kotlin 1.9+
**UI Framework:** Jetpack Compose
**Storage:** Room + DataStore
**Architecture:** MVVM with StateFlow
**DI:** Hilt

---

## Project Setup

### build.gradle.kts (Project)
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
```

### build.gradle.kts (App)
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.onefocus.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.onefocus.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // DataStore
    implementation(libs.datastore.preferences)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Glance (Widgets)
    implementation(libs.glance.appwidget)
    implementation(libs.glance.material3)

    // WorkManager
    implementation(libs.work.runtime.ktx)
}
```

### libs.versions.toml
```toml
[versions]
kotlin = "1.9.22"
compose-bom = "2024.01.00"
room = "2.6.1"
hilt = "2.50"
glance = "1.0.0"

[libraries]
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
activity-compose = { group = "androidx.activity", name = "activity-compose", version = "1.8.2" }
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version = "2.7.6" }
lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version = "2.7.0" }
lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version = "2.7.0" }

room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version = "1.0.0" }

hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.1.0" }

glance-appwidget = { group = "androidx.glance", name = "glance-appwidget", version.ref = "glance" }
glance-material3 = { group = "androidx.glance", name = "glance-material3", version.ref = "glance" }

work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version = "2.9.0" }

[plugins]
android-application = { id = "com.android.application", version = "8.2.2" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version = "1.9.22-1.0.17" }
```

---

## File Structure

```
app/src/main/
├── java/com/onefocus/app/
│   ├── OneFocusApp.kt
│   ├── MainActivity.kt
│   │
│   ├── core/
│   │   ├── design/
│   │   │   ├── Color.kt
│   │   │   ├── Type.kt
│   │   │   ├── Theme.kt
│   │   │   └── Shape.kt
│   │   ├── navigation/
│   │   │   ├── NavGraph.kt
│   │   │   └── Destinations.kt
│   │   └── util/
│   │       ├── DateHelper.kt
│   │       └── HapticManager.kt
│   │
│   ├── data/
│   │   ├── local/
│   │   │   ├── AppDatabase.kt
│   │   │   ├── dao/
│   │   │   │   ├── HabitDao.kt
│   │   │   │   ├── JourneyDao.kt
│   │   │   │   ├── MoodDao.kt
│   │   │   │   └── RepeatingLogDao.kt
│   │   │   └── Converters.kt
│   │   ├── model/
│   │   │   ├── Habit.kt
│   │   │   ├── Journey.kt
│   │   │   ├── MoodEntry.kt
│   │   │   ├── RepeatingLog.kt
│   │   │   └── enums/
│   │   │       ├── HabitType.kt
│   │   │       └── TriggerType.kt
│   │   └── repository/
│   │       ├── HabitRepository.kt
│   │       └── JourneyRepository.kt
│   │
│   ├── di/
│   │   ├── AppModule.kt
│   │   └── DatabaseModule.kt
│   │
│   ├── feature/
│   │   ├── onboarding/
│   │   │   ├── OnboardingScreen.kt
│   │   │   ├── HabitTypeScreen.kt
│   │   │   ├── HabitNameScreen.kt
│   │   │   ├── TriggerSetupScreen.kt
│   │   │   ├── ReadyScreen.kt
│   │   │   ├── OnboardingViewModel.kt
│   │   │   └── components/
│   │   │       ├── ScienceCard.kt
│   │   │       ├── HabitTypeCard.kt
│   │   │       ├── IntentionBuilder.kt
│   │   │       └── TriggerTypeOption.kt
│   │   │
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   ├── HomeViewModel.kt
│   │   │   └── components/
│   │   │       ├── JourneyCard.kt
│   │   │       ├── HabitCard.kt
│   │   │       ├── MoodTracker.kt
│   │   │       └── UnlockCard.kt
│   │   │
│   │   ├── focus/
│   │   │   ├── FocusScreen.kt
│   │   │   ├── FocusViewModel.kt
│   │   │   └── components/
│   │   │       ├── BreathingCircle.kt
│   │   │       ├── FocusTimer.kt
│   │   │       └── CelebrationAnimation.kt
│   │   │
│   │   ├── analytics/
│   │   │   ├── AnalyticsScreen.kt
│   │   │   └── components/
│   │   │       ├── CalendarHeatmap.kt
│   │   │       ├── MoodChart.kt
│   │   │       └── StatsCard.kt
│   │   │
│   │   └── secondhabit/
│   │       ├── AddSecondHabitScreen.kt
│   │       └── SecondHabitViewModel.kt
│   │
│   ├── service/
│   │   ├── NotificationService.kt
│   │   └── WidgetUpdateWorker.kt
│   │
│   └── widget/
│       ├── OneFocusWidget.kt
│       └── WidgetContent.kt
│
└── res/
    ├── values/
    │   ├── colors.xml
    │   ├── strings.xml
    │   └── themes.xml
    ├── drawable/
    └── xml/
        └── onefocus_widget_info.xml
```

---

## Data Models

### enums/HabitType.kt
```kotlin
package com.onefocus.app.data.model.enums

enum class HabitType {
    BINARY,     // ✓ Yes/No
    TIMED,      // ⏱ Duration-based
    INCREMENT,  // 📈 Build up
    REDUCTION,  // 📉 Reduce
    REPEATING;  // 🔄 Multi-daily

    val icon: String
        get() = when (this) {
            BINARY -> "✓"
            TIMED -> "⏱"
            INCREMENT -> "📈"
            REDUCTION -> "📉"
            REPEATING -> "🔄"
        }

    val label: String
        get() = when (this) {
            BINARY -> "Daily"
            TIMED -> "Timed"
            INCREMENT -> "Build Up"
            REDUCTION -> "Cut Down"
            REPEATING -> "Repeating"
        }

    val description: String
        get() = when (this) {
            BINARY -> "Yes or no each day"
            TIMED -> "Duration-based habit"
            INCREMENT -> "Increase over time"
            REDUCTION -> "Reduce a behavior"
            REPEATING -> "Multiple times daily"
        }

    val requiresTrigger: Boolean
        get() = this != REPEATING
}
```

### enums/TriggerType.kt
```kotlin
package com.onefocus.app.data.model.enums

enum class TriggerType {
    ANCHOR,      // "After I ___"
    THROUGHOUT,  // No specific trigger
    CONTEXT;     // Context cues

    val label: String
        get() = when (this) {
            ANCHOR -> "After a specific action"
            THROUGHOUT -> "Throughout the day"
            CONTEXT -> "At specific moments"
        }

    val description: String
        get() = when (this) {
            ANCHOR -> "Use the traditional \"After I ___\" format"
            THROUGHOUT -> "No specific trigger - log whenever you do it"
            CONTEXT -> "Set context cues like \"with meals\" or \"during work\""
        }

    val isRecommendedForRepeating: Boolean
        get() = this == THROUGHOUT
}
```

### Habit.kt
```kotlin
package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onefocus.app.data.model.enums.HabitType
import com.onefocus.app.data.model.enums.TriggerType
import java.util.UUID

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
    val createdAt: Long = System.currentTimeMillis(),
    val isSecondary: Boolean = false
) {
    val triggerDisplayText: String
        get() = when (triggerType) {
            TriggerType.THROUGHOUT -> "Throughout the day"
            TriggerType.CONTEXT -> trigger
            TriggerType.ANCHOR -> "After I $trigger"
        }
}
```

### Journey.kt
```kotlin
package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity(tableName = "journey")
data class Journey(
    @PrimaryKey val id: String = "main_journey",
    val startDate: Long = System.currentTimeMillis(),
    val completedDays: List<String> = emptyList(),
    val graceDaysUsed: Int = 0,
    val totalFocusTimeSeconds: Int = 0
) {
    val currentDay: Int
        get() {
            val start = LocalDate.ofEpochDay(startDate / 86400000)
            val today = LocalDate.now()
            return minOf(maxOf(ChronoUnit.DAYS.between(start, today).toInt() + 1, 1), 66)
        }

    val currentPhase: JourneyPhase
        get() = when {
            currentDay <= 21 -> JourneyPhase.FOUNDATION
            currentDay <= 42 -> JourneyPhase.STRENGTHENING
            else -> JourneyPhase.CEMENTING
        }

    val currentStreak: Int
        get() {
            var streak = 0
            var checkDate = LocalDate.now()
            while (streak < currentDay) {
                val dateStr = checkDate.toString()
                if (completedDays.any { it.startsWith(dateStr) }) {
                    streak++
                    checkDate = checkDate.minusDays(1)
                } else {
                    break
                }
            }
            return streak
        }

    val graceDaysRemaining: Int
        get() = 3 - graceDaysUsed

    val progress: Float
        get() = currentDay / 66f

    fun isCompletedToday(habitIndex: Int = 0): Boolean {
        val todayKey = todayKey(habitIndex)
        return completedDays.contains(todayKey)
    }

    private fun todayKey(habitIndex: Int): String {
        val today = LocalDate.now().toString()
        return if (habitIndex == 0) today else "${today}_2"
    }
}

enum class JourneyPhase(val label: String) {
    FOUNDATION("Building Foundation"),
    STRENGTHENING("Strengthening"),
    CEMENTING("Cementing Habit")
}
```

### MoodEntry.kt
```kotlin
package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val mood: Int,  // 1-5
    val habitId: String? = null,
    val isBefore: Boolean = true
)
```

### RepeatingLog.kt
```kotlin
package com.onefocus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "repeating_logs")
data class RepeatingLog(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val dateKey: String,  // "2024-01-26_0"
    val count: Int = 1
)
```

---

## Database

### AppDatabase.kt
```kotlin
package com.onefocus.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.onefocus.app.data.local.dao.*
import com.onefocus.app.data.model.*

@Database(
    entities = [
        Habit::class,
        Journey::class,
        MoodEntry::class,
        RepeatingLog::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun journeyDao(): JourneyDao
    abstract fun moodDao(): MoodDao
    abstract fun repeatingLogDao(): RepeatingLogDao
}
```

### Converters.kt
```kotlin
package com.onefocus.app.data.local

import androidx.room.TypeConverter
import com.onefocus.app.data.model.enums.HabitType
import com.onefocus.app.data.model.enums.TriggerType

class Converters {
    @TypeConverter
    fun fromHabitType(value: HabitType): String = value.name

    @TypeConverter
    fun toHabitType(value: String): HabitType = HabitType.valueOf(value)

    @TypeConverter
    fun fromTriggerType(value: TriggerType): String = value.name

    @TypeConverter
    fun toTriggerType(value: String): TriggerType = TriggerType.valueOf(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isEmpty()) emptyList() else value.split(",")
}
```

### dao/HabitDao.kt
```kotlin
package com.onefocus.app.data.local.dao

import androidx.room.*
import com.onefocus.app.data.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isSecondary = 0 LIMIT 1")
    fun getPrimaryHabit(): Flow<Habit?>

    @Query("SELECT * FROM habits WHERE isSecondary = 1 LIMIT 1")
    fun getSecondaryHabit(): Flow<Habit?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)
}
```

### dao/JourneyDao.kt
```kotlin
package com.onefocus.app.data.local.dao

import androidx.room.*
import com.onefocus.app.data.model.Journey
import kotlinx.coroutines.flow.Flow

@Dao
interface JourneyDao {
    @Query("SELECT * FROM journey LIMIT 1")
    fun getJourney(): Flow<Journey?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(journey: Journey)

    @Update
    suspend fun update(journey: Journey)

    @Query("UPDATE journey SET completedDays = :completedDays WHERE id = 'main_journey'")
    suspend fun updateCompletedDays(completedDays: List<String>)
}
```

---

## Design System

### Color.kt
```kotlin
package com.onefocus.app.core.design

import androidx.compose.ui.graphics.Color

// Primary
val Purple80 = Color(0xFFD0BCFF)
val Purple40 = Color(0xFF4F378B)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Indigo = Color(0xFF5E5CE6)

// Surfaces
val Surface = Color(0xFF141218)
val SurfaceContainer = Color(0xFF211F26)
val SurfaceContainerHigh = Color(0xFF2B2930)
val SurfaceContainerHighest = Color(0xFF36343B)

// On Surface
val OnSurface = Color(0xFFE6E0E9)
val OnSurfaceVariant = Color(0xFFCAC4D0)
val Outline = Color(0xFF938F99)
val OutlineVariant = Color(0xFF49454F)

// Semantic
val Green = Color(0xFFA8DAB5)
val GreenContainer = Color(0xFF1B3A2D)
val Yellow = Color(0xFFFFE082)
val Red = Color(0xFFF2B8B5)
val Cyan = Color(0xFFA8E6CF)
val Blue = Color(0xFF8ECAFF)

// Mood colors
val Mood1 = Color(0xFFF2B8B5)
val Mood2 = Color(0xFFFFE082)
val Mood3 = Color(0xFFFFD54F)
val Mood4 = Color(0xFFA8DAB5)
val Mood5 = Color(0xFF8ECAFF)
```

### Type.kt
```kotlin
package com.onefocus.app.core.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontSize = 45.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```

### Theme.kt
```kotlin
package com.onefocus.app.core.design

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple40,
    primaryContainer = Purple40,
    onPrimaryContainer = Purple80,
    secondary = PurpleGrey80,
    tertiary = Cyan,
    background = Surface,
    surface = Surface,
    surfaceVariant = SurfaceContainer,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant
)

@Composable
fun OneFocusTheme(
    darkTheme: Boolean = true, // Default to dark
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

---

## Key Components

### BreathingCircle.kt
```kotlin
package com.onefocus.app.feature.focus.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.Indigo
import com.onefocus.app.core.design.Purple80
import kotlinx.coroutines.delay

enum class BreathPhase { INHALE, HOLD, EXHALE }

@Composable
fun BreathingCircle(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var phase by remember { mutableStateOf(BreathPhase.INHALE) }
    val scale = remember { Animatable(1f) }

    val phaseText = when (phase) {
        BreathPhase.INHALE -> "Breathe in..."
        BreathPhase.HOLD -> "Hold..."
        BreathPhase.EXHALE -> "Breathe out..."
    }

    LaunchedEffect(Unit) {
        // Inhale: 4 seconds
        phase = BreathPhase.INHALE
        scale.animateTo(
            targetValue = 1.4f,
            animationSpec = tween(durationMillis = 4000, easing = EaseInOut)
        )

        // Hold: 7 seconds
        phase = BreathPhase.HOLD
        delay(7000)

        // Exhale: 8 seconds
        phase = BreathPhase.EXHALE
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 8000, easing = EaseInOut)
        )

        onComplete()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(scale.value)
                .shadow(
                    elevation = if (phase == BreathPhase.HOLD) 40.dp else 20.dp,
                    shape = CircleShape,
                    ambientColor = Purple80.copy(alpha = 0.5f),
                    spotColor = Purple80.copy(alpha = 0.5f)
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Purple80, Indigo)
                    ),
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = phaseText,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

### HabitCard.kt
```kotlin
package com.onefocus.app.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.onefocus.app.core.design.*
import com.onefocus.app.data.model.Habit
import com.onefocus.app.data.model.enums.HabitType

@Composable
fun HabitCard(
    habit: Habit,
    isCompletedToday: Boolean,
    repeatingCount: Int = 0,
    onTap: () -> Unit,
    onQuickLog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainer
        )
    ) {
        Column {
            // Accent bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        if (isCompletedToday) Green
                        else Brush.horizontalGradient(listOf(Purple80, Indigo))
                    )
            )

            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Purple80.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = habit.type.icon,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = habit.type.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = Purple80
                            )
                        }
                    }

                    Text(
                        text = if (habit.isSecondary) "2nd habit" else "1st habit",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name & Trigger
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = habit.triggerDisplayText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Type-specific UI
                if (habit.type == HabitType.REPEATING) {
                    RepeatingUI(
                        count = repeatingCount,
                        target = habit.dailyTarget ?: 8,
                        habitName = habit.name,
                        onQuickLog = onQuickLog
                    )
                } else {
                    ActionButton(
                        text = if (isCompletedToday) "Completed today ✓" else "Start ${habit.name}",
                        isCompleted = isCompletedToday,
                        onClick = onTap
                    )
                }
            }
        }
    }
}

@Composable
private fun RepeatingUI(
    count: Int,
    target: Int,
    habitName: String,
    onQuickLog: () -> Unit
) {
    val progress = (count.toFloat() / target).coerceIn(0f, 1f)
    val isComplete = count >= target

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Baseline
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Purple80
            )
            Text(
                text = " / $target times",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Purple80,
            trackColor = SurfaceContainerHigh
        )

        Button(
            onClick = onQuickLog,
            enabled = !isComplete,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isComplete) Green.copy(alpha = 0.15f) else Purple80,
                contentColor = if (isComplete) Green else SurfaceContainer,
                disabledContainerColor = Green.copy(alpha = 0.15f),
                disabledContentColor = Green
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (isComplete) "✓ Target Reached" else "+ Log $habitName",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isCompleted,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isCompleted) Green.copy(alpha = 0.15f) else Purple80,
            contentColor = if (isCompleted) Green else SurfaceContainer,
            disabledContainerColor = Green.copy(alpha = 0.15f),
            disabledContentColor = Green
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
```

---

## Dependency Injection

### AppModule.kt
```kotlin
package com.onefocus.app.di

import android.content.Context
import androidx.room.Room
import com.onefocus.app.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "onefocus_database"
        ).build()
    }

    @Provides
    fun provideHabitDao(database: AppDatabase) = database.habitDao()

    @Provides
    fun provideJourneyDao(database: AppDatabase) = database.journeyDao()

    @Provides
    fun provideMoodDao(database: AppDatabase) = database.moodDao()

    @Provides
    fun provideRepeatingLogDao(database: AppDatabase) = database.repeatingLogDao()
}
```

---

## Widget

### OneFocusWidget.kt
```kotlin
package com.onefocus.app.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.appwidget.*
import androidx.glance.layout.*
import androidx.glance.text.*
import com.onefocus.app.core.design.Green
import com.onefocus.app.core.design.Purple80
import com.onefocus.app.core.design.SurfaceContainer

class OneFocusWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Fetch data from database
        val habitName = "Meditate"  // TODO: Fetch from Room
        val currentDay = 12
        val streak = 12
        val isCompletedToday = false

        provideContent {
            OneFocusWidgetContent(
                habitName = habitName,
                currentDay = currentDay,
                streak = streak,
                isCompletedToday = isCompletedToday
            )
        }
    }
}

@Composable
private fun OneFocusWidgetContent(
    habitName: String,
    currentDay: Int,
    streak: Int,
    isCompletedToday: Boolean
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(SurfaceContainer))
            .padding(16.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Day $currentDay",
                style = TextStyle(
                    color = ColorProvider(Purple80),
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(GlanceModifier.defaultWeight())
            if (isCompletedToday) {
                Text(
                    text = "✓",
                    style = TextStyle(color = ColorProvider(Green))
                )
            }
        }

        Spacer(GlanceModifier.height(8.dp))

        Text(
            text = habitName,
            style = TextStyle(
                color = ColorProvider(androidx.compose.ui.graphics.Color.White),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(GlanceModifier.defaultWeight())

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "🔥",
                style = TextStyle(fontSize = 14.sp)
            )
            Spacer(GlanceModifier.width(4.dp))
            Text(
                text = "$streak day streak",
                style = TextStyle(
                    color = ColorProvider(androidx.compose.ui.graphics.Color.Gray),
                    fontSize = 12.sp
                )
            )
        }
    }
}

class OneFocusWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = OneFocusWidget()
}
```

### res/xml/onefocus_widget_info.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:minWidth="110dp"
    android:minHeight="40dp"
    android:targetCellWidth="2"
    android:targetCellHeight="2"
    android:updatePeriodMillis="1800000"
    android:initialLayout="@layout/widget_loading"
    android:resizeMode="horizontal|vertical"
    android:widgetCategory="home_screen"
    android:description="@string/widget_description" />
```

---

## Testing

### HabitTest.kt
```kotlin
package com.onefocus.app.data.model

import com.onefocus.app.data.model.enums.HabitType
import com.onefocus.app.data.model.enums.TriggerType
import org.junit.Assert.*
import org.junit.Test

class HabitTest {

    @Test
    fun `trigger display text for anchor type`() {
        val habit = Habit(
            name = "Meditate",
            type = HabitType.BINARY,
            trigger = "pour my coffee",
            triggerType = TriggerType.ANCHOR
        )
        assertEquals("After I pour my coffee", habit.triggerDisplayText)
    }

    @Test
    fun `trigger display text for throughout type`() {
        val habit = Habit(
            name = "Drink water",
            type = HabitType.REPEATING,
            trigger = "",
            triggerType = TriggerType.THROUGHOUT
        )
        assertEquals("Throughout the day", habit.triggerDisplayText)
    }

    @Test
    fun `trigger display text for context type`() {
        val habit = Habit(
            name = "Drink water",
            type = HabitType.REPEATING,
            trigger = "With meals, During work",
            triggerType = TriggerType.CONTEXT
        )
        assertEquals("With meals, During work", habit.triggerDisplayText)
    }
}

class JourneyTest {

    @Test
    fun `grace days remaining calculation`() {
        val journey = Journey(graceDaysUsed = 1)
        assertEquals(2, journey.graceDaysRemaining)
    }

    @Test
    fun `progress calculation`() {
        val journey = Journey().copy(
            startDate = System.currentTimeMillis() - (32 * 86400000L) // 32 days ago
        )
        // currentDay would be 33
        assertTrue(journey.progress > 0.4f && journey.progress < 0.6f)
    }
}
```

---

## Release Checklist

- [ ] App icon (adaptive icon with foreground/background)
- [ ] Splash screen
- [ ] Privacy policy URL
- [ ] Play Store screenshots
- [ ] Play Store description
- [ ] Internal testing track
- [ ] Accessibility (TalkBack, font scaling)
- [ ] Dark/Light mode testing
- [ ] Widget testing on different launchers
- [ ] Notification permissions (Android 13+)
- [ ] ProGuard/R8 rules
- [ ] App signing
- [ ] Target API 34 compliance
