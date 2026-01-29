# Onward - Product Decisions & Strategy

## App Name: **Onward**
*Moving forward, one habit at a time.*

---

## Executive Summary

Based on multi-perspective analysis (Product Manager, Design Director, Investor), this document captures key product decisions, UX specifications, business strategy, and implementation priorities.

---

## 1. Core Philosophy (Validated)

| Principle | Decision | Rationale |
|-----------|----------|-----------|
| One habit at a time | **KEEP** | Key differentiator, scientifically grounded |
| 66-day journey | **KEEP** (but hide initially) | Don't lead with "66 days" - it's intimidating |
| 3 grace days | **KEEP** | Rename to "flex days" for warmer language |
| Trigger-based habits | **KEEP** | Real habit science, most apps miss this |
| $1/year pricing | **REVISIT** | See Business Model section |

---

## 2. Habit Types - SIMPLIFIED

### Decision: Consolidate 5 types to 3

**Before (Too Complex):**
- Binary, Timed, Increment, Reduction, Repeating

**After (User-Friendly):**

| Type | Icon | Description | Covers |
|------|------|-------------|--------|
| **Yes/No** | ✓ | "Did you do it today?" | Binary |
| **Duration** | ⏱ | "For how long?" | Timed |
| **Count** | 🔢 | "How many times?" | Increment, Reduction, Repeating |

**Count type has a "direction" flag:**
- Build up (5 → 50 pushups)
- Cut down (10 → 0 cigarettes)
- Daily target (8 glasses of water)

---

## 3. Onboarding Flow

### Screen 1: Welcome
```
"One habit. 66 days. Life changed."
[Get Started]
```
- No logo fanfare
- Dark background, centered white text
- Single CTA

### Screen 2: Pick Your Habit
- Show 6 curated popular habits with icons
- "Or create your own" as secondary option
- **NO** explanation of habit types - infer from selection

**Suggested habits:**
- Meditate (Yes/No)
- Read (Duration)
- Exercise (Duration)
- Drink water (Count)
- Journal (Yes/No)
- Walk (Duration)

### Screen 3: When Will You Do This?
```
"After I ___, I will [habit name]"
```
- Common triggers dropdown: wake up, morning coffee, lunch, get home, dinner, before bed
- Time picker for notification
- For "Count" habits, offer "Throughout the day" option

### Screen 4: Meet Your Safety Net
```
"Life happens. You get 3 flex days -
miss a day without breaking your streak."
[Visual of 3 hearts]
```

### Screen 5: Enable Notifications
```
"Your first check-in is tomorrow. We'll remind you."
[Enable Notifications] ← Critical CTA
```

### What's NOT in Onboarding
- 66-day science explanation (reveal at day 7)
- Second habit unlock info (reveal at day 14)
- Habit type distinctions (infer from input)

---

## 4. Settings Page

### Essential Settings Only

**Habit Settings**
- Edit habit name
- Edit trigger
- Change reminder time
- Pause habit (with warning)

**Appearance**
- Dark / Light / System (3-segment toggle)
- That's it. No themes.

**Notifications**
- Daily reminder: On/Off + Time
- Milestone celebrations: On/Off
- Streak warnings: On/Off

**Data**
- Export habit history (CSV)
- Delete all data

**About**
- App version
- Privacy policy
- Terms
- "Made with care"

**Support**
- Contact / Feedback (email composer)
- Rate app

### What to AVOID
- Sound/haptic customization
- Widget appearance settings
- Social sharing toggles
- Backup/sync options
- Language selection (follow system)

---

## 5. Support Email & Legal

### Support Email
```
support@onwardhabit.app
```

### Privacy Policy Must Include
- Data stored locally only (no cloud sync)
- No personal data collected
- No analytics/tracking (or minimal, disclosed)
- Notification permission usage
- Data deletion instructions

### Terms of Service
- Subscription terms ($1/year auto-renewal)
- Refund policy (App Store handles)
- Age requirements
- Limitation of liability

---

## 6. Payment Page

### When to Show Payment

