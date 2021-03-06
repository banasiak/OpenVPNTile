package com.banasiak.android.openvpntile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.view.ContextThemeWrapper

class VpnProfileDialog : AppCompatDialogFragment() {
  interface DialogListener {
    fun onPositiveClick(values: Pair<String, String>)
    fun onNegativeClick()
  }

  private lateinit var _context: Context
  private lateinit var listener: DialogListener
  private lateinit var dialogView: View

  fun initialize(context: Context, listener: DialogListener) {
    this._context = ContextThemeWrapper(context, R.style.AppTheme)
    this.listener = listener
  }

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    check(::_context.isInitialized && ::listener.isInitialized) {
      "QuickSettingsDialog not properly initialized!"
    }

    dialogView = LayoutInflater.from(_context).inflate(R.layout.dialog, null)
    val adapter = ArrayAdapter<String>(_context, android.R.layout.simple_spinner_dropdown_item,
        _context.resources.getStringArray(R.array.vpn_type))
    dialogView.findViewById<Spinner>(R.id.profileType).adapter = adapter

    val alertBuilder = AlertDialog.Builder(_context)
    alertBuilder.setView(dialogView)
    alertBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
      listener.onPositiveClick(getProfileValues())
    }
    alertBuilder.setNegativeButton(android.R.string.cancel) { _, _ ->
      listener.onNegativeClick()
    }

    return alertBuilder.create()
  }

  private fun getProfileValues(): Pair<String, String> {
    val profileName = dialogView.findViewById<EditText>(R.id.profileName).text.toString()
    val profileTypes = _context.resources.getStringArray(R.array.vpn_type_prefix)
    val profileType =
      profileTypes[dialogView.findViewById<Spinner>(R.id.profileType).selectedItemPosition]
    return Pair(profileName, profileType)
  }

}