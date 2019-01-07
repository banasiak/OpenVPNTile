package com.banasiak.android.openvpntile

import android.content.Context
import android.content.Intent
import android.net.Uri

fun isOpenVpnConnectInstalled(context: Context): Boolean {
  return context.packageManager
    .getLaunchIntentForPackage(QuickSettingsService.OPENVPN_PACKAGE) != null
}

fun getOpenVpnLaunchIntent(context: Context): Intent {
  var intent = context.packageManager
    .getLaunchIntentForPackage(QuickSettingsService.OPENVPN_PACKAGE)
  if (intent == null) {
    intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("market://details?id=${QuickSettingsService.OPENVPN_PACKAGE}")
  }
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  return intent
}