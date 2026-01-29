# OneFocus iOS Architecture

**Platform:** iOS 17.0+
**Language:** Swift 5.9+
**UI Framework:** SwiftUI
**Storage:** SwiftData
**Architecture:** MVVM with @Observable

---

## Project Setup

### Xcode Project Configuration
```
Product Name: OneFocus
Team: [Your Team]
Organization Identifier: com.onefocus
Bundle Identifier: com.onefocus.app
Deployment Target: iOS 17.0
Interface: SwiftUI
Language: Swift
Storage: SwiftData
Include Tests: Yes
```

### Capabilities Required
- App Groups (for widget data sharing)
- Push Notifications
- Background Modes (Background fetch for streak protection)

### App Group ID
```
group.com.onefocus.shared
```

---

## File Structure

```
OneFocus/
├── OneFocusApp.swift
├── ContentView.swift
│
├── Core/
│   ├── Design/
│   │   ├── Colors.swift
│   │   ├── Typography.swift
│   │   ├── Haptics.swift
│   │   └── Theme.swift
│   ├── Extensions/
│   │   ├── Color+Hex.swift
│   │   ├── Date+Extensions.swift
│   │   └── View+Extensions.swift
│   └── Components/
│       ├── GradientText.swift
│       ├── AnimatedCounter.swift
│       └── ProgressRing.swift
│
├── Models/
│   ├── Habit.swift
│   ├── Journey.swift
│   ├── MoodEntry.swift
│   ├── RepeatingLog.swift
│   └── Enums/
│       ├── HabitType.swift
│       └── TriggerType.swift
│
├── Services/
│   ├── DataService.swift
│   ├── NotificationService.swift
│   └── WidgetService.swift
│
├── Features/
│   ├── Onboarding/
│   ├── Home/
│   ├── Focus/
│   ├── Analytics/
│   └── SecondHabit/
│
├── Widget/
│   ├── OneFocusWidget.swift
│   ├── WidgetEntry.swift
│   └── WidgetViews/
│
└── Resources/
    ├── Assets.xcassets
    └── Localizable.strings
```

---

## Data Models (SwiftData)

### Habit.swift
```swift
import SwiftData
import Foundation

@Model
final class Habit {
    @Attribute(.unique) var id: UUID
    var name: String
    var typeRaw: String
    var trigger: String
    var triggerTypeRaw: String
    var startValue: Int?
    var targetValue: Int?
    var currentValue: Int?
    var dailyTarget: Int?
    var timedMinutes: Int?
    var createdAt: Date
    var isSecondary: Bool

    var type: HabitType {
        get { HabitType(rawValue: typeRaw) ?? .binary }
        set { typeRaw = newValue.rawValue }
    }

    var triggerType: TriggerType {
        get { TriggerType(rawValue: triggerTypeRaw) ?? .anchor }
        set { triggerTypeRaw = newValue.rawValue }
    }

    init(
        name: String,
        type: HabitType,
        trigger: String,
        triggerType: TriggerType = .anchor,
        isSecondary: Bool = false
    ) {
        self.id = UUID()
        self.name = name
        self.typeRaw = type.rawValue
        self.trigger = trigger
        self.triggerTypeRaw = triggerType.rawValue
        self.createdAt = Date()
        self.isSecondary = isSecondary
    }

    // Display helpers
    var triggerDisplayText: String {
        switch triggerType {
        case .throughout:
            return "Throughout the day"
        case .context:
            return trigger
        case .anchor:
            return "After I \(trigger)"
        }
    }

    var typeIcon: String {
        type.icon
    }

    var typeLabel: String {
        type.label
    }
}
```

