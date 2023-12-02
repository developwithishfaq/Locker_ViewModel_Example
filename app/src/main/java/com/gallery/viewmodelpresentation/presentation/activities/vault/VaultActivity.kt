package com.gallery.viewmodelpresentation.presentation.activities.vault

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallery.viewmodelpresentation.core.adapters.PhotoFilesAdapter
import com.gallery.viewmodelpresentation.core.interfaces.ToastListener
import com.gallery.viewmodelpresentation.databinding.ActivityVaultBinding
import com.gallery.viewmodelpresentation.presentation.activities.vault.components.VaultScreenEvents
import com.gallery.viewmodelpresentation.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VaultActivity : BaseActivity() {

    @Inject
    lateinit var binding: ActivityVaultBinding

    private val viewModel: VaultScreenViewModel by viewModels()

    @Inject
    lateinit var mAdapter: PhotoFilesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.onEvent(VaultScreenEvents.LoadPhotos(mContext))

        initAdapters()

        mAdapter.setListener(viewModel.listener)

        viewModel.setToastListener(object : ToastListener {
            override fun onToast(msg: String) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            }
        })

        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                mAdapter.submitList(state.vaultPhotos)
            }
        }

        binding.backArrow.setOnClickListener {
            finish()
        }

    }

    override fun handleBackPress() {
        finish()
    }

    private fun initAdapters() {
        binding.recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(mContext)
        }
    }
}