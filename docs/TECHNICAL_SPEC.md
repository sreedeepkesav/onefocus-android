# OneFocus Technical Specification

**Version:** 1.0
**Last Updated:** January 2026
**Status:** Ready for Development

---

## 1. Executive Summary

OneFocus is a habit-building mobile application that uses deliberate constraints to maximize user success. Unlike typical habit trackers that encourage tracking dozens of habits, OneFocus limits users to ONE habit at a time (with a second unlocking after 21 days of consistency).

### Key Differentiators
- **Radical constraint**: 1-2 habits maximum
- **Trigger-based**: Habits anchored to existing behaviors
- **66-day science**: Based on habit formation research
- **Grace system**: 3 allowed misses prevent all-or-nothing failure
- **Mindful completion**: Breathing exercises before marking complete

---

## 2. Feature Specifications

### 2.1 Habit Types

| Type | Icon | Description | Completion Criteria | UI Elements |
|------|------|-------------|---------------------|-------------|
| Binary | ✓ | Yes/No habits | Single completion | Check button |
| Timed | ⏱ | Duration-based | Timer reaches target | Timer display, Start/Pause |
| Increment | 📈 | Build up over time | Reach target value | Current value, +/- buttons |
| Reduction | 📉 | Reduce over time | Reach target (lower) | Current value, +/- buttons |
| Repeating | 🔄 | Multiple times daily | Hit daily target count | Counter, Quick log button |

### 2.2 Onboarding Flow

```
Screen 1: Welcome
├── App icon with float animation
├── "OneFocus" title with gradient
├── Tagline: "One habit. Total focus. Real change."
├── 3 Science cards:
│   ├── "66 Days" - habit formation timeline
│   ├── "Trigger Power" - 2.5x success with triggers
│   └── "One Focus" - 91% success with single focus
└── "Begin Your Journey" CTA

Screen 2: Habit Type Selection
├── Header: "What type of habit?"
├── 2x2 Grid:
│   ├── Binary (top-left)
│   ├── Timed (top-right)
│   ├── Increment (bottom-left)
│   └── Reduction (bottom-right)
├── Full-width card: Repeating
└── "Continue" CTA (disabled until selection)

Screen 3: Habit Name
├── Header: "Name your habit"
├── Text input field
├── Suggestions based on type:
│   ├── Binary: "Meditate", "Journal", "Make bed"
│   ├── Timed: "Read", "Exercise", "Practice guitar"
│   ├── Increment: "Pushups", "Pages read", "Minutes walked"
│   ├── Reduction: "Cigarettes", "Sodas", "Screen time"
│   └── Repeating: "Glasses of water", "Stretch breaks", "Gratitude moments"
└── "Continue" CTA

Screen 4: Type-Specific Config (conditional)
├── Timed: Duration picker (5-120 min, default 20)
├── Increment: Start value + Target value
├── Reduction: Start value + Target value
├── Repeating: Daily target (1-20, default 8)
└── Binary: Skip this screen

Screen 5: Trigger Setup (VARIES BY HABIT TYPE)

FOR NON-REPEATING HABITS (Binary, Timed, Increment, Reduction):
├── Header: "Anchor your habit"
├── Intention Builder card:
│   ├── "After I" + [trigger input] (REQUIRED)
│   ├── "I will" + [habit name - readonly]
│   └── "for" + [duration/amount - if applicable]
├── Pro tip: "Specific triggers work best"
└── "Start My Journey" CTA

FOR REPEATING HABITS:
├── Header: "When will you [habit name]?"
├── Subtitle: "Repeating habits work differently"
├── Trigger type selection (radio):
│   ├── ○ Throughout the day (RECOMMENDED, DEFAULT)
│   │     "No specific trigger - log whenever you do it"
│   ├── ○ At specific moments
│   │     Shows context cue chips: "With meals", "During work",
│   │     "Morning routine", "When tired", "Between tasks", "Before bed"
│   └── ○ After a specific action
│       Shows intention builder if selected
├── Research insight: "Studies show flexible cues work well for multi-daily habits"
└── "Start My Journey" CTA

Screen 6: Ready
├── Animated journey visualization (66 dots)
├── Day 1 highlighted
├── Milestone markers (21, 40, 66)
├── Encouragement message
└── "Let's Go" CTA → Home
```

