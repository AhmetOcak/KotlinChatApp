package com.ahmet.features.dialogs.deleteaccount

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.ahmet.features.R
import javax.inject.Inject

class DeleteAccountDialogFragment @Inject constructor(): DialogFragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rootView = inflater.inflate(R.layout.custom_delete_account_dialog, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.findViewById<Button>(R.id.dialogDeleteAccountButtonDAD).setOnClickListener {
            // delete account
            goToLoginScreen()
            dismiss()
        }

        rootView.findViewById<Button>(R.id.dialogCancelButtonDAD).setOnClickListener {
            dismiss()
        }
    }

    private fun goToLoginScreen() {
        findNavController().navigate(R.id.action_accountSettingsFragment_to_loginFragment)
    }
}