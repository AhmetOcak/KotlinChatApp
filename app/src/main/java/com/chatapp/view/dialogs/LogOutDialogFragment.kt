package com.chatapp.view.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.chatapp.R

class LogOutDialogFragment() : DialogFragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rootView = inflater.inflate(R.layout.custom_logout_dialog, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.findViewById<Button>(R.id.dialogLogOutButtonDAD).setOnClickListener {
            // logout
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

