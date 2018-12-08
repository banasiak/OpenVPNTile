package com.banasiak.android.openvpntile

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QuickSettingsService : TileService() {
  companion object {
    const val OPENVPN_PACKAGE = "net.openvpn.openvpn"
    const val OPENVPN_ACTIVITY = "net.openvpn.unified.MainActivity"
    const val VPN_PROFILE_KEY = "vpn_profile"
    const val VPN_TYPE_KEY = "vpn_type"
    const val DEFAULT_VPN_PROFILE = "vpn.cyberguyenterprises.com"
  }

  private lateinit var prefs: SharedPreferences

  private var vpnProfile: String
    get() = prefs.getString(VPN_PROFILE_KEY, DEFAULT_VPN_PROFILE)!!
    set(value) { prefs.edit().putString(VPN_PROFILE_KEY, value).apply() }
  private var vpnType: String
    get() = prefs.getString(VPN_TYPE_KEY, "PC")!! // could also be "AS"
    set(value) { prefs.edit().putString(VPN_TYPE_KEY, value).apply() }


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

    //TODO -> check to see if the VPN profile has been defined
    //promptForVpnProfile()

    when (qsTile.state) {
      Tile.STATE_INACTIVE -> connect()
      Tile.STATE_ACTIVE -> disconnect()
    }

    qsTile.updateTile()
  }

  private fun promptForVpnProfile() {
    val dialog = VpnProfileDialog()
    dialog.initialize(applicationContext, createProfileDialogListener())
    showDialog(dialog.onCreateDialog(null))
  }

  private fun createProfileDialogListener(): VpnProfileDialog.DialogListener {
    return object : VpnProfileDialog.DialogListener {
      override fun onPositiveClick() {
        //TODO -> persist vpn profile name
      }
      override fun onNegativeClick() {
        // no-op
      }
    }
  }

  private fun deleteVpnProfile() {
    prefs.edit().clear().apply()
  }

  private fun connect() {
    qsTile.state = Tile.STATE_ACTIVE
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
    val intent = Intent("$OPENVPN_PACKAGE.DISCONNECT")
    intent.component = ComponentName(OPENVPN_PACKAGE, OPENVPN_ACTIVITY)
    intent.putExtra("$OPENVPN_PACKAGE.STOP", true)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivityAndCollapse(intent)
  }

}