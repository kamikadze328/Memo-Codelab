package com.kamikadze328.memo.data.di

import com.kamikadze328.memo.data.MemoRepository
import com.kamikadze328.memo.domain.repository.IMemoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    internal abstract fun bindMemoRepository(impl: MemoRepository): IMemoRepository
}

