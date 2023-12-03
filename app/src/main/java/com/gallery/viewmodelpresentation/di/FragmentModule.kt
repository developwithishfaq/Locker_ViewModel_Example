package com.gallery.viewmodelpresentation.di

import android.app.Activity
import android.content.Context
import com.gallery.viewmodelpresentation.databinding.FragmentPhotosBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped


@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    @FragmentScoped
    fun getFragmentPhotosBinding(@ActivityContext context: Context) =
        FragmentPhotosBinding.inflate((context as Activity).layoutInflater)

}