package com.chris.nakamai.utils

import com.chris.nakamai.data.AIRepoImplementation
import com.chris.nakamai.data.AuthRepoImplementation
import com.chris.nakamai.data.FirestoreDBRepoImplementation
import com.chris.nakamai.data.OnBoardingOperationImpl
import com.chris.nakamai.source.AIRepository
import com.chris.nakamai.source.AuthRepository
import com.chris.nakamai.source.FirestoreDBRepository
import com.chris.nakamai.source.OnBoardingRepository
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


    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        AuthRepoImplementation: AuthRepoImplementation
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindFirestoreDBRepository(
        FirestoreDBRepoImplementation: FirestoreDBRepoImplementation
    ): FirestoreDBRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
       OnboardingRepoImplementation: OnBoardingOperationImpl
    ): OnBoardingRepository

}