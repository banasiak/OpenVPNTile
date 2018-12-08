package com.banasiak.android.openvpntile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment

class VpnProfileDialog: DialogFragment() {

  interface DialogListener {
    fun onPositiveClick()
    fun onNegativeClick()
  }

  private lateinit var _context: Context
  private lateinit var _listener: DialogListener

  fun initialize(context: Context, listener: DialogListener) {
    this._context = context
    this._listener = listener
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

    if(!::_context.isInitialized || !::_listener.isInitialized) {
      throw IllegalStateException("QuickSettingsDialog not properly initialized!")
    }

    val alertBuilder = AlertDialog.Builder(_context)
    alertBuilder.setView(R.layout.dialog)
    alertBuilder.setPositiveButton(android.R.string.ok) {
      _, _ -> _listener.onPositiveClick()
    }
    alertBuilder.setNegativeButton(android.R.string.cancel) {
      _, _ -> _listener.onNegativeClick()
    }
    return alertBuilder.create()
  }

}