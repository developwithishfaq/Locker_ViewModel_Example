package com.gallery.viewmodelpresentation.presentation.fragments.vault.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallery.viewmodelpresentation.core.adapters.PhotoFilesAdapter
import com.gallery.viewmodelpresentation.databinding.FragmentPhotosBinding
import com.gallery.viewmodelpresentation.presentation.activities.vault.VaultScreenViewModel
import com.gallery.viewmodelpresentation.presentation.activities.vault.components.VaultScreenEvents
import com.gallery.viewmodelpresentation.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhotosFragment : BaseFragment() {

    @Inject
    lateinit var binding: FragmentPhotosBinding

    private val viewModel: VaultScreenViewModel by viewModels()

    @Inject
    lateinit var mAdapter: PhotoFilesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEvent(VaultScreenEvents.LoadPhotos(pathHelper.getPrivatePhotosFile()))

        initAdapter()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                mAdapter.submitList(it.vaultPhotos)
            }
        }
    }

    private fun initAdapter() {
        binding.recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(mContext)
        }
    }
}