**Recommended: Post-First-Journey**
- Full app is FREE for first 66-day journey
- After completing (or abandoning) first habit, show payment
- This proves value before asking for money

### Payment Screen Design

```
┌─────────────────────────────────────────┐
│                                         │
│        Continue Your Journey            │
│                                         │
│     $1/year. Unlimited habits.          │
│            No tricks.                   │
│                                         │
│   "That's less than a penny per week"   │
│                                         │
│        ┌─────────────────────┐          │
│        │  Unlock for $1/year │          │
│        └─────────────────────┘          │
│                                         │
│          Restore Purchase               │
│                                         │
│    Cancel anytime in Settings           │
│                                         │
└─────────────────────────────────────────┘
```

### What's Included
- Unlimited sequential habits
- All habit types
- Full history
- Home screen widgets
- Future features included

### What to AVOID
- Comparison tables
- "Save X%" messaging
- Fake urgency
- Free trial language
- Multiple plan options

---

## 7. Business Model Considerations

### The $1/year Problem

| At $1/year | Users Needed | Reality |
|------------|--------------|---------|
| $1M ARR | 1,000,000 paying | Top 0.1% of apps |
| $100K ARR | 100,000 paying | Still very hard |

### Recommended Options

**Option A: Keep $1/year + Add Premium Tier**
- $1/year: Core single-habit tracking
- $9.99/year: Multiple habits, analytics, widget customization

**Option B: Lifetime Model**
- $1/year OR $9.99 lifetime
- Appeals to minimalist ethos
- Front-loads revenue

