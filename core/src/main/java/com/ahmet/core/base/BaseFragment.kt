package com.ahmet.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

abstract class BaseFragment<ViewModel : BaseViewModel, VB : ViewDataBinding> : Fragment() {

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
        _binding = getViewDataBinding()
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    protected fun goToNextScreen(resId: Int) {
        findNavController().navigate(resId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}