### 2.3 Home Screen

```
┌─────────────────────────────────┐
│ Status Bar                      │
├─────────────────────────────────┤
│ Good morning, [Name]            │
│ Sunday, January 26              │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │   JOURNEY CARD              │ │
│ │   Day 12 ─────────── 66     │ │
│ │   ████████░░░░░░░░░░ 18%    │ │
│ │                             │ │
│ │   🔥 12 day streak          │ │
│ │   ❤️ 3 grace days remaining │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │   HABIT CARD 1              │ │
│ │   [Type-specific UI]        │ │
│ │   Trigger: After morning... │ │
│ │                             │ │
│ │   [Complete Button]         │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ (If day >= 21 && !secondHabit)  │
│ ┌─────────────────────────────┐ │
│ │   🎉 UNLOCK SECOND HABIT    │ │
│ │   You've earned it!         │ │
│ │   [Add Second Habit]        │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │   MOOD TRACKER              │ │
│ │   How are you feeling?      │ │
│ │   😫 😕 😐 🙂 😊            │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│        [Analytics Tab]          │
└─────────────────────────────────┘
```

### 2.4 Focus Mode

Triggered when user taps "Complete" on a habit card.

```
Phase 1: Breathing (19 seconds)
├── Full-screen breathing circle
├── Animated scale: 1.0 → 1.4 → 1.4 → 1.0
├── Text prompts: "Breathe in" → "Hold" → "Breathe out"
├── Timing: 4s inhale, 7s hold, 8s exhale
└── Subtle haptic on phase transitions

Phase 2: Completion (type-specific)
├── Binary: Large checkmark animation
├── Timed: Timer interface (if not pre-completed)
├── Increment: Value adjustment UI
├── Reduction: Value adjustment UI
└── Repeating: Already completed via quick-log

Phase 3: Celebration
├── Confetti burst animation
├── "Day X Complete!" message
├── Streak update
├── Heavy haptic feedback
└── Auto-dismiss after 2s → Mood check

Phase 4: Mood Check
├── "How do you feel after [habit]?"
├── 5-point emoji scale
├── Optional - can skip
└── Returns to Home
```

### 2.5 Analytics Screen

```
┌─────────────────────────────────┐
│ Analytics                       │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │   66-DAY CALENDAR           │ │
│ │   [Heatmap grid]            │ │
│ │   ● Completed  ○ Missed     │ │
│ │   ◐ Grace day used          │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │   WEEKLY COMPLETION         │ │
│ │   [Bar chart - 7 weeks]     │ │
│ │   This week: 85%            │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │   MOOD CORRELATION          │ │
│ │   After [habit]:            │ │
│ │   Avg mood: 4.2/5 😊        │ │
│ │   vs baseline: +0.8         │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │   BEST TIME                 │ │
│ │   You complete most often:  │ │
│ │   7:00 - 8:00 AM ☀️         │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │   STATS                     │ │
│ │   Current streak: 12 days   │ │
│ │   Best streak: 12 days      │ │
│ │   Total focus time: 4.2 hrs │ │
│ │   Completion rate: 92%      │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘
```

### 2.6 Second Habit System

**Unlock Conditions:**
- Day >= 21
- First habit has >= 70% completion rate
- User explicitly chooses to add (not auto-prompted)

