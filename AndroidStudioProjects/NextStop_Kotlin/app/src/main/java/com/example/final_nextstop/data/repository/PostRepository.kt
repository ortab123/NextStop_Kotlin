package com.example.final_nextstop.data.repository

import com.example.final_nextstop.data.local_db.PostDao
import com.example.final_nextstop.data.model.Post
import javax.inject.Inject


class PostRepository @Inject constructor(
    private val postDao: PostDao
) {
    fun getPosts() = postDao.getPosts()

    suspend fun addPost(post:Post){
        postDao.addPost(post)
    }

    suspend fun deletePost(post:Post){
        postDao.deletePost(post)
    }

    fun getPost(id:Int) = postDao.getPost(id)

    suspend fun deleteALL(){
         postDao.deleteAll()
    }

    suspend fun updatePost(post: Post) {
        postDao.updatePost(post)
    }

    suspend fun updateProfileImageUri(id: Int, profileImageUri: String) {
        postDao.updateProfileImageUri(id, profileImageUri)
    }

    fun getFavoritePosts() = postDao.getFavoritePosts()

    suspend fun updateFavorite(postId: Int, isFavorite: Boolean) {
        postDao.updateFavorite(postId, isFavorite)
    }


}