### Journey.swift
```swift
import SwiftData
import Foundation

@Model
final class Journey {
    @Attribute(.unique) var id: UUID
    var startDate: Date
    var completedDays: [String]  // ISO date strings "2024-01-26"
    var graceDaysUsed: Int
    var totalFocusTimeSeconds: Int

    init(startDate: Date = Date()) {
        self.id = UUID()
        self.startDate = Calendar.current.startOfDay(for: startDate)
        self.completedDays = []
        self.graceDaysUsed = 0
        self.totalFocusTimeSeconds = 0
    }

    var currentDay: Int {
        let calendar = Calendar.current
        let days = calendar.dateComponents([.day], from: startDate, to: Date()).day ?? 0
        return min(max(days + 1, 1), 66)
    }

    var currentPhase: JourneyPhase {
        if currentDay <= 21 { return .foundation }
        if currentDay <= 42 { return .strengthening }
        return .cementing
    }

    var currentStreak: Int {
        var streak = 0
        var checkDate = Date()
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withFullDate]

        while streak < currentDay {
            let dateString = formatter.string(from: checkDate).prefix(10)
            if completedDays.contains(String(dateString)) {
                streak += 1
                checkDate = Calendar.current.date(byAdding: .day, value: -1, to: checkDate)!
            } else {
                break
            }
        }
        return streak
    }

    var graceDaysRemaining: Int {
        3 - graceDaysUsed
    }

    var progress: Double {
        Double(currentDay) / 66.0
    }

    func isCompletedToday(habitIndex: Int = 0) -> Bool {
        let today = todayKey(habitIndex: habitIndex)
        return completedDays.contains(today)
    }

    func markComplete(habitIndex: Int = 0) {
        let today = todayKey(habitIndex: habitIndex)
        if !completedDays.contains(today) {
            completedDays.append(today)
        }
    }

    private func todayKey(habitIndex: Int) -> String {
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withFullDate]
        let dateStr = String(formatter.string(from: Date()).prefix(10))
        return habitIndex == 0 ? dateStr : "\(dateStr)_2"
    }
}

enum JourneyPhase: String {
    case foundation = "Building Foundation"
    case strengthening = "Strengthening"
    case cementing = "Cementing Habit"
}
```

### MoodEntry.swift
```swift
import SwiftData
import Foundation

@Model
final class MoodEntry {
    @Attribute(.unique) var id: UUID
    var timestamp: Date
    var mood: Int  // 1-5
    var habitId: UUID?
    var isBefore: Bool  // Before or after habit completion

    init(mood: Int, habitId: UUID? = nil, isBefore: Bool = true) {
        self.id = UUID()
        self.timestamp = Date()
        self.mood = mood
        self.habitId = habitId
        self.isBefore = isBefore
    }
}
```

### RepeatingLog.swift
```swift
import SwiftData
import Foundation

@Model
final class RepeatingLog {
    @Attribute(.unique) var id: UUID
    var dateKey: String  // "2024-01-26_0" (date + habit index)
    var count: Int

    init(dateKey: String, count: Int = 1) {
        self.id = UUID()
        self.dateKey = dateKey
        self.count = count
    }
}
```

### Enums/HabitType.swift
```swift
import Foundation

enum HabitType: String, Codable, CaseIterable, Identifiable {
    case binary
    case timed
    case increment
    case reduction
    case repeating

    var id: String { rawValue }

    var icon: String {
        switch self {
        case .binary: return "checkmark.circle"
        case .timed: return "timer"
        case .increment: return "chart.line.uptrend.xyaxis"
        case .reduction: return "chart.line.downtrend.xyaxis"
        case .repeating: return "arrow.2.squarepath"
        }
    }

    var emoji: String {
        switch self {
        case .binary: return "✓"
        case .timed: return "⏱"
        case .increment: return "📈"
        case .reduction: return "📉"
        case .repeating: return "🔄"
        }
    }

    var label: String {
        switch self {
        case .binary: return "Daily"
        case .timed: return "Timed"
        case .increment: return "Build Up"
        case .reduction: return "Cut Down"
        case .repeating: return "Repeating"
        }
    }

    var description: String {
        switch self {
        case .binary: return "Yes or no each day"
        case .timed: return "Duration-based habit"
        case .increment: return "Increase over time"
        case .reduction: return "Reduce a behavior"
        case .repeating: return "Multiple times daily"
        }
    }

    var requiresTrigger: Bool {
        self != .repeating
    }
}
```

