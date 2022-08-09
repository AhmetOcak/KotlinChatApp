package com.ahmet.features.message

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ahmet.core.base.BaseFragment
import com.ahmet.data.model.UserEntity
import com.ahmet.features.R
import com.ahmet.features.adapter.UserAdapter
import com.ahmet.features.databinding.FragmentMessagesBinding
import com.ahmet.features.dialogs.adduser.AddUserDialogFragment
import com.ahmet.features.utils.resource.ImpUserImage
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

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackHandler()
        binding.viewModel = viewModel

        // images taken from firebase storage
        viewModel.getUserImage()

        // images are applied
        viewModel.userImageFilePath.observe(viewLifecycleOwner) {
            if (viewModel.userImageFilePath.value != null) {
                viewModel.putSharedPref()
                initUserImage()
            }
        }

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

        initAdapter()

        binding.addUser.setOnClickListener {
            addUserDialogFragment.show(parentFragmentManager, "Add User")
        }

        binding.friendsRecylerview.layoutManager = LinearLayoutManager(activity)
        binding.currentUserImage.setOnClickListener {
            goToNextScreen(R.id.action_messagesFragment_to_accountSettingsFragment, null, null)
        }

        setRefreshListener()
        notifyAdapter()
    }

    private fun onBackHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ActivityCompat.finishAffinity(requireActivity())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun initAdapter() {
        viewModel.progressBarVisibility.observe(viewLifecycleOwner) {
            // if progress bar gone, then our data is ready
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
                binding.friendsRecylerview.layoutManager = LinearLayoutManager(activity)

                // The new message will appear in descending order.
                viewModel.userFriends.value?.sortByDescending { it.emailAddress }

                adapter = UserAdapter(
                    viewModel.userFriends.value,
                    findNavController(),
                    sharedPreferences,
                    viewModel.currentMessages.value ?: listOf(),
                    viewModel.unreadMessagesAlert.value ?: listOf(),
                    ::callback
                )
                binding.friendsRecylerview.adapter = adapter
                binding.searchView.setQuery("", false)
            }
        }
    }

    // refresh user friends in recylerview
    private fun setRefreshListener() {
        binding.refreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.refreshUserFriends()
                adapter.filterList(viewModel.userFriends.value) // for the update recylerview
                adapter.takeUnreadMessages(viewModel.unreadMessagesAlert.value ?: listOf())
                adapter.updateMessageData(viewModel.currentMessages.value ?: listOf())
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    // notify adapter when message come
    private fun notifyAdapter() {
        viewModel.currentMessages.observe(viewLifecycleOwner) {
            if (!viewModel.currentMessages.value.isNullOrEmpty() && this::adapter.isInitialized) {
                adapter.filterList(viewModel.userFriends.value)
                adapter.updateMessageData(viewModel.currentMessages.value ?: listOf())
                adapter.takeUnreadMessages(viewModel.unreadMessagesAlert.value ?: listOf())
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun filterList(friendName: String) {
        val filteredList: MutableList<UserEntity> = mutableListOf()

        for (elem in viewModel.userFriends.value!!) {
            if (elem.userName.contains(friendName)) {
                filteredList.add(0, elem)
            }
        }
        adapter.filterList(filteredList)
    }

    private fun initUserImage() {
        val path = viewModel.getUserImageFromSharedPref()
        if (path != null) {
            val bitmap = ImpUserImage.implementUserImage(path)
            view?.findViewById<ImageView>(R.id.current_user_image)?.setImageBitmap(bitmap)
        } else {
            view?.findViewById<ImageView>(R.id.current_user_image)?.setImageResource(com.ahmet.core.R.drawable.blank_profile_picture)
        }
    }

    private fun callback(pos: Int) {
        if (!viewModel.unreadMessagesAlert.value.isNullOrEmpty()) viewModel.unreadMessagesAlert.value?.set(pos, 0)
    }

}