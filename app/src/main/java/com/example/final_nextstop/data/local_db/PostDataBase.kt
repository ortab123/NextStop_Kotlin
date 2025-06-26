package com.example.final_nextstop.data.local_db

import androidx.room.*
import com.example.final_nextstop.data.model.Post


@Database(entities = [Post::class], version = 1, exportSchema = false)
@TypeConverters(PostDataBase.Converters::class)
abstract class PostDataBase : RoomDatabase() {

    abstract fun postsDao(): PostDao

    class Converters {
        @TypeConverter
        fun fromString(value: String?): List<String>? {
            return value?.split(",")?.map { it.trim() }
        }

        @TypeConverter
        fun fromList(list: List<String>?): String? {
            return list?.joinToString(", ")
        }
    }
}








