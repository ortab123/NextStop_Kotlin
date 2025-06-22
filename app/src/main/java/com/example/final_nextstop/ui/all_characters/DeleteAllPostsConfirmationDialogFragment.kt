package com.example.final_nextstop.ui.all_characters

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.final_nextstop.R

class DeleteAllPostsConfirmationDialogFragment : DialogFragment() {

    var onDeleteAllPostsConfirmed: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_delete))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_posts))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> onDeleteAllPostsConfirmed?.invoke() }
            .setNegativeButton(getString(R.string.no), null)
            .create()
    }

    companion object {
        fun newInstance(): DeleteAllPostsConfirmationDialogFragment {
            return DeleteAllPostsConfirmationDialogFragment()
        }
    }
}
