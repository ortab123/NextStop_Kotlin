package com.example.final_nextstop.di

import android.content.Context
import androidx.room.Room
import com.example.final_nextstop.data.local_db.PostDao
import com.example.final_nextstop.data.local_db.PostDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PostDataBase =
        Room.databaseBuilder(
            context,
            PostDataBase::class.java,
            "posts_db"
        ).build()

    @Provides
    fun providePostDao(database: PostDataBase): PostDao =
        database.postsDao()

    //Retrofit
}

