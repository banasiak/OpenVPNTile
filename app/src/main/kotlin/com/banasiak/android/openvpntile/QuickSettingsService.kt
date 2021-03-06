package com.banasiak.android.openvpntile

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast

class QuickSettingsService : TileService() {
  companion object {
    const val OPENVPN_PACKAGE = "net.openvpn.openvpn"
    const val OPENVPN_ACTIVITY = "net.openvpn.unified.MainActivity"
    const val VPN_PROFILE_KEY = "vpn_profile"
    const val VPN_TYPE_KEY = "vpn_type"
    const val EMPTY_STRING = ""
  }

  private lateinit var prefs: SharedPreferences
  private var vpnProfile: String
    get() = prefs.getString(VPN_PROFILE_KEY, EMPTY_STRING)!!
    set(value) {
      prefs.edit().putString(VPN_PROFILE_KEY, value).apply()
    }
  private var vpnType: String
    get() = prefs.getString(VPN_TYPE_KEY, EMPTY_STRING)!!
    set(value) {
      prefs.edit().putString(VPN_TYPE_KEY, value).apply()
    }

  override fun onBind(intent: Intent?): IBinder? {
    prefs = applicationContext
      .getSharedPreferences(applicationContext.packageName, Context.MODE_PRIVATE)
    return super.onBind(intent)
  }

  override fun onTileAdded() {
    super.onTileAdded()
    qsTile.state = Tile.STATE_INACTIVE
    qsTile.updateTile()
  }

  override fun onTileRemoved() {
    super.onTileRemoved()
    deleteVpnProfile()
  }

  override fun onClick() {
    if (isLocked) {
      return
    }

    // redirect user to store if OpenVPN Connect isn't installed
    if (!isOpenVpnConnectInstalled(applicationContext)) {
      Toast.makeText(applicationContext, R.string.openvpn_not_installed, Toast.LENGTH_LONG).show()
      startActivityAndCollapse(getOpenVpnLaunchIntent(applicationContext))
      return
    }

    // if a profile hasn't been configured, prompt the user to do it now
    if (vpnProfile.isEmpty() || vpnType.isEmpty()) {
      promptForVpnProfile()
      return
    }

    // otherwise, connect or disconnect based on the tile's current state...
    // clearly this isn't ideal, because the VPN can be connected/disconnected via other means, but
    // since the app doesn't have any notification mechanism (AFAIK) it's the best we can do
    when (qsTile.state) {
      Tile.STATE_INACTIVE -> connect()
      Tile.STATE_ACTIVE -> disconnect()
    }

  }

  private fun promptForVpnProfile() {
    val dialog = VpnProfileDialog()
    dialog.initialize(applicationContext, createProfileDialogListener())
    showDialog(dialog.onCreateDialog(null))
  }

  private fun createProfileDialogListener(): VpnProfileDialog.DialogListener {
    return object : VpnProfileDialog.DialogListener {
      override fun onPositiveClick(values: Pair<String, String>) {
        vpnProfile = values.first
        vpnType = values.second
        connect()
      }
      override fun onNegativeClick() {
        // no-op
      }
    }
  }

  private fun deleteVpnProfile() {
    prefs.edit().clear().apply()
  }

  // These intents are constructed using parameters documented here:
  // https://openvpn.net/vpn-server-resources/faq-regarding-openvpn-connect-android/#How_do_I_use_tasker_with_OpenVPN_Connect_for_Android
  private fun connect() {
    qsTile.state = Tile.STATE_ACTIVE
    qsTile.updateTile()
    val intent = Intent(Intent.ACTION_VIEW)
    intent.component = ComponentName(OPENVPN_PACKAGE, OPENVPN_ACTIVITY)
    intent.putExtra("$OPENVPN_PACKAGE.AUTOSTART_PROFILE_NAME", "$vpnType $vpnProfile")
    intent.putExtra("$OPENVPN_PACKAGE.AUTOCONNECT", true)
    intent.putExtra("$OPENVPN_PACKAGE.APP_SECTION", vpnProfile)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivityAndCollapse(intent)
  }

  private fun disconnect() {
    qsTile.state = Tile.STATE_INACTIVE
    qsTile.updateTile()
    val intent = Intent("$OPENVPN_PACKAGE.DISCONNECT")
    intent.component = ComponentName(OPENVPN_PACKAGE, OPENVPN_ACTIVITY)
    intent.putExtra("$OPENVPN_PACKAGE.STOP", true)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivityAndCollapse(intent)
  }

}