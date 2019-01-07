package com.banasiak.android.openvpntile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LongPressActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startActivity(getOpenVpnLaunchIntent(this))
    finish()
  }

}