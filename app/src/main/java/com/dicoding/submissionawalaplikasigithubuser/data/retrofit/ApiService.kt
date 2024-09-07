package com.dicoding.submissionawalaplikasigithubuser.data.retrofit

import com.dicoding.submissionawalaplikasigithubuser.data.response.ApiResponse
import com.dicoding.submissionawalaplikasigithubuser.data.response.GitHubResponse
import com.dicoding.submissionawalaplikasigithubuser.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getGithub(): Call<ArrayList<ApiResponse>>

    @GET("search/users")
    fun getSearch(@Query("q") login: String): Call<GitHubResponse>

    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<UserResponse>

    @GET("users/{username}/followers")
    fun getFollower(@Path("username") username: String): Call<ArrayList<ApiResponse>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<ApiResponse>>
}