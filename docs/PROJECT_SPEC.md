# OneFocus - Project Specification

## Executive Summary

OneFocus is a research-backed habit formation app that helps users build lasting habits through radical constraints and progressive unlocks. Unlike traditional productivity apps that overwhelm users with unlimited options, OneFocus enforces a "one thing at a time" philosophy grounded in behavioral science.

---

## 1. Problem Statement

### The Overwhelm Trap
Most habit and productivity apps fail because they:
- Allow unlimited habits/tasks, leading to overwhelm
- Use the debunked "21-day habit" myth
- Punish users for missing days, creating guilt spirals
- Treat all habits the same regardless of type
- Lack social accountability mechanisms

### Our Solution
OneFocus addresses these issues by:
- **Radical constraints**: ONE habit only until day 21
- **Real science**: 66-day journey based on actual research
- **Grace over guilt**: 3 grace days per journey
- **Habit-type awareness**: 4 distinct tracking modes
- **Progressive unlocks**: Earn capabilities through consistency

---

## 2. Target Users

### Primary Persona: The Overwhelmed Optimizer
- Age: 25-45
- Has tried multiple productivity apps
- Often starts habits but doesn't maintain them
- Easily overwhelmed by too many options
- Values simplicity and focus
- May have ADHD or executive function challenges

### Secondary Persona: The Habit Builder
- Wants to establish a specific new habit
- Understands the value of focus
- Looking for accountability
- Appreciates science-backed approaches

---

## 3. Core Features

### 3.1 Habit Types

| Type | Use Case | Tracking | Example |
|------|----------|----------|---------|
| **Binary** | Simple yes/no habits | Check mark | "Make my bed" |
| **Timed** | Fixed duration activities | Timer to target | "Meditate for 10 min" |
| **Increment** | Building up over time | Increasing daily target | "Run +1 min each day" |
| **Reduction** | Cutting down behaviors | Resistance timer | "Resist social media" |

### 3.2 The 66-Day Journey

**Why 66 Days?**
Research by Phillippa Lally (UCL) found habits take 18-254 days to form, with 66 days being the average. This is dramatically longer than the popular "21-day" myth.

**Journey Phases:**

| Phase | Days | Name | Description |
|-------|------|------|-------------|
| 1 | 1-21 | Building Foundation | Establishing the neural pathway |
| 2 | 22-42 | Strengthening | Deepening the habit groove |
| 3 | 43-66 | Cementing | Making it automatic |

**Milestones:**
- Day 1: Journey begins
- Day 7: First week complete
- Day 21: Foundation built, unlock habit stacking
- Day 42: Two-thirds complete
- Day 66: Journey complete, habit formed

### 3.3 Implementation Intentions

Users must set an "implementation intention" during setup:

> "After I [EXISTING ROUTINE], I will [NEW HABIT]"

**Examples:**
- "After I pour my morning coffee, I will meditate"
- "After I sit at my desk, I will review my priorities"
- "After I eat dinner, I will go for a walk"

**Why It Works:**
Gollwitzer's meta-analysis of 94 studies found implementation intentions increase goal achievement by 65%. They work by:
1. Creating a specific cue in the environment
2. Automating the decision-making process
3. Linking new behavior to existing routine

### 3.4 Grace Days

Users receive 3 grace days per 66-day journey.

**Why Grace Days?**
Research shows:
- Missing one day does NOT significantly impact habit formation
- Guilt and shame are counterproductive to behavior change
- Flexibility increases long-term adherence

**How They Work:**
- If a user misses a day, they can "use" a grace day
- Their streak and journey continue unaffected
- After 3 grace days, subsequent misses don't reset progress but do affect completion rate

### 3.5 Habit Stacking (Unlock at Day 21)

After 21 days of consistency, users unlock the ability to add a second habit. This second habit MUST stack onto the first:

> "After I [FIRST HABIT], I will [SECOND HABIT]"

**Why Day 21?**
- The first habit has established a foundation
- User has proven consistency
- Cognitive load is reduced as first habit becomes more automatic

**Example Stack:**
- First habit: "After I wake up, I will meditate for 5 minutes"
- Second habit: "After I meditate, I will write in my journal"

### 3.6 Focus Mode

When completing a habit, users enter a distraction-free Focus Mode with:
- Large timer display
- Progress toward target (for timed/increment habits)
- Minimal UI
- No notifications

**Resistance Mode (for Reduction habits):**
- Green color scheme (positive framing)
- Timer counts UP (tracking time resisted)
- Encouraging messages
- "I gave in" vs "Still resisting" options