**Conflict Detection Algorithm:**
```dart
bool hasConflict(String trigger1, String trigger2) {
  final t1 = trigger1.toLowerCase();
  final t2 = trigger2.toLowerCase();

  // Time-based conflicts
  final timeKeywords = ['morning', 'evening', 'night', 'afternoon', 'lunch', 'dinner', 'breakfast'];
  for (final keyword in timeKeywords) {
    if (t1.contains(keyword) && t2.contains(keyword)) return true;
  }

  // Activity-based conflicts
  final activityKeywords = ['coffee', 'shower', 'wake', 'bed', 'work', 'eat', 'brush'];
  for (final keyword in activityKeywords) {
    if (t1.contains(keyword) && t2.contains(keyword)) return true;
  }

  // Word overlap (significant words only)
  final words1 = t1.split(' ').where((w) => w.length > 3).toSet();
  final words2 = t2.split(' ').where((w) => w.length > 3).toSet();
  return words1.intersection(words2).isNotEmpty;
}
```

**Warning UI:**
- Yellow warning banner (not blocking)
- Message: "Similar trigger detected - this might cause confusion"
- "Continue anyway" option available

---

## 3. Data Architecture

### 3.1 State Schema (Dart/Hive)

```dart
import 'package:hive/hive.dart';

part 'models.g.dart';

@HiveType(typeId: 0)
enum HabitType {
  @HiveField(0) binary,
  @HiveField(1) timed,
  @HiveField(2) increment,
  @HiveField(3) reduction,
  @HiveField(4) repeating,
}

// Trigger types - determines how the habit is cued
// Research shows repeating habits work better with flexible cues
@HiveType(typeId: 5)
enum TriggerType {
  @HiveField(0) anchor,      // "After I ___" - required for non-repeating habits
  @HiveField(1) throughout,  // No specific trigger - default for repeating habits
  @HiveField(2) context,     // Context cues like "with meals", "during work"
}

@HiveType(typeId: 1)
class Habit extends HiveObject {
  @HiveField(0) String id;
  @HiveField(1) String name;
  @HiveField(2) HabitType type;
  @HiveField(3) String trigger;           // The trigger text or context cues
  @HiveField(4) int? startValue;
  @HiveField(5) int? targetValue;
  @HiveField(6) int? currentValue;
  @HiveField(7) int? dailyTarget;
  @HiveField(8) int? timedMinutes;
  @HiveField(9) DateTime createdAt;
  @HiveField(10) TriggerType triggerType; // NEW: How the trigger works

  Habit({
    required this.id,
    required this.name,
    required this.type,
    required this.trigger,
    this.triggerType = TriggerType.anchor, // Default for non-repeating
    this.startValue,
    this.targetValue,
    this.currentValue,
    this.dailyTarget,
    this.timedMinutes,
    DateTime? createdAt,
  }) : createdAt = createdAt ?? DateTime.now();
}

@HiveType(typeId: 2)
class Journey extends HiveObject {
  @HiveField(0) DateTime startDate;
  @HiveField(1) List<String> completedDays; // ISO date strings
  @HiveField(2) int graceDaysUsed;
  @HiveField(3) int totalFocusTimeMinutes;

  Journey({
    required this.startDate,
    List<String>? completedDays,
    this.graceDaysUsed = 0,
    this.totalFocusTimeMinutes = 0,
  }) : completedDays = completedDays ?? [];

  int get currentDay {
    return DateTime.now().difference(startDate).inDays + 1;
  }
}

@HiveType(typeId: 3)
class MoodEntry extends HiveObject {
  @HiveField(0) DateTime timestamp;
  @HiveField(1) int mood; // 1-5
  @HiveField(2) String? habitId;

  MoodEntry({
    required this.timestamp,
    required this.mood,
    this.habitId,
  });
}

@HiveType(typeId: 4)
class AppState extends HiveObject {
  @HiveField(0) bool onboarded;
  @HiveField(1) Habit? habit;
  @HiveField(2) Habit? secondHabit;
  @HiveField(3) Journey? journey;
  @HiveField(4) List<MoodEntry> moods;
  @HiveField(5) Map<String, int> repeatingLogs; // "2024-01-26_0": count
  @HiveField(6) String? lastActiveDate;
  @HiveField(7) String? userName;

  AppState({
    this.onboarded = false,
    this.habit,
    this.secondHabit,
    this.journey,
    List<MoodEntry>? moods,
    Map<String, int>? repeatingLogs,
    this.lastActiveDate,
    this.userName,
  })  : moods = moods ?? [],
        repeatingLogs = repeatingLogs ?? {};
}
```