### Enums/TriggerType.swift
```swift
import Foundation

enum TriggerType: String, Codable, CaseIterable, Identifiable {
    case anchor      // "After I ___"
    case throughout  // No specific trigger
    case context     // Context cues

    var id: String { rawValue }

    var label: String {
        switch self {
        case .anchor: return "After a specific action"
        case .throughout: return "Throughout the day"
        case .context: return "At specific moments"
        }
    }

    var description: String {
        switch self {
        case .anchor: return "Use the traditional \"After I ___\" format"
        case .throughout: return "No specific trigger - log whenever you do it"
        case .context: return "Set context cues like \"with meals\" or \"during work\""
        }
    }

    var isRecommendedForRepeating: Bool {
        self == .throughout
    }
}
```

---

## Design System

### Colors.swift
```swift
import SwiftUI

extension Color {
    // Primary
    static let accent = Color(hex: "BF5AF2")
    static let accentSecondary = Color(hex: "5E5CE6")
    static let accentDim = Color(hex: "BF5AF2").opacity(0.15)
    static let accentGlow = Color(hex: "BF5AF2").opacity(0.4)

    // Backgrounds
    static let bgPrimary = Color.black
    static let bgSecondary = Color(hex: "1C1C1E")
    static let bgTertiary = Color(hex: "2C2C2E")

    // Text
    static let textPrimary = Color.white
    static let textSecondary = Color.white.opacity(0.6)
    static let textTertiary = Color.white.opacity(0.3)

    // Semantic
    static let success = Color(hex: "30D158")
    static let warning = Color(hex: "FFD60A")
    static let error = Color(hex: "FF453A")

    // Mood colors
    static let mood1 = Color(hex: "FF453A")
    static let mood2 = Color(hex: "FF9F0A")
    static let mood3 = Color(hex: "FFD60A")
    static let mood4 = Color(hex: "30D158")
    static let mood5 = Color(hex: "64D2FF")

    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default: (a, r, g, b) = (255, 0, 0, 0)
        }
        self.init(.sRGB, red: Double(r) / 255, green: Double(g) / 255, blue: Double(b) / 255, opacity: Double(a) / 255)
    }
}
```

### Haptics.swift
```swift
import UIKit

enum HapticType {
    case light
    case medium
    case heavy
    case success
    case warning
    case error
    case selection
}

final class HapticManager {
    static let shared = HapticManager()
    private init() {}

    func trigger(_ type: HapticType) {
        switch type {
        case .light:
            UIImpactFeedbackGenerator(style: .light).impactOccurred()
        case .medium:
            UIImpactFeedbackGenerator(style: .medium).impactOccurred()
        case .heavy:
            UIImpactFeedbackGenerator(style: .heavy).impactOccurred()
        case .success:
            UINotificationFeedbackGenerator().notificationOccurred(.success)
        case .warning:
            UINotificationFeedbackGenerator().notificationOccurred(.warning)
        case .error:
            UINotificationFeedbackGenerator().notificationOccurred(.error)
        case .selection:
            UISelectionFeedbackGenerator().selectionChanged()
        }
    }
}

extension View {
    func haptic(_ type: HapticType) -> some View {
        self.onTapGesture {
            HapticManager.shared.trigger(type)
        }
    }
}
```

---

## Key Views

### BreathingCircle.swift
```swift
import SwiftUI

struct BreathingCircle: View {
    @State private var scale: CGFloat = 1.0
    @State private var glowIntensity: CGFloat = 0.3
    @State private var phase: BreathPhase = .inhale
    @State private var phaseText: String = "Breathe in..."

    let onComplete: () -> Void

    private let inhaleSeconds = 4.0
    private let holdSeconds = 7.0
    private let exhaleSeconds = 8.0

    var body: some View {
        VStack(spacing: 40) {
            ZStack {
                // Glow layer
                Circle()
                    .fill(Color.accent.opacity(glowIntensity))
                    .frame(width: 240, height: 240)
                    .blur(radius: 40)

                // Main circle
                Circle()
                    .fill(
                        RadialGradient(
                            colors: [Color.accent, Color.accentSecondary],
                            center: .center,
                            startRadius: 0,
                            endRadius: 100
                        )
                    )
                    .frame(width: 200, height: 200)
                    .scaleEffect(scale)
            }

            Text(phaseText)
                .font(.title2)
                .foregroundColor(.textSecondary)
        }
        .onAppear {
            startBreathingCycle()
        }
    }

    private func startBreathingCycle() {
        // Inhale
        phase = .inhale
        phaseText = "Breathe in..."
        HapticManager.shared.trigger(.light)

        withAnimation(.easeInOut(duration: inhaleSeconds)) {
            scale = 1.4
            glowIntensity = 0.5
        }

        // Hold
        DispatchQueue.main.asyncAfter(deadline: .now() + inhaleSeconds) {
            phase = .hold
            phaseText = "Hold..."
            HapticManager.shared.trigger(.light)

            withAnimation(.easeInOut(duration: holdSeconds)) {
                glowIntensity = 0.6
            }
        }

        // Exhale
        DispatchQueue.main.asyncAfter(deadline: .now() + inhaleSeconds + holdSeconds) {
            phase = .exhale
            phaseText = "Breathe out..."
            HapticManager.shared.trigger(.light)

            withAnimation(.easeInOut(duration: exhaleSeconds)) {
                scale = 1.0
                glowIntensity = 0.3
            }
        }

        // Complete
        DispatchQueue.main.asyncAfter(deadline: .now() + inhaleSeconds + holdSeconds + exhaleSeconds) {
            onComplete()
        }
    }
}

enum BreathPhase {
    case inhale, hold, exhale
}
```

