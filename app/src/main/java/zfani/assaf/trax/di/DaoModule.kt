package zfani.assaf.trax.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import zfani.assaf.trax.data.local.db.TraxDatabase

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideDB(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, TraxDatabase::class.java, "trax-db").build()

    @Provides
    fun provideProductDAO(traxDatabase: TraxDatabase) = traxDatabase.productDao()
}