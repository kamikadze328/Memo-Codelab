package com.kamikadze328.memo.data.di

import android.content.Context
import androidx.room.Room
import com.kamikadze328.memo.data.db.AppDatabase
import com.kamikadze328.memo.data.db.MemoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    internal fun provideDb(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room
            .databaseBuilder(applicationContext, AppDatabase::class.java, "codelab")
            .build()
    }

    @Provides
    internal fun provideMemoDao(db: AppDatabase): MemoDao = db.memoDao()
}