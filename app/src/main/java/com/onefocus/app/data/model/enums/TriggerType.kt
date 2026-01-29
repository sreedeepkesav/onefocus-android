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
