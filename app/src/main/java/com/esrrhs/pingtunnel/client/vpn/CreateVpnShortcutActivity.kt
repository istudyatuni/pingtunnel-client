package com.esrrhs.pingtunnel.client.vpn

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.esrrhs.pingtunnel.client.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateVpnShortcutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.action != Intent.ACTION_CREATE_SHORTCUT) {
            finish()
            return
        }

        val shortcut = ShortcutInfo.Builder(this, "vpn_toggle")
            .setShortLabel(getString(R.string.tile_label))
            .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
            .setIntent(Intent(this, VpnShortcutActivity::class.java).setAction(Intent.ACTION_MAIN))
            .build()

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                getSystemService(ShortcutManager::class.java)?.createShortcutResultIntent(shortcut)
            }
            if (result != null) setResult(RESULT_OK, result)
            finish()
        }
    }
}
