package com.gerpo.spottrack.ui.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.gerpo.spottrack.R


class ConfirmDialog {
    companion object {
        fun getCreateDialog(context: Context): AlertDialog.Builder {

            return AlertDialog.Builder(context)
                    .setMessage(R.string.confirm_untracking)
                    .setNegativeButton(R.string.cancel) {
                        dialog, which -> dialog.dismiss()
                    }
        }
    }
}

