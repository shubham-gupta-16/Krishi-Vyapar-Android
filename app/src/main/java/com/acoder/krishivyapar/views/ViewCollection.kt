package com.acoder.krishivyapar.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.acoder.krishivyapar.R

class ViewCollection {

    class LoadingProgressDialog(context: Context, private val title: String, cancelable: Boolean = false) {
        private var alertDialog: AlertDialog
        private var hbar: ProgressBar
        private var titleText: TextView

        init {
            val dialogRoot: View =
                LayoutInflater.from(context).inflate(R.layout.layout_progress_loading, null)
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(cancelable)
            builder.setView(dialogRoot)
            hbar = dialogRoot.findViewById(R.id.progress_bar)
            titleText = dialogRoot.findViewById(R.id.title)
            titleText.text = title
            alertDialog = builder.create()
        }

        fun show() {
            alertDialog.show()
        }

        fun dismiss() {
            alertDialog.dismiss()
        }

        @SuppressLint("SetTextI18n")
        fun updateProgress(progress: Long, max : Long) {
            val percent = getPercentCal(progress, max)
            Log.d("tagprog", "$progress | $max => $percent%")
            titleText.text = "$title ($percent%)"
            hbar.max = 100
            hbar.progress = percent
        }

        private fun getPercentCal(bytesUploaded: Long, totalBytes: Long): Int {
            return (bytesUploaded * 100 / totalBytes.toDouble()).toInt()
        }
    }
}