package com.kamikadze328.memo.core.android

import com.kamikadze328.memo.core.android.coroutines.DispatcherProvider
import com.kamikadze328.memo.core.android.coroutines.IDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CoroutinesModule {
    @Binds
    abstract fun bindsDispatcherProvider(impl: DispatcherProvider): IDispatcherProvider
}