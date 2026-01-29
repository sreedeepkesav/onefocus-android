# Internationalization & Localization Guide

Onward should be available in multiple languages. This document outlines the implementation strategy.

---

## 1. Language Priority

### Tier 1: Launch Languages
| Language | Code | Market |
|----------|------|--------|
| English | en | Global |
| Spanish | es | Americas, Spain |
| Portuguese (Brazil) | pt-BR | Brazil |
| Simplified Chinese | zh-Hans | China |

### Tier 2: Add Within 3-6 Months
| Language | Code | Market |
|----------|------|--------|
| German | de | Germany, Austria, Switzerland |
| French | fr | France, Canada, Africa |
| Japanese | ja | Japan |
| Korean | ko | South Korea |
| Hindi | hi | India |
| Arabic | ar | Middle East, North Africa |

### Tier 3: Add Within 6-12 Months
| Language | Code | Market |
|----------|------|--------|
| Italian | it | Italy |
| Dutch | nl | Netherlands, Belgium |
| Russian | ru | Russia |
| Turkish | tr | Turkey |
| Polish | pl | Poland |
| Indonesian | id | Indonesia |
| Thai | th | Thailand |
| Vietnamese | vi | Vietnam |

---

## 2. iOS Implementation

### String Catalogs (Xcode 15+)

**Localizable.xcstrings** (JSON-based):
```json
{
  "sourceLanguage" : "en",
  "strings" : {
    "day_count" : {
      "localizations" : {
        "en" : {
          "stringUnit" : {
            "state" : "translated",
            "value" : "Day %lld of 66"
          }
        },
        "es" : {
          "stringUnit" : {
            "state" : "translated",
            "value" : "Día %lld de 66"
          }
        }
      }
    }
  }
}
```

### Usage in SwiftUI

```swift
// Simple string
Text("start_habit")  // Automatically looks up localization

// String with arguments
Text("day_count \(currentDay)")

// Explicit localization
Text(String(localized: "welcome_message"))
```

### Pluralization

```swift
// In String Catalog, set up plural variations:
// "flex_days_remaining" with plural rules

// English:
// one: "1 flex day remaining"
// other: "%lld flex days remaining"

// Usage:
Text("flex_days_remaining \(count)")
```

### Date/Time Formatting

```swift
// Always use formatters, never hardcode formats
let formatter = DateFormatter()
formatter.dateStyle = .medium
formatter.timeStyle = .none
formatter.locale = Locale.current

Text(formatter.string(from: date))

// Or with SwiftUI
Text(date, style: .date)

// Relative formatting
Text(date, style: .relative)  // "2 days ago"
```

### Number Formatting

```swift
// Streak count
Text(streak, format: .number)

// Percentage
Text(progress, format: .percent)

// Duration
let formatter = DateComponentsFormatter()
formatter.allowedUnits = [.minute, .second]
formatter.unitsStyle = .abbreviated
Text(formatter.string(from: duration)!)
```

### RTL (Right-to-Left) Support

```swift
// Use leading/trailing instead of left/right
HStack {
    Text("Label")
    Spacer()
    Image(systemName: "chevron.forward")
}

// For icons that should flip
Image(systemName: "arrow.right")
    .flipsForRightToLeftLayoutDirection(true)

// Check layout direction
@Environment(\.layoutDirection) var layoutDirection
```

---

## 3. Android Implementation

### strings.xml Structure

```
res/
├── values/
│   └── strings.xml          // English (default)
├── values-es/
│   └── strings.xml          // Spanish
├── values-pt-rBR/
│   └── strings.xml          // Portuguese (Brazil)
├── values-zh-rCN/
│   └── strings.xml          // Simplified Chinese
└── values-ar/
    └── strings.xml          // Arabic (RTL)
```

### strings.xml