**Option C: B2B Play (Best for Scale)**
- Consumer app at $1/year (loss leader)
- B2B/Enterprise at $3-5/employee/year
- Where the real money is (see Calm's model)

### Future Monetization (Without Betraying Ethos)
1. Partnerships with therapists/coaches
2. "The Onward Method" ebook ($19.99)
3. White-label licensing to other apps

---

## 8. Widget Specifications

### Small Widget (2x2) - PRIMARY

```
┌──────────────────┐
│ Day 23       ○   │
│                  │
│ Meditate         │
│                  │
│ 🔥 12 day streak │
└──────────────────┘
```

- Habit icon (based on type)
- Current day number
- Completion status (empty circle or checkmark)
- Tap opens app to today's habit

### Medium Widget (4x2) - SECONDARY

```
┌───────────────────────────────────────┐
│ Day 23 of 66                      ✓   │
│ ████████████░░░░░░░░░░░░░░░░░░░░░░░░ │
│                                       │
│ Meditate                              │
│ After I pour my coffee               │
│                                       │
│ ❤️ ❤️ ❤️  3 flex days remaining        │
└───────────────────────────────────────┘
```

- Progress bar (66 days)
- Habit name and trigger
- Flex days indicator
- Today's status

### Widget Design Principles
- Match app's dark aesthetic by default
- Respect system appearance setting
- High contrast for glanceability
- Minimal color: white/gray + single accent
- Update on completion + midnight

---

## 9. Notification Strategy

### Daily Reminder
- Single notification at user-selected time
- Copy varies:
  - Standard: "Time for [habit name]"
  - With trigger: "After [trigger], remember to [habit]"
  - Late in day: "[Habit name] is waiting"

### Milestone Notifications
- Day 7: "One week down. 59 to go."
- Day 21: "Three weeks. Neural pathways forming."
- Day 33: "Halfway there."
- Day 50: "16 days left. You can see the finish line."
- Day 66: Celebration with custom sound/haptic

### Flex Day Notifications
- First used: "Flex day 1 of 3 used. That's what they're for."
- Second used: "2 flex days used. One remains."
- Third used: "Last flex day used. Every day counts now."

### What to AVOID
- Multiple daily reminders
- Shame-based messaging ("You're falling behind!")
- Notifications after habit is complete
- Weekly summary notifications

---

## 10. Key Screens & States

### Empty State (After 66 Days)

**Celebration Screen:**
```
┌─────────────────────────────────────────┐
│                                         │
│                 66                      │
│                                         │
│        You built a habit.               │
│                                         │
│   "Meditate is now part of who you are" │
│                                         │
│         [Start a new habit]             │
│            Take a break                 │
│                                         │
└─────────────────────────────────────────┘
```

### Failed State (All Flex Days Used + Day Missed)

**Compassionate Failure Screen:**
```
┌─────────────────────────────────────────┐
│                                         │
│   This round didn't work out.           │
│   That happens.                         │
│                                         │
│   What matters is you tried.            │
│   And you can try again.                │
│                                         │
│   You made it to Day 23.                │
│   That's 23 days more than not trying.  │
│                                         │
│    [Start fresh with same habit]        │
│      Try something different            │
│                                         │
└─────────────────────────────────────────┘
```

### Day 21 - Second Habit Unlock
```
┌─────────────────────────────────────────┐
│                                         │
│            Milestone!                   │
│                                         │
│   You've been consistent for 3 weeks.   │
│                                         │
│   You've unlocked the ability to        │
│   build a second habit in parallel.     │
│                                         │
│         [Add second habit]              │
│          Continue with one              │
│                                         │
└─────────────────────────────────────────┘
```

---

## 11. Metrics to Track

### North Star Metric
**Day 21 Retention Rate** - % of users who check in on day 21

### Primary Metrics

| Metric | Target | Why |
|--------|--------|-----|
| D1 Retention | >60% | Onboarding worked? |
| D7 Retention | >35% | Past novelty phase |
| D21 Retention | >20% | Habit forming |
| D66 Completion | >10% | Journey complete |
| Flex Days Used (avg) | 1-2 | Users struggling? |
| Notification Enable Rate | >70% | Critical for retention |

### Secondary Metrics
- Time to first check-in (same day = good)
- Habit swap rate before day 7
- Share rate at milestones
- App opens per day (target: 1.2)

---

## 12. Feature Prioritization

### MVP (v1.0) - Launch Blockers
1. Push notifications with trigger-time awareness
2. Progress visualization (journey map)
3. Simplified habit types (3 instead of 5)
4. Failure state design
5. 5-screen onboarding

### v1.1 - Week 1 Post-Launch
1. Habit templates/examples
2. Share-to-Stories for milestones
3. Accountability partner (simple version)
4. Data export (CSV)

### v1.2 - Month 1
1. Focus Mode (app blocking)
2. Apple Watch / Wear OS complications
3. Lock Screen widgets (iOS 16+)
4. End-of-journey summary

### v2.0 - Future
1. B2B dashboard for therapists/coaches
2. Family sharing
3. Apple Health / Google Fit integration

---

## 13. Critical UX Decisions

### Default to Light Mode?
**Decision: NO - Keep dark default**
- Our target users are tech-savvy
- Dark mode is increasingly preferred
- Differentiates from "wellness" apps like Calm/Headspace
- BUT offer easy toggle in settings

### Allow Habit Swap?
**Decision: YES - Once before Day 7**
- Reduces "wrong choice" anxiety
- After Day 7, locked in
- Show warning when swapping

### What Happens at Day 67?
**Decision: "Habit Graduation"**
- Option 1: Start a new 66-day journey
- Option 2: Keep tracking (maintenance mode)
- Option 3: "Graduate" the habit (stop tracking, it's automatic now)

---

## 14. Red Flags to Monitor

1. **High swap rate before day 7** → Poor habit selection UX
2. **Low notification enable rate** → Onboarding problem
3. **All 3 flex days used by day 10** → Users struggling, need intervention
4. **High day 7 drop-off** → Novelty wore off, need better hooks
5. **Low D66 completion** → Journey is too long or motivation dies

---

## Summary: What Makes Onward Different

| Most Habit Apps | Onward |
|-----------------|--------|
| Multiple habits from day 1 | **One habit at a time** |
| Streak-based guilt | **Flex days (compassionate)** |
| Generic reminders | **Trigger-based ("After I...")** |
| Same UI for all habits | **Type-specific UIs** |
| Feature bloat | **Radical minimalism** |
| $30-60/year | **$1/year** |

**The positioning:** *"The app that only lets you build one habit."*
