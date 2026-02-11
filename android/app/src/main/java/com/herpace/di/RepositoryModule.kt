package com.herpace.di

import com.herpace.data.repository.AuthRepositoryImpl
import com.herpace.data.repository.ProfileRepositoryImpl
import com.herpace.data.repository.RaceRepositoryImpl
import com.herpace.data.repository.TrainingPlanRepositoryImpl
import com.herpace.domain.repository.AuthRepository
import com.herpace.domain.repository.ProfileRepository
import com.herpace.domain.repository.RaceRepository
import com.herpace.domain.repository.TrainingPlanRepository
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
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindRaceRepository(impl: RaceRepositoryImpl): RaceRepository

    @Binds
    @Singleton
    abstract fun bindTrainingPlanRepository(impl: TrainingPlanRepositoryImpl): TrainingPlanRepository
}