### 3.2 Riverpod Providers

```dart
// app_state_provider.dart
import 'package:flutter_riverpod/flutter_riverpod.dart';

final appStateProvider = StateNotifierProvider<AppStateNotifier, AppState>((ref) {
  return AppStateNotifier();
});

class AppStateNotifier extends StateNotifier<AppState> {
  AppStateNotifier() : super(AppState()) {
    _loadState();
  }

  Future<void> _loadState() async {
    final box = await Hive.openBox<AppState>('onefocus');
    final saved = box.get('state');
    if (saved != null) {
      state = saved;
    }
  }

  Future<void> _saveState() async {
    final box = Hive.box<AppState>('onefocus');
    await box.put('state', state);
  }

  void completeHabit(String habitId) {
    final today = DateTime.now().toIso8601String().split('T')[0];
    final newCompletedDays = [...state.journey!.completedDays, today];

    state = state.copyWith(
      journey: state.journey!.copyWith(
        completedDays: newCompletedDays,
      ),
    );
    _saveState();
  }

  void incrementRepeating(String habitId) {
    final today = DateTime.now().toIso8601String().split('T')[0];
    final key = '${today}_$habitId';
    final newLogs = Map<String, int>.from(state.repeatingLogs);
    newLogs[key] = (newLogs[key] ?? 0) + 1;

    state = state.copyWith(repeatingLogs: newLogs);
    _saveState();
  }

  void addMood(int mood, String? habitId) {
    final entry = MoodEntry(
      timestamp: DateTime.now(),
      mood: mood,
      habitId: habitId,
    );

    state = state.copyWith(
      moods: [...state.moods, entry],
    );
    _saveState();
  }

  // ... more methods
}

// Derived providers
final currentDayProvider = Provider<int>((ref) {
  final state = ref.watch(appStateProvider);
  return state.journey?.currentDay ?? 0;
});

final streakProvider = Provider<int>((ref) {
  final state = ref.watch(appStateProvider);
  if (state.journey == null) return 0;

  final completed = state.journey!.completedDays;
  if (completed.isEmpty) return 0;

  int streak = 0;
  var checkDate = DateTime.now();

  while (true) {
    final dateStr = checkDate.toIso8601String().split('T')[0];
    if (completed.contains(dateStr)) {
      streak++;
      checkDate = checkDate.subtract(Duration(days: 1));
    } else {
      break;
    }
  }

  return streak;
});

final canAddSecondHabitProvider = Provider<bool>((ref) {
  final currentDay = ref.watch(currentDayProvider);
  final state = ref.watch(appStateProvider);
  return currentDay >= 21 && state.secondHabit == null;
});

final completionRateProvider = Provider<double>((ref) {
  final state = ref.watch(appStateProvider);
  if (state.journey == null) return 0.0;

  final currentDay = state.journey!.currentDay;
  final completedCount = state.journey!.completedDays.length;

  return completedCount / currentDay;
});
```

---

## 4. UI Components

### 4.1 Breathing Circle Widget

