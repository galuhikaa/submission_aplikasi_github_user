package com.dicoding.submissionawalaplikasigithubuser.bookmark

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class EntityUser (
    @ColumnInfo(name = "login")
    val login: String,

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "avatar")
    val avatar_url: String,

    @ColumnInfo(name = "follower")
    val followersUrl: String,

    @ColumnInfo(name = "following")
    val followingUrl: String,
) : Parcelable