package com.gallery.viewmodelpresentation.presentation.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallery.viewmodelpresentation.core.adapters.MyPhotosAdapter
import com.gallery.viewmodelpresentation.databinding.ActivityMainBinding
import com.gallery.viewmodelpresentation.presentation.activities.main.comonents.MainScreenEvents
import com.gallery.viewmodelpresentation.presentation.activities.vault.VaultActivity
import com.gallery.viewmodelpresentation.presentation.base.BasePermissionsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BasePermissionsActivity() {

    @Inject
    lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var myPhotosAdapter: MyPhotosAdapter

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            viewModel.onEvent(MainScreenEvents.LauncherResults(it))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.init(intentLauncher)
        initAdapter()

        binding.vaultBtn.setOnClickListener {
            startActivity(Intent(mContext, VaultActivity::class.java))
        }

        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                myPhotosAdapter.submitList(state.galleryPhotosList)
            }
        }
    }

    override fun handleBackPress() {
        finish()
    }

    private fun initAdapter() {
        myPhotosAdapter.setListener(viewModel.listener)
        binding.recyclerView.apply {
            adapter = myPhotosAdapter
            layoutManager = LinearLayoutManager(mContext)
        }
    }

    override fun onResume() {
        super.onResume()

        askForPermissions(PermissionType.GALLERY_READ, object : PermissionListener {
            override fun onAccepted() {
                viewModel.onEvent(MainScreenEvents.LoadPhotos)
            }
        })

    }
}