```dart
class BreathingCircle extends StatefulWidget {
  final VoidCallback onComplete;

  const BreathingCircle({required this.onComplete});

  @override
  State<BreathingCircle> createState() => _BreathingCircleState();
}

class _BreathingCircleState extends State<BreathingCircle>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  String _phase = 'inhale';

  static const _inhaleSeconds = 4;
  static const _holdSeconds = 7;
  static const _exhaleSeconds = 8;
  static const _totalSeconds = _inhaleSeconds + _holdSeconds + _exhaleSeconds;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: Duration(seconds: _totalSeconds),
    )..addListener(_updatePhase)
     ..addStatusListener((status) {
       if (status == AnimationStatus.completed) {
         widget.onComplete();
       }
     });
    _controller.forward();
  }

  void _updatePhase() {
    final progress = _controller.value * _totalSeconds;
    String newPhase;

    if (progress < _inhaleSeconds) {
      newPhase = 'inhale';
    } else if (progress < _inhaleSeconds + _holdSeconds) {
      newPhase = 'hold';
    } else {
      newPhase = 'exhale';
    }

    if (newPhase != _phase) {
      setState(() => _phase = newPhase);
      HapticFeedback.lightImpact();
    }
  }

  double get _scale {
    final progress = _controller.value * _totalSeconds;

    if (progress < _inhaleSeconds) {
      // Inhale: 1.0 → 1.4
      return 1.0 + (progress / _inhaleSeconds) * 0.4;
    } else if (progress < _inhaleSeconds + _holdSeconds) {
      // Hold: stay at 1.4
      return 1.4;
    } else {
      // Exhale: 1.4 → 1.0
      final exhaleProgress = (progress - _inhaleSeconds - _holdSeconds) / _exhaleSeconds;
      return 1.4 - exhaleProgress * 0.4;
    }
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _controller,
      builder: (context, child) {
        return Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Transform.scale(
              scale: _scale,
              child: Container(
                width: 200,
                height: 200,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  gradient: RadialGradient(
                    colors: [
                      Theme.of(context).colorScheme.primary.withOpacity(0.8),
                      Theme.of(context).colorScheme.primary.withOpacity(0.2),
                    ],
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: Theme.of(context).colorScheme.primary.withOpacity(
                        _phase == 'hold' ? 0.6 : 0.3
                      ),
                      blurRadius: _phase == 'hold' ? 40 : 20,
                      spreadRadius: _phase == 'hold' ? 10 : 5,
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 40),
            Text(
              _phase == 'inhale' ? 'Breathe in...' :
              _phase == 'hold' ? 'Hold...' : 'Breathe out...',
              style: Theme.of(context).textTheme.headlineSmall,
            ),
          ],
        );
      },
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }
}
```

### 4.2 Habit Card Widget

```dart
class HabitCard extends ConsumerWidget {
  final Habit habit;
  final bool isSecondary;

  const HabitCard({
    required this.habit,
    this.isSecondary = false,
  });

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isCompleted = ref.watch(isHabitCompletedTodayProvider(habit.id));

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                _buildTypeIcon(),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        habit.name,
                        style: Theme.of(context).textTheme.titleMedium,
                      ),
                      Text(
                        habit.trigger,
                        style: Theme.of(context).textTheme.bodySmall?.copyWith(
                          color: Theme.of(context).colorScheme.onSurfaceVariant,
                        ),
                      ),
                    ],
                  ),
                ),
                if (isCompleted)
                  Icon(
                    Icons.check_circle,
                    color: Theme.of(context).colorScheme.primary,
                  ),
              ],
            ),
            const SizedBox(height: 16),
            _buildTypeSpecificUI(context, ref),
          ],
        ),
      ),
    );
  }

  Widget _buildTypeIcon() {
    final icons = {
      HabitType.binary: '✓',
      HabitType.timed: '⏱',
      HabitType.increment: '📈',
      HabitType.reduction: '📉',
      HabitType.repeating: '🔄',
    };
    return Text(icons[habit.type]!, style: TextStyle(fontSize: 24));
  }

  Widget _buildTypeSpecificUI(BuildContext context, WidgetRef ref) {
    switch (habit.type) {
      case HabitType.binary:
        return _BinaryHabitUI(habit: habit);
      case HabitType.timed:
        return _TimedHabitUI(habit: habit);
      case HabitType.increment:
      case HabitType.reduction:
        return _IncrementHabitUI(habit: habit);
      case HabitType.repeating:
        return _RepeatingHabitUI(habit: habit);
    }
  }
}

class _RepeatingHabitUI extends ConsumerWidget {
  final Habit habit;

  const _RepeatingHabitUI({required this.habit});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final todayCount = ref.watch(repeatingCountTodayProvider(habit.id));
    final target = habit.dailyTarget ?? 8;
    final progress = todayCount / target;

    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              '$todayCount',
              style: Theme.of(context).textTheme.displaySmall,
            ),
            Text(
              ' / $target',
              style: Theme.of(context).textTheme.titleLarge?.copyWith(
                color: Theme.of(context).colorScheme.onSurfaceVariant,
              ),
            ),
          ],
        ),
        const SizedBox(height: 12),
        LinearProgressIndicator(
          value: progress.clamp(0.0, 1.0),
          backgroundColor: Theme.of(context).colorScheme.surfaceVariant,
        ),
        const SizedBox(height: 16),
        FilledButton.icon(
          onPressed: todayCount >= target ? null : () {
            ref.read(appStateProvider.notifier).incrementRepeating(habit.id);
            HapticFeedback.mediumImpact();
          },
          icon: const Icon(Icons.add),
          label: const Text('Quick Log'),
        ),
      ],
    );
  }
}
```

