package com.example.final_nextstop.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.final_nextstop.data.model.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPost(post:Post)

    @Delete
    suspend fun deletePost(vararg posts:Post)

    @Update
    suspend fun updatePost(post:Post)

    @Query("SELECT * FROM posts ")
    fun getPosts() : LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getPost(id:Int) :Post

    @Query("DELETE FROM posts")
    suspend fun deleteAll(): Int

    @Query("UPDATE posts SET profileImageUri = :profileImageUri WHERE id = :id")
    suspend fun updateProfileImageUri(id: Int, profileImageUri: String)

    @Query("SELECT * FROM posts WHERE isFavorite = 1 ORDER BY date DESC")
    fun getFavoritePosts(): LiveData<List<Post>>

    @Query("UPDATE posts SET isFavorite = :isFav WHERE id = :postId")
    suspend fun updateFavorite(postId: Int, isFav: Boolean)

}