```xml
<!-- values/strings.xml (English) -->
<resources>
    <string name="app_name">Onward</string>
    <string name="day_count">Day %1$d of 66</string>
    <string name="start_habit">Start %1$s</string>
    <string name="trigger_after">After I %1$s</string>

    <!-- Plurals -->
    <plurals name="flex_days_remaining">
        <item quantity="one">%d flex day remaining</item>
        <item quantity="other">%d flex days remaining</item>
    </plurals>
</resources>

<!-- values-es/strings.xml (Spanish) -->
<resources>
    <string name="day_count">Día %1$d de 66</string>
    <string name="start_habit">Comenzar %1$s</string>
    <string name="trigger_after">Después de %1$s</string>

    <plurals name="flex_days_remaining">
        <item quantity="one">%d día flexible restante</item>
        <item quantity="other">%d días flexibles restantes</item>
    </plurals>
</resources>
```

### Usage in Compose

```kotlin
// Simple string
Text(stringResource(R.string.app_name))

// String with arguments
Text(stringResource(R.string.day_count, currentDay))

// Plurals
Text(pluralStringResource(
    R.plurals.flex_days_remaining,
    count,
    count
))
```

### Date/Time Formatting

```kotlin
// Use DateTimeFormatter with locale
val formatter = DateTimeFormatter
    .ofLocalizedDate(FormatStyle.MEDIUM)
    .withLocale(Locale.getDefault())

Text(date.format(formatter))

// Relative time
val relativeFormatter = DateUtils.getRelativeTimeSpanString(
    timestamp,
    System.currentTimeMillis(),
    DateUtils.DAY_IN_MILLIS
)
```

### Number Formatting

```kotlin
// Streak count
val numberFormat = NumberFormat.getInstance(Locale.getDefault())
Text(numberFormat.format(streak))

// Percentage
val percentFormat = NumberFormat.getPercentInstance(Locale.getDefault())
Text(percentFormat.format(progress))
```

### RTL Support

```xml
<!-- AndroidManifest.xml -->
<application
    android:supportsRtl="true">
```

```kotlin
// Use start/end instead of left/right
Row(
    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
) { }

// Check layout direction
val layoutDirection = LocalLayoutDirection.current
if (layoutDirection == LayoutDirection.Rtl) {
    // RTL-specific logic
}
```

---

## 4. Specific Challenges

### "After I ___" Trigger Phrase

This phrase structure varies by language:

| Language | Pattern | Example |
|----------|---------|---------|
| English | "After I [trigger], I will [habit]" | "After I pour coffee, I will meditate" |
| Spanish | "Después de [trigger], voy a [habit]" | "Después de servir café, voy a meditar" |
| German | "Nachdem ich [trigger], werde ich [habit]" | "Nachdem ich Kaffee eingegossen habe, werde ich meditieren" |
| Japanese | "[trigger]の後、[habit]する" | "コーヒーを注いだ後、瞑想する" |
| Arabic | "بعد [trigger]، سأقوم بـ [habit]" | "بعد صب القهوة، سأقوم بالتأمل" |

**Solution: Use flexible templates**

```swift
// iOS
// en: "After I %@"
// de: "Nachdem ich %@"
// ja: "%@の後"

let triggerText = String(localized: "trigger_pattern \(trigger)")
```

```kotlin
// Android
// en: "After I %1$s"
// de: "Nachdem ich %1$s"
// ja: "%1$sの後"

val triggerText = stringResource(R.string.trigger_pattern, trigger)
```

### Date Formats

| Region | Short Date | Week Start |
|--------|------------|------------|
| US | MM/DD/YYYY | Sunday |
| UK/EU | DD/MM/YYYY | Monday |
| Japan | YYYY/MM/DD | Sunday |
| Middle East | DD/MM/YYYY | Saturday |

**Always use system formatters:**
```swift
// iOS - Respects user's locale automatically
Text(date, format: .dateTime.day().month())
```

```kotlin
// Android - Respects user's locale automatically
DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
```

### Habit Templates

Habit suggestions must be localized with culturally relevant examples:

