package com.gallery.viewmodelpresentation.di

import android.content.Context
import android.view.LayoutInflater
import com.gallery.viewmodelpresentation.databinding.ActivityMainBinding
import com.gallery.viewmodelpresentation.databinding.ActivityVaultBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    fun getActivityVaultBinding(@ActivityContext context: Context) =
        ActivityVaultBinding.inflate(LayoutInflater.from(context))

    @Provides
    @ActivityScoped
    fun getActivityMainBinding(@ActivityContext context: Context) =
        ActivityMainBinding.inflate(LayoutInflater.from(context))

}