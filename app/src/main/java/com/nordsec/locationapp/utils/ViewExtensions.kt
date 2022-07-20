package com.nordsec.locationapp.utils

import android.view.View

fun View.showItAndHideOthers(vararg otherViews: View) {
    this.visibility = View.VISIBLE
    otherViews.forEach {
        it.visibility = View.GONE
    }
}