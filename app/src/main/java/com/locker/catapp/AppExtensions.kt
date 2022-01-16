package com.locker.catapp

import android.content.Context
import android.content.res.Configuration

fun Context.uiMode() = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)

fun Context.isNightMode() = uiMode() == Configuration.UI_MODE_NIGHT_YES
