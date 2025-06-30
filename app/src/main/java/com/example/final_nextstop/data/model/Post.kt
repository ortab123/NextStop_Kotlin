package com.example.final_nextstop.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "place")
    val place: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "images")
    val images: List<String>? = null,

    @ColumnInfo(name = "profileImageUri")
    val profileImageUri: String?,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean = false

) : Parcelable