### HabitCard.swift
```swift
import SwiftUI
import SwiftData

struct HabitCard: View {
    let habit: Habit
    let isCompletedToday: Bool
    let repeatingCount: Int
    let onTap: () -> Void
    let onQuickLog: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Accent bar
            Rectangle()
                .fill(isCompletedToday ? Color.success : Color.accent)
                .frame(height: 4)

            VStack(alignment: .leading, spacing: 16) {
                // Header
                HStack {
                    Label(habit.typeLabel, systemImage: habit.typeIcon)
                        .font(.caption)
                        .foregroundColor(.accent)
                        .padding(.horizontal, 10)
                        .padding(.vertical, 4)
                        .background(Color.accentDim)
                        .cornerRadius(20)

                    Spacer()

                    Text(habit.isSecondary ? "2nd habit" : "1st habit")
                        .font(.caption)
                        .foregroundColor(.textTertiary)
                }

                // Name & Trigger
                Text(habit.name)
                    .font(.title2)
                    .fontWeight(.semibold)

                Text(habit.triggerDisplayText)
                    .font(.subheadline)
                    .foregroundColor(.textSecondary)

                // Type-specific UI
                if habit.type == .repeating {
                    repeatingUI
                } else {
                    actionButton
                }
            }
            .padding(20)
        }
        .background(Color.bgSecondary)
        .cornerRadius(24)
    }

    @ViewBuilder
    private var repeatingUI: some View {
        let target = habit.dailyTarget ?? 8
        let progress = min(Double(repeatingCount) / Double(target), 1.0)

        VStack(spacing: 16) {
            HStack {
                Text("\(repeatingCount)")
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundStyle(
                        LinearGradient(
                            colors: [.accent, .success],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
                Text("/ \(target) times")
                    .foregroundColor(.textSecondary)
            }

            ProgressView(value: progress)
                .tint(Color.accent)

            Button(action: onQuickLog) {
                HStack {
                    Image(systemName: repeatingCount >= target ? "checkmark" : "plus")
                    Text(repeatingCount >= target ? "Target Reached" : "Log \(habit.name)")
                }
                .frame(maxWidth: .infinity)
                .padding()
                .background(repeatingCount >= target ? Color.success.opacity(0.15) : Color.accent)
                .foregroundColor(repeatingCount >= target ? .success : .white)
                .cornerRadius(14)
            }
            .disabled(repeatingCount >= target)
        }
    }

    @ViewBuilder
    private var actionButton: some View {
        Button(action: onTap) {
            Text(isCompletedToday ? "Completed today ✓" : "Start \(habit.name)")
                .frame(maxWidth: .infinity)
                .padding()
                .background(isCompletedToday ? Color.success.opacity(0.15) : Color.accent)
                .foregroundColor(isCompletedToday ? .success : .white)
                .cornerRadius(14)
        }
        .disabled(isCompletedToday)
    }
}
```

---

## Services

