package com.herpace.di

import android.content.Context
import androidx.room.Room
import com.herpace.data.local.HerPaceDatabase
import com.herpace.data.local.dao.RaceDao
import com.herpace.data.local.dao.RunnerProfileDao
import com.herpace.data.local.dao.TrainingPlanDao
import com.herpace.data.local.dao.TrainingSessionDao
import com.herpace.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): HerPaceDatabase {
        val passphrase = net.sqlcipher.database.SQLiteDatabase.getBytes("herpace_db_key".toCharArray())
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            HerPaceDatabase::class.java,
            HerPaceDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    fun provideUserDao(database: HerPaceDatabase): UserDao = database.userDao()

    @Provides
    fun provideRunnerProfileDao(database: HerPaceDatabase): RunnerProfileDao = database.runnerProfileDao()

    @Provides
    fun provideRaceDao(database: HerPaceDatabase): RaceDao = database.raceDao()

    @Provides
    fun provideTrainingPlanDao(database: HerPaceDatabase): TrainingPlanDao = database.trainingPlanDao()

    @Provides
    fun provideTrainingSessionDao(database: HerPaceDatabase): TrainingSessionDao = database.trainingSessionDao()
}
