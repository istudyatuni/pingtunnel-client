package com.esrrhs.pingtunnel.client.vpn

import android.app.Activity

class VpnShortcutActivity : Activity() {

    override fun onResume() {
        super.onResume()
        VpnController.toggle(this, ::startActivity)
        finish()
    }
}