### DataService.swift
```swift
import SwiftData
import Foundation

@MainActor
final class DataService {
    static let shared = DataService()

    let container: ModelContainer

    private init() {
        let schema = Schema([
            Habit.self,
            Journey.self,
            MoodEntry.self,
            RepeatingLog.self
        ])

        let config = ModelConfiguration(
            schema: schema,
            isStoredInMemoryOnly: false,
            groupContainer: .identifier("group.com.onefocus.shared")
        )

        do {
            container = try ModelContainer(for: schema, configurations: config)
        } catch {
            fatalError("Failed to create ModelContainer: \(error)")
        }
    }

    var context: ModelContext {
        container.mainContext
    }

    // MARK: - Habit Operations

    func getPrimaryHabit() -> Habit? {
        let descriptor = FetchDescriptor<Habit>(
            predicate: #Predicate { !$0.isSecondary },
            sortBy: [SortDescriptor(\.createdAt)]
        )
        return try? context.fetch(descriptor).first
    }

    func getSecondaryHabit() -> Habit? {
        let descriptor = FetchDescriptor<Habit>(
            predicate: #Predicate { $0.isSecondary },
            sortBy: [SortDescriptor(\.createdAt)]
        )
        return try? context.fetch(descriptor).first
    }

    func createHabit(_ habit: Habit) {
        context.insert(habit)
        try? context.save()
    }

    // MARK: - Journey Operations

    func getOrCreateJourney() -> Journey {
        let descriptor = FetchDescriptor<Journey>()
        if let journey = try? context.fetch(descriptor).first {
            return journey
        }
        let journey = Journey()
        context.insert(journey)
        try? context.save()
        return journey
    }

    // MARK: - Repeating Logs

    func getRepeatingCount(for habitIndex: Int, on date: Date = Date()) -> Int {
        let dateKey = makeRepeatingKey(date: date, habitIndex: habitIndex)
        let descriptor = FetchDescriptor<RepeatingLog>(
            predicate: #Predicate { $0.dateKey == dateKey }
        )
        return (try? context.fetch(descriptor).first?.count) ?? 0
    }

    func incrementRepeating(for habitIndex: Int, on date: Date = Date()) {
        let dateKey = makeRepeatingKey(date: date, habitIndex: habitIndex)
        let descriptor = FetchDescriptor<RepeatingLog>(
            predicate: #Predicate { $0.dateKey == dateKey }
        )

        if let log = try? context.fetch(descriptor).first {
            log.count += 1
        } else {
            let log = RepeatingLog(dateKey: dateKey)
            context.insert(log)
        }
        try? context.save()
    }

    private func makeRepeatingKey(date: Date, habitIndex: Int) -> String {
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withFullDate]
        let dateStr = String(formatter.string(from: date).prefix(10))
        return "\(dateStr)_\(habitIndex)"
    }
}
```

### NotificationService.swift
```swift
import UserNotifications

final class NotificationService {
    static let shared = NotificationService()
    private init() {}

    func requestPermission() async -> Bool {
        do {
            return try await UNUserNotificationCenter.current()
                .requestAuthorization(options: [.alert, .sound, .badge])
        } catch {
            return false
        }
    }

    func scheduleTriggerReminder(habitName: String, trigger: String, time: DateComponents) {
        let content = UNMutableNotificationContent()
        content.title = "Time for \(habitName)"
        content.body = "After \(trigger), it's time to \(habitName)"
        content.sound = .default

        let trigger = UNCalendarNotificationTrigger(dateMatching: time, repeats: true)
        let request = UNNotificationRequest(
            identifier: "trigger_reminder",
            content: content,
            trigger: trigger
        )

        UNUserNotificationCenter.current().add(request)
    }

    func scheduleStreakProtection(streak: Int) {
        guard streak >= 3 else { return }

        let content = UNMutableNotificationContent()
        content.title = "Don't break your streak!"
        content.body = "You have a \(streak)-day streak going. Complete your habit now!"
        content.sound = .default

        var dateComponents = DateComponents()
        dateComponents.hour = 20
        dateComponents.minute = 0

        let trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: false)
        let request = UNNotificationRequest(
            identifier: "streak_protection",
            content: content,
            trigger: trigger
        )

        UNUserNotificationCenter.current().add(request)
    }

    func cancelAll() {
        UNUserNotificationCenter.current().removeAllPendingNotificationRequests()
    }
}
```

---

## Widget

