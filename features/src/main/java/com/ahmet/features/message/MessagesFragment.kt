package com.ahmet.features.message

import android.content.Context
import android.content.SharedPreferences
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

    // created for cache user email
    private lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackHandler()
        binding.viewModel = viewModel

        sharedPref =
            activity?.getSharedPreferences("user_cache", Context.MODE_PRIVATE)!!

        // init user email
        viewModel.userEmail.value =
            requireArguments().getString(Constants.USER_EMAIL_ARG_NAME) ?: sharedPref.getString(
                Constants.SHARED_PREF_KEY,
                null
            )

        viewModel.setUserData()
        cacheUserEmail()

        viewModel.progressBarVisibility.observe(viewLifecycleOwner) {
            // if progress bar gone, then our data is ready
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
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
            val fragment = AddUserDialogFragment.newInstance(
                viewModel.userEmail.value.toString()
            )
            fragment.show(parentFragmentManager, "Add User")
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

    private fun cacheUserEmail() {
        if (viewModel.userEmail.value != null) {
            with(sharedPref.edit()) {
                putString(Constants.SHARED_PREF_KEY, viewModel.userEmail.value.toString())
                apply()
            }
        }
    }

}