package com.esrrhs.pingtunnel.client.vpn

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.esrrhs.pingtunnel.client.R

class PingtunnelWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        val intent = Intent(context, PingtunnelWidgetProvider::class.java).setAction(ACTION_TOGGLE)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val views = RemoteViews(context.packageName, R.layout.pingtunnel_widget).apply {
            setOnClickPendingIntent(R.id.widget_toggle, pendingIntent)
        }
        manager.updateAppWidget(appWidgetIds, views)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_TOGGLE) {
            VpnController.toggle(context, context::startActivity)
        } else {
            super.onReceive(context, intent)
        }
    }

    private companion object {
        const val ACTION_TOGGLE = "com.esrrhs.pingtunnel.client.action.WIDGET_TOGGLE"
    }
}