```swift
// en
"Meditate", "Read", "Exercise", "Drink water"

// ja (Japan)
"瞑想する", "読書する", "運動する", "水を飲む"

// ar (Arabic)
"تأمل", "قراءة", "تمرين", "شرب الماء"
```

---

## 5. Content to Localize

### App Store / Play Store

- App name (usually keep "Onward")
- Subtitle/short description
- Full description
- Keywords
- Screenshots (consider localized text overlays)
- What's New (release notes)

### In-App Strings

| Category | Examples |
|----------|----------|
| Navigation | "Home", "Settings", "Analytics" |
| Actions | "Start", "Complete", "Skip" |
| Feedback | "Great job!", "Keep going!" |
| Errors | "Something went wrong", "Please try again" |
| Onboarding | All 5 screens |
| Settings | All labels and descriptions |
| Notifications | All notification titles and bodies |
| Widgets | Status text |

### Support Content

- FAQ
- Privacy Policy
- Terms of Service
- Help articles

---

## 6. Workflow Recommendations

### Translation Management Systems

| Tool | Pros | Cons | Price |
|------|------|------|-------|
| **Lokalise** | Great iOS/Android support, CLI | $$$ | From $120/mo |
| **Crowdin** | Open source friendly | Learning curve | Free tier available |
| **Phrase** | Enterprise features | Complex | $$$ |
| **POEditor** | Simple, affordable | Basic features | From $15/mo |

### Recommended Workflow

1. **Development** - Use English as base language
2. **Export** - Extract strings to TMS via CLI
3. **Translate** - Professional translators or community
4. **Review** - Native speaker review
5. **Import** - Pull translations back to project
6. **Test** - Test with pseudo-localization first

### Pseudo-Localization

Test layout before real translations:

```
"Start habit" → "[!!Śţàŕţ ĥàƀíţ!!]"
```

This helps catch:
- Text truncation
- Layout overflow
- Hardcoded strings
- Concatenation issues

### CI/CD Integration

```yaml
# GitHub Actions example
- name: Sync translations
  run: |
    lokalise2 file download \
      --project-id ${{ secrets.LOKALISE_PROJECT_ID }} \
      --token ${{ secrets.LOKALISE_TOKEN }} \
      --format strings \
      --original-filenames=true \
      --directory-prefix=%LANG_ISO%
```

---

## 7. Testing Localized Builds

### iOS

```bash
# Test with specific locale
xcrun simctl boot "iPhone 15 Pro"
xcrun simctl spawn booted defaults write com.apple.springboard \
  AppleLanguages "(es)"
xcrun simctl spawn booted defaults write com.apple.springboard \
  AppleLocale "es_ES"
```

### Android

```kotlin
// In tests, set locale programmatically
val locale = Locale("es", "ES")
Locale.setDefault(locale)
val config = context.resources.configuration
config.setLocale(locale)
context.createConfigurationContext(config)
```

### Checklist Per Language

- [ ] All strings translated (no English fallbacks)
- [ ] Plurals work correctly
- [ ] Dates format correctly
- [ ] Numbers format correctly
- [ ] RTL layout works (if applicable)
- [ ] Text doesn't overflow/truncate
- [ ] Screenshots updated
- [ ] App Store listing updated

---

## 8. Key Files to Localize

### iOS
```
OneFocus/
├── Localizable.xcstrings       # Main strings
├── InfoPlist.xcstrings         # App name, permissions
└── Widget/
    └── Localizable.xcstrings   # Widget strings
```

### Android
```
app/src/main/res/
├── values/strings.xml           # Default (English)
├── values-es/strings.xml        # Spanish
├── values-pt-rBR/strings.xml    # Portuguese (Brazil)
├── values-zh-rCN/strings.xml    # Chinese (Simplified)
└── ...
```

---

## Summary

1. **Start with Tier 1 languages** at launch
2. **Use platform formatters** for dates/numbers
3. **Never concatenate** translated strings
4. **Test with pseudo-localization** early
5. **Use a TMS** for workflow efficiency
6. **Plan for RTL** from the beginning