### OneFocusWidget.swift
```swift
import WidgetKit
import SwiftUI
import SwiftData

struct OneFocusWidget: Widget {
    let kind: String = "OneFocusWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            OneFocusWidgetEntryView(entry: entry)
                .containerBackground(.fill.tertiary, for: .widget)
        }
        .configurationDisplayName("OneFocus")
        .description("Track your habit progress")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

struct Provider: TimelineProvider {
    func placeholder(in context: Context) -> WidgetEntry {
        WidgetEntry(
            date: Date(),
            habitName: "Meditate",
            currentDay: 12,
            streak: 12,
            isCompletedToday: false
        )
    }

    func getSnapshot(in context: Context, completion: @escaping (WidgetEntry) -> Void) {
        completion(placeholder(in: context))
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<WidgetEntry>) -> Void) {
        // Fetch from shared container
        let entry = loadEntry()
        let nextUpdate = Calendar.current.date(byAdding: .hour, value: 1, to: Date())!
        let timeline = Timeline(entries: [entry], policy: .after(nextUpdate))
        completion(timeline)
    }

    private func loadEntry() -> WidgetEntry {
        // Load from App Group shared container
        // This would read from SwiftData or UserDefaults
        return WidgetEntry(
            date: Date(),
            habitName: "Meditate",
            currentDay: 12,
            streak: 12,
            isCompletedToday: false
        )
    }
}

struct WidgetEntry: TimelineEntry {
    let date: Date
    let habitName: String
    let currentDay: Int
    let streak: Int
    let isCompletedToday: Bool
}

struct OneFocusWidgetEntryView: View {
    var entry: WidgetEntry

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

---

## Testing

### HabitTests.swift
```swift
import Testing
import SwiftData
@testable import OneFocus

@Suite("Habit Model Tests")
struct HabitTests {
    @Test("Trigger display text for anchor type")
    func triggerDisplayAnchor() {
        let habit = Habit(
            name: "Meditate",
            type: .binary,
            trigger: "pour my coffee",
            triggerType: .anchor
        )
        #expect(habit.triggerDisplayText == "After I pour my coffee")
    }

    @Test("Trigger display text for throughout type")
    func triggerDisplayThroughout() {
        let habit = Habit(
            name: "Drink water",
            type: .repeating,
            trigger: "",
            triggerType: .throughout
        )
        #expect(habit.triggerDisplayText == "Throughout the day")
    }

    @Test("Trigger display text for context type")
    func triggerDisplayContext() {
        let habit = Habit(
            name: "Drink water",
            type: .repeating,
            trigger: "With meals, During work",
            triggerType: .context
        )
        #expect(habit.triggerDisplayText == "With meals, During work")
    }
}

@Suite("Journey Model Tests")
struct JourneyTests {
    @Test("Current day calculation")
    func currentDayCalculation() {
        let fiveDaysAgo = Calendar.current.date(byAdding: .day, value: -5, to: Date())!
        let journey = Journey(startDate: fiveDaysAgo)
        #expect(journey.currentDay == 6)
    }

    @Test("Streak calculation with consecutive days")
    func streakCalculation() {
        let journey = Journey(startDate: Date())
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withFullDate]

        // Add today and yesterday
        let today = String(formatter.string(from: Date()).prefix(10))
        let yesterday = String(formatter.string(from: Calendar.current.date(byAdding: .day, value: -1, to: Date())!).prefix(10))

        journey.completedDays = [yesterday, today]
        #expect(journey.currentStreak == 2)
    }

    @Test("Grace days remaining")
    func graceDaysRemaining() {
        let journey = Journey()
        journey.graceDaysUsed = 1
        #expect(journey.graceDaysRemaining == 2)
    }
}
```

---

## Release Checklist

- [ ] App icon in all sizes
- [ ] Launch screen
- [ ] Privacy policy URL
- [ ] App Store screenshots (6.5", 5.5")
- [ ] App Store description
- [ ] Keywords
- [ ] TestFlight beta testing
- [ ] Accessibility audit (VoiceOver, Dynamic Type)
- [ ] Dark/Light mode testing
- [ ] Widget testing on all sizes
- [ ] Notification permissions and behavior
- [ ] Data persistence verification
- [ ] Performance profiling
- [ ] Memory leak testing
