package com.example.final_nextstop.data.local_db

import android.content.Context
import androidx.room.*
//import androidx.room.migration.Migration
import com.example.final_nextstop.data.model.Post
//import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Post::class], version = 1, exportSchema = false)
@TypeConverters(PostDataBase.Converters::class)
abstract class PostDataBase : RoomDatabase() {

    abstract fun postsDao(): PostDao

    companion object {

        @Volatile
        private var instance: PostDataBase? = null

//        private val MIGRATION_3_4 = object : Migration(3, 4) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE your_table ADD COLUMN new_column INTEGER DEFAULT 0")
//
//            }
//        }

        fun getDatabase(context: Context): PostDataBase =
            instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    PostDataBase::class.java,
                    "posts_db"
                )
//                    .addMigrations(MIGRATION_3_4)
                    .build()
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
    }
}