---

## 5. Notifications

### 5.1 Notification Types

| Type | Trigger | Content |
|------|---------|---------|
| Trigger Reminder | User-configured time | "After [trigger], time for [habit]!" |
| Streak Protection | 8pm if not completed | "Don't break your X-day streak!" |
| Milestone | Day 21, 40, 66 | "🎉 Amazing! You've reached day X!" |
| Grace Warning | 2 grace days used | "You have 1 grace day left. Stay strong!" |

### 5.2 Implementation

```dart
class NotificationService {
  final FlutterLocalNotificationsPlugin _plugin = FlutterLocalNotificationsPlugin();

  Future<void> initialize() async {
    const androidSettings = AndroidInitializationSettings('@mipmap/ic_launcher');
    const iosSettings = DarwinInitializationSettings(
      requestAlertPermission: true,
      requestBadgePermission: true,
      requestSoundPermission: true,
    );

    await _plugin.initialize(
      const InitializationSettings(
        android: androidSettings,
        iOS: iosSettings,
      ),
    );
  }

  Future<void> scheduleTriggerReminder({
    required String habitName,
    required String trigger,
    required TimeOfDay time,
  }) async {
    await _plugin.zonedSchedule(
      0, // ID for trigger reminder
      'Time for $habitName',
      'After $trigger, it\'s time to $habitName',
      _nextInstanceOfTime(time),
      const NotificationDetails(
        android: AndroidNotificationDetails(
          'trigger_reminders',
          'Habit Reminders',
          importance: Importance.high,
        ),
        iOS: DarwinNotificationDetails(),
      ),
      uiLocalNotificationDateInterpretation:
          UILocalNotificationDateInterpretation.absoluteTime,
      matchDateTimeComponents: DateTimeComponents.time,
    );
  }

  Future<void> scheduleStreakProtection({
    required int currentStreak,
  }) async {
    // Only schedule if streak > 3 days
    if (currentStreak < 3) return;

    await _plugin.zonedSchedule(
      1, // ID for streak protection
      'Don\'t break your streak!',
      'You have a $currentStreak-day streak going. Complete your habit now!',
      _todayAt(20, 0), // 8pm
      const NotificationDetails(
        android: AndroidNotificationDetails(
          'streak_protection',
          'Streak Reminders',
          importance: Importance.high,
        ),
        iOS: DarwinNotificationDetails(),
      ),
      uiLocalNotificationDateInterpretation:
          UILocalNotificationDateInterpretation.absoluteTime,
    );
  }

  tz.TZDateTime _nextInstanceOfTime(TimeOfDay time) {
    final now = tz.TZDateTime.now(tz.local);
    var scheduled = tz.TZDateTime(
      tz.local,
      now.year,
      now.month,
      now.day,
      time.hour,
      time.minute,
    );
    if (scheduled.isBefore(now)) {
      scheduled = scheduled.add(const Duration(days: 1));
    }
    return scheduled;
  }

  tz.TZDateTime _todayAt(int hour, int minute) {
    final now = tz.TZDateTime.now(tz.local);
    return tz.TZDateTime(tz.local, now.year, now.month, now.day, hour, minute);
  }
}
```