---

## 4. User Flows

### 4.1 Onboarding Flow

```
1. Welcome Screen
   - App icon + name
   - Science-backed messaging
   - "Begin Journey" CTA

2. Habit Type Selection
   - 4 type cards with icons
   - Brief description of each
   - Select one

3. Habit Definition
   - Name input
   - Metric configuration (if applicable)
   - Time of day preference

4. Implementation Intention
   - "After I ___" input
   - "I will [habit name]" display
   - Pro tip about linking to existing routines

5. Journey Start
   - Transition to home screen
   - Day 1 begins
```

### 4.2 Daily Completion Flow

```
1. Home Screen
   - Journey progress
   - Habit card with action button
   - Grace days indicator

2. Focus Mode (if timed/increment/reduction)
   - Timer counting up
   - Progress toward target
   - Cancel / Done buttons

3. Completion Screen
   - Celebration message
   - Stats (days, time)
   - Share card
   - Continue button

4. Return to Home
   - Habit card shows completed state
   - Journey progress updated
```

### 4.3 Missed Day Flow

```
1. User opens app after missing yesterday

2. Grace Day Modal appears
   - Acknowledge the miss without guilt
   - Two options:
     a. "Use a grace day" - preserves streak
     b. "Accept & continue" - moves forward

3. Return to home
   - Today's habit ready to complete
```

---

## 5. Platform Specifications

### 5.1 iOS Implementation

**Framework:** SwiftUI + Combine

**Key Technologies:**
- WidgetKit for home/lock screen widgets
- ActivityKit for Live Activities
- CloudKit for sync (optional)
- Core Haptics for tactile feedback

**Design System:**
- SF Pro font family
- iOS semantic colors
- 44pt minimum touch targets
- Large titles that collapse on scroll
- Sheet-style modals

**Widget Surfaces:**
- Home Screen: Small (2x2), Medium (4x2)
- Lock Screen: Inline, Circular
- Live Activity: Compact, Expanded

### 5.2 Android Implementation

**Framework:** Jetpack Compose + Kotlin Coroutines

**Key Technologies:**
- Glance for widgets
- WorkManager for background tasks
- Firebase for sync
- Material 3 Haptics

**Design System:**
- Roboto font family
- Material You dynamic colors
- 48dp minimum touch targets
- Large top app bar
- Bottom navigation

**Widget Surfaces:**
- Home Screen: Multiple sizes via Glance
- Always-on Display: Minimal widget

---

## 6. Data Architecture

### 6.1 Local Storage

**iOS:** Core Data with the following entities:
- `Habit`: Habit definition
- `DailyRecord`: Completion records
- `Journey`: 66-day journey tracking
- `Achievement`: Milestones unlocked

**Android:** Room Database with equivalent tables

### 6.2 Cloud Sync

**Recommended: Firebase Firestore**

```
users/{userId}/
├── habits/{habitId}
│   ├── name: string
│   ├── type: 'binary' | 'timed' | 'increment' | 'reduction'
│   ├── trigger: string
│   ├── time: string
│   ├── startValue: number
│   ├── changeValue: number
│   ├── createdAt: timestamp
│   └── updatedAt: timestamp
│
├── records/{date_habitId}
│   ├── habitId: string
│   ├── date: string (YYYY-MM-DD)
│   ├── completed: boolean
│   ├── completedAt: timestamp
│   ├── focusSeconds: number
│   └── updatedAt: timestamp
│
├── journeys/{journeyId}
│   ├── habitId: string
│   ├── startDate: string
│   ├── currentDay: number
│   ├── graceDaysUsed: number
│   ├── status: 'active' | 'completed' | 'abandoned'
│   └── updatedAt: timestamp
│
└── achievements/{achievementId}
    ├── type: string
    ├── unlockedAt: timestamp
    └── habitId: string (optional)
```

### 6.3 Offline-First Strategy

1. All writes go to local database first
2. Sync manager queues changes
3. When online, push to Firestore
4. Pull remote changes and merge
5. Conflict resolution: last-write-wins with field-level updates

---

## 7. Analytics & Metrics

### 7.1 Key Metrics

| Metric | Description | Target |
|--------|-------------|--------|
| **Day 1 Completion** | Users who complete habit on day 1 | >80% |
| **Day 7 Retention** | Users active after 1 week | >60% |
| **Day 21 Retention** | Users who reach milestone | >40% |
| **Journey Completion** | Users who complete 66 days | >25% |
| **Second Habit Adoption** | Users who add stacked habit | >50% of day 21 |

