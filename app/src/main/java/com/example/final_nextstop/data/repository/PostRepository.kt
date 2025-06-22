package com.example.final_nextstop.data.repository

import android.app.Application
import com.example.final_nextstop.data.local_db.PostDao
import com.example.final_nextstop.data.local_db.PostDataBase
import com.example.final_nextstop.data.model.Post

class PostRepository(application: Application) {

    private var postDao:PostDao?

    init{
        val db = PostDataBase.getDatabase(application.applicationContext)
        postDao =db?.postsDao()
    }

    fun getPosts() = postDao?.getPosts()

    suspend fun addPost(post:Post){
        postDao?.addPost(post)
    }

    suspend fun deletePost(post:Post){
        postDao?.deletePost(post)
    }

    fun getPost(id:Int) = postDao?.getPost(id)

    suspend fun deleteALL(){
         postDao?.deleteAll()
    }

    suspend fun updatePost(post: Post) {
        postDao?.updatePost(post)
    }

    suspend fun updateProfileImageUri(id: Int, profileImageUri: String) {
        postDao?.updateProfileImageUri(id, profileImageUri)
    }


}