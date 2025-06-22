package com.example.final_nextstop.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.final_nextstop.data.model.Post
import com.example.final_nextstop.data.repository.PostRepository
import kotlinx.coroutines.launch

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository(application)
    val posts : LiveData<List<Post>>? = repository.getPosts()

    private val _profileImageUri = MutableLiveData<String?>()
    val profileImageUri: LiveData<String?> get() = _profileImageUri

    private val _chosenPost =  MutableLiveData<Post>()

    val chosenPost : LiveData<Post> get() = _chosenPost

    private val _imageUris = MutableLiveData<List<Uri>>(emptyList())
    val imageUris: LiveData<List<Uri>> get() = _imageUris

    fun setImageUris(uris: List<Uri>) {
        _imageUris.value = uris
    }

    fun getImageUris(): List<Uri> = _imageUris.value ?: emptyList()

    fun addImageUri(uri: Uri) {
        val currentList = _imageUris.value?.toMutableList() ?: mutableListOf()
        if (currentList.size < 10) {
            currentList.add(uri)
            _imageUris.value = currentList
        }
    }

    fun removeImageUri(uri: Uri) {
        val currentList = _imageUris.value?.toMutableList() ?: return
        currentList.remove(uri)
        _imageUris.value = currentList
    }

    fun setPost(post:Post){
        _chosenPost.value = post
    }

    fun addPost(post:Post){
        viewModelScope.launch {
            repository.addPost(post)
        }
    }

    fun deletePost(post:Post){
        viewModelScope.launch {
            repository.deletePost(post)
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteALL()
        }
    }

    fun updatePost(post:Post){
        viewModelScope.launch {
            repository.updatePost(post)
        }
    }

    fun updateProfileImageUri(id: Int, profileImageUri: String) {
        viewModelScope.launch {
            repository.updateProfileImageUri(id, profileImageUri)
        }
    }

}