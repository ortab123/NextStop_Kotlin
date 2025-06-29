package com.example.final_nextstop.data.local_db

import android.content.Context
import androidx.room.*
//import androidx.room.migration.Migration
import com.example.final_nextstop.data.model.Post
import com.example.final_nextstop.data.model.SearchType
import com.example.final_nextstop.data.model.WeatherItem

@Database(entities =  [Post::class, WeatherItem::class], version = 4, exportSchema = false)
@TypeConverters(AppDataBase.Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun postsDao(): PostDao
    abstract fun weatherItemsDao(): WeatherItemDao

    companion object {

        @Volatile
        private var instance: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase =
            instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_db"
                )
                    .build().also {
                        instance = it
                    }
            }
    }

    class Converters {
        @TypeConverter
        fun fromString(value: String?): List<String>? {
            return value?.split(",")?.map { it.trim() }
        }

        @TypeConverter
        fun fromList(list: List<String>?): String? {
            return list?.joinToString(", ")
        }

        @TypeConverter
        fun fromSearchType(value: SearchType): String = value.name

        @TypeConverter
        fun toSearchType(value: String): SearchType = SearchType.valueOf(value)


    }
}
