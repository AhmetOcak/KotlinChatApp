package com.ahmet.features.edit_profile

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ahmet.core.R
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.databinding.FragmentEditProfileBinding
import com.ahmet.features.utils.resource.ImpUserImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfile : BaseFragment<EditViewModel, FragmentEditProfileBinding>() {

    private var uri: Uri? = null
    private var intent: ActivityResultLauncher<String>? = null
    private var isThisInit = true

    @Inject
    lateinit var toast: Toast

    override fun getViewModelClass(): Class<EditViewModel> = EditViewModel::class.java
    override fun getViewDataBinding(): FragmentEditProfileBinding =
        FragmentEditProfileBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        initUserImage()

        binding.userImage.setOnClickListener { intent!!.launch("image/*") }

        binding.saveButton.setOnClickListener {
            if(uri != null) {
                viewModel.uploadUserImage(uri ?: Uri.EMPTY)
            }
        }

        binding.cancelButton.setOnClickListener { initUserImage() }

        viewModel.userImageFilePath.observe(viewLifecycleOwner) {
            if(viewModel.userImageFilePath.value != null || viewModel.userImageFilePath.value != "null") {
                val bitmap = ImpUserImage.implementUserImage(viewModel.userImageFilePath.value)
                binding.userImage.setImageBitmap(bitmap)
            }
        }

        viewModel.resultMessage.observe(viewLifecycleOwner) {
            if(viewModel.resultMessage.value == "Successful") {
                viewModel.getUserImage()
                toast.setText("Upload Successful")
                toast.show()
            }
        }
    }

    // preview for the image
    override fun onResume() {
        super.onResume()
        if(isThisInit) {
            isThisInit = false
        }else {
            binding.userImage.setImageBitmap(MediaStore.Images.Media.getBitmap(context?.contentResolver, uri))
        }
    }

    private fun initUserImage() {
        val path = viewModel.getUserImageFromSharedPref()
        if(path != null) {
            val bitmap = ImpUserImage.implementUserImage(path)
            binding.userImage.setImageBitmap(bitmap)
        }else {
            binding.userImage.setImageResource(R.drawable.blank_profile_picture)
        }
    }

}

