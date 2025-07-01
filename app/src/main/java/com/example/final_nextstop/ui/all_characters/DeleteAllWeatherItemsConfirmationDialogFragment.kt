package com.example.final_nextstop.ui.all_characters

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.final_nextstop.R

class DeleteAllWeatherItemsConfirmationDialogFragment : DialogFragment(){

    var onDeleteAllWeatherItemsConfirmed: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle((R.string.confirm_delete))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_weather_items))
            .setPositiveButton(R.string.yes) { _, _ -> onDeleteAllWeatherItemsConfirmed?.invoke() }
            .setNegativeButton(R.string.no, null)
            .create()
    }

    companion object {
        fun newInstance(): DeleteAllWeatherItemsConfirmationDialogFragment {
            return DeleteAllWeatherItemsConfirmationDialogFragment()
        }
    }
}