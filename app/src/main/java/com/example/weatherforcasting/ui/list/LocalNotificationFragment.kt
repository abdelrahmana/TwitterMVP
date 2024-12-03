package com.example.weatherforcasting.ui.list

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.example.weatherforcasting.data.model.NotificationData
import com.example.weatherforcasting.databinding.LocalNotificationFragmentBinding
import com.example.weatherforcasting.domain.LogicClass
import com.example.weatherforcasting.ui.details.DetailsActivity
import com.example.weatherforcasting.ui.list.adaptor.localeNotificationAdaptor
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocalNotificationFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @Inject lateinit var logicClass: LogicClass
    val viewModel: LocalNotificationViewModel by viewModels()
    private var arrayListNotifications = ArrayList<NotificationData>()
    private var notificationAdaptor : localeNotificationAdaptor? = null
    private var binding: LocalNotificationFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LocalNotificationFragmentBinding.inflate(layoutInflater, container, false)
        setViewObserver()
        setNotificationLocale()
        askPermissionsIfNeeded()
        binding?.cancelButton?.setOnClickListener{
            WorkManager.getInstance(requireContext()).cancelAllWork()
            Snackbar.make(binding.root, "all notifications are canceled", Snackbar.LENGTH_SHORT).show()

        }
        return binding?.root
    }

    override fun onResume() {
        viewModel.getNotificationData()
        super.onResume()
    }

    private fun setNotificationLocale() {
        notificationAdaptor = localeNotificationAdaptor(requireContext(),arrayListNotifications){item->
            context?.startActivity(Intent(requireContext(),DetailsActivity::class.java).putExtra(
                DetailsActivity.ITEM_INFO,Gson().toJson(item))) // start details activity
        }
        binding!!.recycleLocaleNotification.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = notificationAdaptor
        }
    }
    private fun askPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions = arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            )
            val shouldAsk = permissions.any {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }

            if (shouldAsk) {
                notificationPermissionLauncher.launch(
                    permissions
                )
            }
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->

    }
    private fun setViewObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.notificationDataStateFlow.collectLatest {
                it?.let {
                        arrayListNotifications.clear()
                        notificationAdaptor?.updateList(logicClass.parseXml(it))

                    }
                        //bindViewWithData(binding,it)
                }
            }
        viewModel.networkLoader.observe(viewLifecycleOwner, Observer {
            it?.let { progress ->
                binding?.progressLoader?.visibility = it
            }
        })
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.errorStateFlow.collectLatest {
                it?.let {
                   Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()

                }
                //bindViewWithData(binding,it)
            }
        }
    }



    companion object {
        val NOTIFICATION = "notification"
        val ID = "id"
        val TITLE_VALUE = "title"
        val TIME = "timeInSeconds"
    }
}