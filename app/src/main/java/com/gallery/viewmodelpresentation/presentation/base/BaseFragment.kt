package com.gallery.viewmodelpresentation.presentation.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gallery.viewmodelpresentation.domain.util.path_helper.PathHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    lateinit var mContext: Activity

    @Inject
    lateinit var pathHelper: PathHelper

    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = requireActivity()
    }
}