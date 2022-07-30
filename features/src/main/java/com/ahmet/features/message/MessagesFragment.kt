package com.ahmet.features.message

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ahmet.core.base.BaseFragment
import com.ahmet.domain.model.User
import com.ahmet.features.R
import com.ahmet.features.adapter.UserAdapter
import com.ahmet.features.databinding.FragmentMessagesBinding
import com.ahmet.features.dialogs.adduser.AddUserDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : BaseFragment<MessageViewModel, FragmentMessagesBinding>() {

    override fun getViewModelClass() = MessageViewModel::class.java
    override fun getViewDataBinding() = FragmentMessagesBinding.inflate(layoutInflater)

    private lateinit var adapter: UserAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var addUserDialogFragment: AddUserDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackHandler()
        binding.viewModel = viewModel

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText!!)
                return false
            }
        })

        swipeRefreshLayout = binding.refreshLayout
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(255, 119, 0))
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(30, 25, 64))

        viewModel.setUserData()

        viewModel.progressBarVisibility.observe(viewLifecycleOwner) {
            // if progress bar gone, then our data is ready
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
                binding.friendsRecylerview.layoutManager = LinearLayoutManager(activity)
                adapter = UserAdapter(viewModel.userFriends.value, findNavController())
                binding.friendsRecylerview.adapter = adapter
                binding.searchView.setQuery("", false)
            }
        }

        binding.addUser.setOnClickListener {
            addUserDialogFragment.show(parentFragmentManager, "Add User")
        }

        binding.friendsRecylerview.layoutManager = LinearLayoutManager(activity)
        binding.currentUserImage.setOnClickListener {
            goToNextScreen(R.id.action_messagesFragment_to_accountSettingsFragment, null, null)
        }

        setRefreshListener()
    }

    private fun onBackHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ActivityCompat.finishAffinity(requireActivity())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    // refresh user friends in recylerview
    private fun setRefreshListener() {
        binding.refreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.refreshUserFriends()
                adapter.filterList(viewModel.userFriends.value) // for the update recylerview
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun filterList(friendName: String) {
        val filteredList: MutableList<User> = mutableListOf()

        for (elem in viewModel.userFriends.value!!) {
            if (elem.userName.contains(friendName)) {
                filteredList.add(0, elem)
            }
        }
        adapter.filterList(filteredList)
    }

}