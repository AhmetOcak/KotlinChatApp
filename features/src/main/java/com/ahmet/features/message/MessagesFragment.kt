package com.ahmet.features.message

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.R
import com.ahmet.features.adapter.UserAdapter
import com.ahmet.features.databinding.FragmentMessagesBinding
import com.ahmet.features.dialogs.AddUserDialogFragment
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : BaseFragment<MessageViewModel, FragmentMessagesBinding>() {

    override fun getViewModelClass() = MessageViewModel::class.java
    override fun getViewDataBinding() = FragmentMessagesBinding.inflate(layoutInflater)

    private lateinit var adapter: UserAdapter

    @Inject
    lateinit var addUserDialogFragment: AddUserDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userEmail.value = requireArguments().getString(Constants.USER_EMAIL_ARG_NAME)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackHandler()
        binding.viewModel = viewModel

        viewModel.setUserData()

        viewModel.progressBarVisibility.observe(viewLifecycleOwner) {

            // if progress bar gone, then our data is ready
            if(viewModel.progressBarVisibility.value == View.INVISIBLE) {
                Log.e("e", viewModel.userFriends.value.toString())
                binding.friendsRecylerview.layoutManager = LinearLayoutManager(activity)
                adapter = UserAdapter(viewModel.userFriends.value, findNavController())
                binding.friendsRecylerview.adapter = adapter
            }
        }

        binding.friendsRecylerview.layoutManager = LinearLayoutManager(activity)

        binding.currentUserImage.setOnClickListener {
            goToNextScreen(R.id.action_messagesFragment_to_accountSettingsFragment, null, null)
        }
        binding.addUser.setOnClickListener {
            addUserDialogFragment.show(parentFragmentManager, "Add User")
        }
    }

    private fun onBackHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ActivityCompat.finishAffinity(requireActivity())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}