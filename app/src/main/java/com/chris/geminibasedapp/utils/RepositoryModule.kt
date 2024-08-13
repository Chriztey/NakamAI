package com.chris.geminibasedapp.utils

import com.chris.geminibasedapp.data.AIRepoImplementation
import com.chris.geminibasedapp.source.AIRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMyRepository(
        AIRepoImplementation: AIRepoImplementation
    ): AIRepository

}