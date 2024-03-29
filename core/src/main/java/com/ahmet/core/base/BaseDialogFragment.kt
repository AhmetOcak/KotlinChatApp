package com.ahmet.core.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

abstract class BaseDialogFragment<ViewModel: BaseViewModel, VB: ViewDataBinding>: DialogFragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    abstract fun getViewModelClass() : Class<ViewModel>
    abstract fun getViewDataBinding() : VB

    protected val viewModel: ViewModel by lazy { ViewModelProvider(this)[getViewModelClass()] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = getViewDataBinding()
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun showToastMessage()

}