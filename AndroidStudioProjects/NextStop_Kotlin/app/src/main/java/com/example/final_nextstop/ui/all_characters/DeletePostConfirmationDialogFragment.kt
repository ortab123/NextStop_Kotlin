package com.example.final_nextstop.ui.all_characters

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.final_nextstop.R

class DeletePostConfirmationDialogFragment : DialogFragment() {
    var onDeletePostConfirmed: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_delete))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_the_post))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> onDeletePostConfirmed?.invoke() }
            .setNegativeButton(getString(R.string.no), null)
            .create()
    }

    companion object {
        fun newInstance(): DeletePostConfirmationDialogFragment {
            return DeletePostConfirmationDialogFragment()
        }
    }
}