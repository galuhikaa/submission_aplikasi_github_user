package com.dicoding.submissionawalaplikasigithubuser

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.submissionawalaplikasigithubuser.Fragment.TabAdapter
import com.dicoding.submissionawalaplikasigithubuser.bookmark.EntityUser
import com.dicoding.submissionawalaplikasigithubuser.bookmark.UserDb
import com.dicoding.submissionawalaplikasigithubuser.data.response.UserResponse
import com.dicoding.submissionawalaplikasigithubuser.data.retrofit.ApiConfig
import com.dicoding.submissionawalaplikasigithubuser.databinding.ActivityDetailBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPage: ViewPager2
    private lateinit var tabAdapter: TabAdapter

    private var cek:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nama = intent.getStringExtra("key_nama")
        val id = intent.getIntExtra("id", 0)

        if (id != null){
            cekUser(id)
        }

        if (nama != null) {
            getUserdata(nama)
        }

        tabLayout = binding.tabs
        viewPage = binding.viewPager

        if (nama != null) {
            tabAdapter = TabAdapter(supportFragmentManager, lifecycle, nama)
        }

        tabLayout.addTab(tabLayout.newTab().setText("Followers"))
        tabLayout.addTab(tabLayout.newTab().setText("Following"))

        viewPage.adapter = tabAdapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab != null){
                    viewPage.currentItem = tab.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        viewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    fun getUserdata(nama: String){
        showLoading(true)
        ApiConfig.apiService.getUser(nama).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    if (userData != null) {
                        binding.nameProfile.text = userData.name
                        binding.follower.text = userData.followers.toString()+" Followers"
                        binding.following.text = userData.following.toString()+" Following"
                        binding.login.text = userData.login
                        Glide.with(this@DetailActivity)
                            .load(userData.avatar_url)
                            .into(binding.photoProfile)
                        showLoading(false)
                        binding.btnfavorite.setOnClickListener {
                            if (cek == true){
                                deleteUser(userData.id.toInt(), userData.login, userData.avatar_url, userData.followers_url, userData.following_url)
                                binding.btnfavorite.changeIconColor(R.color.white)
                                cek = false
                            }else{
                                addUser(userData.id.toInt(), userData.login, userData.avatar_url, userData.followers_url, userData.following_url)
                                binding.btnfavorite.changeIconColor(R.color.red)
                                cek = true
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("error", ""+t.stackTraceToString())
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    fun addUser(id:Int, login: String, avatar:String, follower:String, following: String ){
        CoroutineScope(Dispatchers.IO).launch {
            val user = EntityUser(login = login, id = id, avatar_url = avatar, followersUrl = follower, followingUrl = following)
            UserDb(this@DetailActivity).userDao().insert(user)
        }
   }

    fun cekUser(id : Int){
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserDb(this@DetailActivity).userDao().findById(id)
            runOnUiThread {
                if (user != null) {
                    binding.btnfavorite.changeIconColor(R.color.red)
                    cek = true
                } else {
                    binding.btnfavorite.changeIconColor(R.color.white)
                    cek = false
                }
            }
        }
    }

    fun deleteUser(id:Int, login: String, avatar:String, follower:String, following: String ){
        CoroutineScope(Dispatchers.IO).launch {
            val user = EntityUser(login = login, id = id, avatar_url = avatar, followersUrl = follower, followingUrl = following)
            UserDb(this@DetailActivity).userDao().delete(user)
        }
    }

    fun FloatingActionButton.changeIconColor(@ColorRes color: Int){
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
    }
}