---

## 6. Home Screen Widgets

### 6.1 iOS Widget (Swift)

```swift
// OneFocusWidget.swift
import WidgetKit
import SwiftUI

struct OneFocusEntry: TimelineEntry {
    let date: Date
    let habitName: String
    let currentDay: Int
    let isCompletedToday: Bool
    let streak: Int
}

struct OneFocusWidgetEntryView: View {
    var entry: OneFocusEntry

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Text("Day \(entry.currentDay)")
                    .font(.headline)
                    .foregroundColor(.purple)
                Spacer()
                if entry.isCompletedToday {
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(.green)
                }
            }

            Text(entry.habitName)
                .font(.title3)
                .fontWeight(.semibold)

            Spacer()

            HStack {
                Image(systemName: "flame.fill")
                    .foregroundColor(.orange)
                Text("\(entry.streak) day streak")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
        }
        .padding()
    }
}
```

### 6.2 Android Widget (Kotlin + Compose Glance)

```kotlin
// OneFocusWidget.kt
class OneFocusWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = context.getSharedPreferences("onefocus", Context.MODE_PRIVATE)
        val habitName = prefs.getString("habit_name", "Your Habit") ?: "Your Habit"
        val currentDay = prefs.getInt("current_day", 1)
        val streak = prefs.getInt("streak", 0)
        val isCompleted = prefs.getBoolean("completed_today", false)

        provideContent {
            OneFocusWidgetContent(
                habitName = habitName,
                currentDay = currentDay,
                streak = streak,
                isCompleted = isCompleted,
            )
        }
    }
}

@Composable
fun OneFocusWidgetContent(
    habitName: String,
    currentDay: Int,
    streak: Int,
    isCompleted: Boolean,
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2E))
            .padding(16.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Day $currentDay",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFBB86FC)),
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(GlanceModifier.defaultWeight())
            if (isCompleted) {
                Image(
                    provider = ImageProvider(R.drawable.ic_check),
                    contentDescription = "Completed",
                )
            }
        }

        Spacer(GlanceModifier.height(8.dp))

        Text(
            text = habitName,
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            ),
        )

        Spacer(GlanceModifier.defaultWeight())

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                provider = ImageProvider(R.drawable.ic_flame),
                contentDescription = "Streak",
            )
            Spacer(GlanceModifier.width(4.dp))
            Text(
                text = "$streak day streak",
                style = TextStyle(
                    color = ColorProvider(Color.Gray),
                    fontSize = 12.sp,
                ),
            )
        }
    }
}
```

---

## 7. Testing Plan

### 7.1 Unit Tests

```dart
// streak_test.dart
void main() {
  group('Streak Calculation', () {
    test('returns 0 for empty completed days', () {
      final journey = Journey(startDate: DateTime.now(), completedDays: []);
      expect(calculateStreak(journey), 0);
    });

    test('returns correct streak for consecutive days', () {
      final today = DateTime.now();
      final journey = Journey(
        startDate: today.subtract(Duration(days: 5)),
        completedDays: [
          today.subtract(Duration(days: 2)).toIso8601String().split('T')[0],
          today.subtract(Duration(days: 1)).toIso8601String().split('T')[0],
          today.toIso8601String().split('T')[0],
        ],
      );
      expect(calculateStreak(journey), 3);
    });

    test('breaks streak on missed day', () {
      final today = DateTime.now();
      final journey = Journey(
        startDate: today.subtract(Duration(days: 5)),
        completedDays: [
          today.subtract(Duration(days: 3)).toIso8601String().split('T')[0],
          // Day 2 missed
          today.subtract(Duration(days: 1)).toIso8601String().split('T')[0],
          today.toIso8601String().split('T')[0],
        ],
      );
      expect(calculateStreak(journey), 2);
    });
  });

  group('Conflict Detection', () {
    test('detects time-based conflicts', () {
      expect(hasConflict('After my morning coffee', 'When I wake up in the morning'), true);
    });

    test('no conflict for different times', () {
      expect(hasConflict('After my morning coffee', 'Before bed at night'), false);
    });

    test('detects activity-based conflicts', () {
      expect(hasConflict('After I pour coffee', 'When drinking my coffee'), true);
    });
  });
}
```

