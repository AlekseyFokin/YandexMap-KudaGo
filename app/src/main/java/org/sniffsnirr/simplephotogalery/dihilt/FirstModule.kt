package org.sniffsnirr.simplephotogalery.dihilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sniffsnirr.simplephotogalery.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirstModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "galery.db").build()

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase) = db.tileDao()

}