### 7.2 Events to Track

- `onboarding_started`
- `onboarding_completed`
- `habit_created` (with type)
- `focus_started`
- `focus_completed` (with duration)
- `focus_cancelled`
- `grace_day_used`
- `milestone_reached` (7, 21, 42, 66)
- `second_habit_added`
- `share_initiated`

---

## 8. Monetization (Future)

### Potential Revenue Streams

1. **Premium Features:**
   - Additional habit slots (3rd, 4th habit)
   - Advanced analytics
   - Custom themes
   - Export data

2. **Subscription Model:**
   - Free: 1 habit, basic features
   - Pro ($4.99/mo): Unlimited habits, widgets, sync

3. **One-Time Purchase:**
   - Lifetime unlock: $29.99

*Note: Core functionality should always remain free. Constraints are features, not paywalls.*

---

## 9. Accessibility Requirements

### iOS
- Full VoiceOver support
- Dynamic Type support (all text sizes)
- Reduce Motion alternative animations
- Bold Text support
- Increase Contrast support

### Android
- TalkBack support
- Font scaling support
- High contrast theme
- Touch target minimum 48dp
- Content descriptions for all images

### General
- Color contrast ratio ≥ 4.5:1 for text
- No color-only indicators
- Keyboard navigation support
- Screen reader announcements for state changes

---

## 10. Security & Privacy

### Data Handling
- All data encrypted at rest (AES-256)
- All data encrypted in transit (TLS 1.3)
- No habit data shared with third parties
- User can delete all data at any time

### Permissions Required
- **Notifications**: For reminders (optional)
- **HealthKit/Google Fit**: For fitness habits (optional, future)

### Privacy Policy Requirements
- Clear disclosure of data collection
- GDPR/CCPA compliant
- No selling of user data

---

## 11. Roadmap

### Phase 1: MVP (Months 1-2)
- [ ] iOS app with all core features
- [ ] Android app with all core features
- [ ] Local persistence only
- [ ] Basic widgets

### Phase 2: Cloud & Social (Months 3-4)
- [ ] Cloud sync implementation
- [ ] Social sharing features
- [ ] Advanced widgets (Live Activities, Lock Screen)

### Phase 3: Expansion (Months 5-6)
- [ ] Apple Watch app
- [ ] Wear OS app
- [ ] Habit insights and analytics
- [ ] Premium features

### Phase 4: Growth (Months 7+)
- [ ] Community features
- [ ] Habit templates
- [ ] Integration with other apps
- [ ] Web companion

---

## 12. Success Criteria

### Launch Criteria
- [ ] All core features functional
- [ ] No critical bugs
- [ ] Accessibility audit passed
- [ ] Performance benchmarks met (<3s cold start)
- [ ] App Store guidelines compliant

### 6-Month Goals
- 10,000+ downloads
- 4.5+ star rating
- 25%+ day-21 retention
- 15%+ journey completion rate

---

## Appendix A: Research Citations

1. Lally, P., van Jaarsveld, C. H. M., Potts, H. W. W., & Wardle, J. (2010). How are habits formed: Modelling habit formation in the real world. *European Journal of Social Psychology*, 40(6), 998-1009.

2. Gollwitzer, P. M. (1999). Implementation intentions: Strong effects of simple plans. *American Psychologist*, 54(7), 493-503.

3. Wood, W., & Neal, D. T. (2007). A new look at habits and the habit-goal interface. *Psychological Review*, 114(4), 843-863.

4. Clear, J. (2018). *Atomic Habits: An Easy & Proven Way to Build Good Habits & Break Bad Ones*. Penguin.

5. Fogg, B. J. (2019). *Tiny Habits: The Small Changes That Change Everything*. Houghton Mifflin Harcourt.

---

## Appendix B: Competitor Analysis

| App | Focus | Strength | Weakness |
|-----|-------|----------|----------|
| **Streaks** | Multiple habits | Clean UI | No constraint, easy to overwhelm |
| **Habitica** | Gamification | Fun RPG elements | Complex, gaming focus |
| **Fabulous** | Routines | Beautiful design | Subscription heavy |
| **Loop** | Simple tracking | Free, open source | Minimal features |
| **Focusmo** | Single task | One-at-a-time | Mac only, task not habit |

**OneFocus Differentiation:**
- Science-based 66-day journey (vs arbitrary timelines)
- Enforced constraints (vs optional focus modes)
- Habit-type aware tracking (vs one-size-fits-all)
- Grace days (vs streak-breaking punishment)
- Implementation intentions (vs basic reminders)
