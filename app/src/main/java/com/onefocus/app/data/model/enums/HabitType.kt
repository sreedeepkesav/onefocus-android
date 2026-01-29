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

    val example: String
        get() = when (this) {
            BINARY -> "e.g. Meditate, Journal"
            TIMED -> "e.g. Read for 30 min"
            INCREMENT -> "e.g. 5 → 50 pushups"
            REDUCTION -> "e.g. 10 → 0 cigarettes"
            REPEATING -> "e.g. Drink 8 glasses"
        }

    val requiresTrigger: Boolean
        get() = this != REPEATING
}
