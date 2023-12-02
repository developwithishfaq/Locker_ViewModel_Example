package com.gallery.viewmodelpresentation.di

import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import com.bumptech.glide.Glide
import com.gallery.viewmodelpresentation.data.repository.GalleryOperationsImpl
import com.gallery.viewmodelpresentation.data.repository.VaultRepositoryImpl
import com.gallery.viewmodelpresentation.domain.repository.GalleryOperations
import com.gallery.viewmodelpresentation.domain.repository.VaultRepository
import com.gallery.viewmodelpresentation.domain.use_case.DeleteExternalStoragePhotoUseCase
import com.gallery.viewmodelpresentation.domain.use_case.DeleteFileUseCase
import com.gallery.viewmodelpresentation.domain.use_case.FetchGalleryPhotosUseCase
import com.gallery.viewmodelpresentation.domain.use_case.MoveFileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Provides
    @Singleton
    fun getContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Singleton
    @Provides
    fun provideGalleryOperations(
        fetchGalleryPhotosUseCase: FetchGalleryPhotosUseCase,
        deleteExternalStoragePhotoUseCase: DeleteExternalStoragePhotoUseCase,
        moveFileUseCase: MoveFileUseCase
    ): GalleryOperations = GalleryOperationsImpl(
        fetchGalleryPhotosUseCase, deleteExternalStoragePhotoUseCase, moveFileUseCase
    )

    @Singleton
    @Provides
    fun provideVaultRepository(
        moveFileUseCase: MoveFileUseCase,
        deleteFileUseCase: DeleteFileUseCase
    ): VaultRepository = VaultRepositoryImpl(moveFileUseCase,deleteFileUseCase)

    @Provides
    @Singleton
    @Named("Main")
    fun getMainCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)

    @Provides
    @Singleton
    fun getGlide(@ApplicationContext context: Context) = Glide.with(context)

    @Provides
    @Singleton
    fun getConnectivityManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}