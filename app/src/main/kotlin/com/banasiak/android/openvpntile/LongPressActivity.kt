package com.banasiak.android.openvpntile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LongPressActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var intent = packageManager.getLaunchIntentForPackage(QuickSettingsService.OPENVPN_PACKAGE)
    if (intent == null) {
      intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse("market://details?id=${QuickSettingsService.OPENVPN_PACKAGE}")
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    finish()
  }

}