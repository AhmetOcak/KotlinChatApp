package com.ahmet.features.friend_requests

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.adapter.FriendRequestsAdapter
import com.ahmet.features.databinding.FragmentFriendRequestsBinding
import com.ahmet.features.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FriendRequests : BaseFragment<FriendRequestsViewModel, FragmentFriendRequestsBinding>() {

    override fun getViewModelClass(): Class<FriendRequestsViewModel> = FriendRequestsViewModel::class.java
    override fun getViewDataBinding(): FragmentFriendRequestsBinding = FragmentFriendRequestsBinding.inflate(layoutInflater)

    private lateinit var adapter: FriendRequestsAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        // when our data is ready, it will init adapter
        initAdapter()
    }

    private fun initAdapter() {
        viewModel.progressBarVis.observe(viewLifecycleOwner) {
            if(viewModel.progressBarVis.value == View.INVISIBLE) { // if progress bar gone then our data ready
                binding.friendRequests.layoutManager = LinearLayoutManager(activity)
                adapter = FriendRequestsAdapter(viewModel.friendRequests.value ?: listOf(), sharedPreferences)
                binding.friendRequests.adapter = adapter
            }
        }
    }
}