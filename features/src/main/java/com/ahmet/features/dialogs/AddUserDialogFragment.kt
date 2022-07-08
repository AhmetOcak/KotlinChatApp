package com.ahmet.features.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.ahmet.features.R
import javax.inject.Inject

class AddUserDialogFragment @Inject constructor() : DialogFragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rootView = inflater.inflate(R.layout.custom_adduser_dialog, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.findViewById<Button>(R.id.dialogAddUserButtonAUD).setOnClickListener {
            // add user
            dismiss()
        }

        rootView.findViewById<Button>(R.id.dialogCancelButtonAUD).setOnClickListener {
            dismiss()
        }
    }
}