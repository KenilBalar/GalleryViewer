package com.kenil.gallaryviewer.views.utils

import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.kenil.gallaryviewer.R
import java.util.*


/**
 * For Finish Activity from the Outside of Activity Scope
 * */
fun Context.finish() {
    (this as Activity).finish()
}

fun Context.finishAffinity() {
    (this as Activity).finishAffinity()
}

fun Context.ShowToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.StartActivity(activity: Activity, finishActivity: Boolean = false) {
    startActivity(Intent(this, activity::class.java))
    if (finishActivity) this.finish()
}

fun View.doVisible() {
    this.visibility = View.VISIBLE
}

fun View.doHide() {
    this.visibility = View.GONE
}