### 7.2 Widget Tests

```dart
// habit_card_test.dart
void main() {
  testWidgets('RepeatingHabitUI shows correct count', (tester) async {
    final habit = Habit(
      id: '1',
      name: 'Drink water',
      type: HabitType.repeating,
      trigger: 'Throughout the day',
      dailyTarget: 8,
    );

    await tester.pumpWidget(
      ProviderScope(
        overrides: [
          repeatingCountTodayProvider('1').overrideWithValue(5),
        ],
        child: MaterialApp(
          home: Scaffold(body: _RepeatingHabitUI(habit: habit)),
        ),
      ),
    );

    expect(find.text('5'), findsOneWidget);
    expect(find.text(' / 8'), findsOneWidget);
  });

  testWidgets('Quick log button increments count', (tester) async {
    // ... test implementation
  });
}
```

### 7.3 Integration Tests

```dart
// onboarding_flow_test.dart
void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  testWidgets('Complete onboarding flow', (tester) async {
    await tester.pumpWidget(const OneFocusApp());

    // Welcome screen
    expect(find.text('OneFocus'), findsOneWidget);
    await tester.tap(find.text('Begin Your Journey'));
    await tester.pumpAndSettle();

    // Habit type selection
    await tester.tap(find.text('Binary'));
    await tester.tap(find.text('Continue'));
    await tester.pumpAndSettle();

    // Habit name
    await tester.enterText(find.byType(TextField), 'Meditate');
    await tester.tap(find.text('Continue'));
    await tester.pumpAndSettle();

    // Trigger setup
    await tester.enterText(
      find.byType(TextField),
      'I pour my morning coffee',
    );
    await tester.tap(find.text('Start My Journey'));
    await tester.pumpAndSettle();

    // Ready screen
    await tester.tap(find.text("Let's Go"));
    await tester.pumpAndSettle();

    // Should be on home screen
    expect(find.text('Day 1'), findsOneWidget);
    expect(find.text('Meditate'), findsOneWidget);
  });
}
```

---

## 8. Release Checklist

### Pre-Launch
- [ ] All unit tests passing
- [ ] All widget tests passing
- [ ] Integration tests on real devices (iOS + Android)
- [ ] Performance profiling (60fps animations)
- [ ] Accessibility audit (VoiceOver, TalkBack)
- [ ] Localization (if applicable)
- [ ] Privacy policy created
- [ ] App store assets (screenshots, descriptions)

### iOS Specific
- [ ] Test on iPhone SE (smallest screen)
- [ ] Test on iPhone 15 Pro Max (largest screen)
- [ ] Widget works correctly
- [ ] Dynamic Type support
- [ ] Dark/Light mode transitions

### Android Specific
- [ ] Test on API 26 device (minimum)
- [ ] Test on Pixel (stock Android)
- [ ] Test on Samsung (One UI)
- [ ] Material You theming works
- [ ] Widget renders correctly
- [ ] Edge-to-edge display

---

## 9. Future Considerations

### Phase 2 Features (Post-MVP)
- iCloud/Google Drive backup
- Apple Watch / Wear OS companion
- Habit templates library
- Social accountability (optional share)
- Advanced analytics (exportable data)

### Not Planned (By Design)
- More than 2 habits
- Social feeds
- Gamification beyond streaks
- Premium tiers
- Advertisements

---

*This specification is the source of truth for OneFocus development. All implementation should reference this document.*
