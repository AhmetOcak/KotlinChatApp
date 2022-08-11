package com.ahmet.features.friend_requests

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.core.base.BaseFragment
import com.ahmet.data.usecase.firebase.AddUser
import com.ahmet.data.usecase.firebase.DeleteFriendRequest
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.messages.CreateMessageDoc
import com.ahmet.features.R
import com.ahmet.features.adapter.FriendRequestsAdapter
import com.ahmet.features.databinding.FragmentFriendRequestsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FriendRequests : BaseFragment<FriendRequestsViewModel, FragmentFriendRequestsBinding>() {

    override fun getViewModelClass(): Class<FriendRequestsViewModel> = FriendRequestsViewModel::class.java
    override fun getViewDataBinding(): FragmentFriendRequestsBinding = FragmentFriendRequestsBinding.inflate(layoutInflater)

    private lateinit var adapter: FriendRequestsAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var addUser: AddUser
    @Inject
    lateinit var deleteFriendRequest: DeleteFriendRequest
    @Inject
    lateinit var getCurrentUserEmail: GetCurrentUserEmail
    @Inject
    lateinit var createMessageDoc: CreateMessageDoc

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.back.setOnClickListener {
            goToNextScreen(R.id.action_friendRequests_to_accountSettingsFragment, null, null)
        }

        // when our data is ready, it will init adapter
        initAdapter()
    }

    private fun initAdapter() {
        viewModel.progressBarVis.observe(viewLifecycleOwner) {
            if (viewModel.progressBarVis.value == View.INVISIBLE) { // if progress bar gone then our data ready
                binding.friendRequests.layoutManager = LinearLayoutManager(activity)
                adapter = FriendRequestsAdapter(
                    viewModel.friendRequests.value ?: mutableListOf(),
                    sharedPreferences,
                    addUser,
                    deleteFriendRequest,
                    lifecycleScope,
                    getCurrentUserEmail,
                    createMessageDoc
                )
                binding.friendRequests.adapter = adapter
            }
        }
    }
}