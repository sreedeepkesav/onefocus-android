package com.onefocus.app.core.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding

class OneFocusWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Responsive(
        setOf(SmallWidgetSize, MediumWidgetSize)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val widgetData = WidgetDataProvider(context).getWidgetData()

        provideContent {
            WidgetContent(widgetData)
        }
    }

    @Composable
    private fun WidgetContent(data: WidgetData) {
        androidx.glance.layout.Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SmallWidgetContent(data)
        }
    }

    companion object {
        val SmallWidgetSize = androidx.glance.appwidget.SizeMode.Exact(120.dp, 120.dp)
        val MediumWidgetSize = androidx.glance.appwidget.SizeMode.Exact(260.dp, 120.dp)
    }
}

class OneFocusWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = OneFocusWidget()
}
