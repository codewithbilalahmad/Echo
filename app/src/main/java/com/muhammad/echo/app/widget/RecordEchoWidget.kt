package com.muhammad.echo.app.widget

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import com.muhammad.echo.MainActivity
import com.muhammad.echo.R

const val ACTION_CREATE_ECHO = "com.muhammad.CREATE_ECHO"

class RecordEchoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = RecordEchoWidget()
}

class RecordEchoWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val recordNewEcho = context.getString(R.string.record_new_echo)
        provideContent {
            GlanceTheme {
                Column(modifier = GlanceModifier.clickable {
                    val intent = Intent(context, MainActivity::class.java).also {
                        it.data = "https://echo.com/echos/true".toUri()
                        it.action = ACTION_CREATE_ECHO
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    context.startActivity(intent)
                }) {
                    Image(
                        provider = ImageProvider(R.drawable.widget),
                        contentDescription = recordNewEcho
                    )
                }
            }
        }
    }
}