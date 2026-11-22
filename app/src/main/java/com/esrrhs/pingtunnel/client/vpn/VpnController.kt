package com.esrrhs.pingtunnel.client.vpn

import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.core.content.ContextCompat
import com.esrrhs.pingtunnel.client.MainActivity

/** UI 层启停 VPN 的唯一入口；状态请读 [VpnStateHolder]。 */
object VpnController {

    fun toggle(context: Context, openAppForConsent: (Intent) -> Unit) {
        if (VpnTileState.of(VpnStateHolder.state.value) == VpnTileState.Tile.ACTIVE) {
            disconnect(context)
        } else if (VpnService.prepare(context) == null) {
            connect(context)
        } else {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra(MainActivity.EXTRA_CONNECT, true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            openAppForConsent(intent)
        }
    }

    fun connect(context: Context) {
        val intent = Intent(context, PingtunnelVpnService::class.java)
            .setAction(PingtunnelVpnService.ACTION_CONNECT)
        ContextCompat.startForegroundService(context, intent)
    }

    fun disconnect(context: Context) {
        val intent = Intent(context, PingtunnelVpnService::class.java)
            .setAction(PingtunnelVpnService.ACTION_DISCONNECT)
        ContextCompat.startForegroundService(context, intent)
    }
}
